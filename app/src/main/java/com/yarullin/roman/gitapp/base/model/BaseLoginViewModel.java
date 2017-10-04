package com.yarullin.roman.gitapp.base.model;

import android.net.Uri;

import com.yarullin.roman.gitapp.interactor.UseCaseLogin;
import com.yarullin.roman.gitapp.interactor.UseCaseLogout;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseLoginViewModel<T> extends BaseViewModel<T> {
    final private PublishSubject<String> loginStatePublishSubject;
    final private PublishSubject<Boolean> loginPublishSubject;

    protected BaseLoginViewModel() {
        loginStatePublishSubject = PublishSubject.create();
        loginPublishSubject = PublishSubject.create();
    }

    protected void login() {
        loginPublishSubject.onNext(true);
    }

    protected void logout() {
        loginPublishSubject.onNext(false);
    }

    protected static class LoginState {
        private static final int STATE_LOGIN = 1;
        private static final int STATE_LOGOUT = 2;
        private static final int STATE_ERROR = 3;
        private final int state;
        private final boolean inProgress;
        private final boolean isSuccess;
        private final boolean loginClicked;
        private final Throwable error;

        private LoginState(int state, boolean inProgress, boolean isSuccess, Throwable error, boolean loginClicked) {
            this.state = state;
            this.inProgress = inProgress;
            this.isSuccess = isSuccess;
            this.error = error;
            this.loginClicked = loginClicked;
        }

        private static LoginState successLogin() {
            return new LoginState(STATE_LOGIN, false, true, null, false);
        }

        public boolean isLoginSuccess() {
            return state == STATE_LOGIN && isSuccess;
        }

        private static LoginState successLogout() {
            return new LoginState(STATE_LOGOUT, false, true, null, false);
        }

        public boolean isLogoutSuccess() {
            return state == STATE_LOGOUT && isSuccess;
        }

        private static LoginState loginProgress() {
            return new LoginState(STATE_LOGIN, true, false, null, false);
        }

        public boolean isLoginInProgress() {
            return state == STATE_LOGIN && inProgress;
        }

        private static LoginState error(Throwable throwable) {
            return new LoginState(STATE_ERROR, false, false, throwable, false);
        }

        public boolean isError() {
            return error != null;
        }

        public Throwable getError() {
            return error;
        }

        private static LoginState loginClicked() {
            return new LoginState(STATE_LOGIN, false, false, null, true);
        }

        public boolean isLoginClicked() {
            return loginClicked;
        }
    }

    public void handleUri(Uri uri) {
        String tokenCode = uri.getQueryParameter("code");
        if (tokenCode == null) {
            tokenCode = "";
        }
        loginStatePublishSubject.onNext(tokenCode);
    }

    private Flowable<LoginState> createLoginStateFlowable() {
        return loginStatePublishSubject.toFlowable(BackpressureStrategy.LATEST)
                .flatMap(tokenCode -> {
                    if (tokenCode.isEmpty()) {
                        return Flowable.just(LoginState.error(new Throwable("")));
                    }
                    return new UseCaseLogin(tokenCode).execute()
                            .map(accessTokenModel -> LoginState.successLogin())
                            .startWith(LoginState.loginProgress())
                            .onErrorResumeNext(throwable -> {
                                return Flowable.just(LoginState.error(throwable));
                            });
                });
    }

    private Flowable<LoginState> createLoginFlowable() {
        return loginPublishSubject.toFlowable(BackpressureStrategy.LATEST)
                .flatMap(isLogin -> {
                    if (isLogin) {
                        return Flowable.just(LoginState.loginClicked());
                    } else {
                        return new UseCaseLogout().execute()
                                .map(aBoolean -> LoginState.successLogout());
                    }
                });
    }

    protected Flowable<LoginState> getLoginFlowable() {
        return Flowable.merge(createLoginFlowable(), createLoginStateFlowable());
    }
}
