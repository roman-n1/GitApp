package com.yarullin.roman.gitapp.features.authorization.model;

import com.yarullin.roman.gitapp.base.model.BaseLoginViewModel;
import com.yarullin.roman.gitapp.features.authorization.state.LoginViewState;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class LoginViewModel extends BaseLoginViewModel<LoginViewState> {
    private PublishSubject<Boolean> skipLoginPublishSubject = PublishSubject.create();

    public LoginViewModel() {
        super();
        merge();
    }

    private void merge() {
        Flowable<PartialState> skipLoginFlowable = skipLoginPublishSubject.toFlowable(BackpressureStrategy.LATEST)
                .map(aBoolean -> PartialState.skipLogin());

        Flowable<PartialState> loginStateFlowable = getLoginFlowable()
                .map(loginState -> {
                    if (loginState.isLoginClicked()) {
                        return PartialState.loginClicked();
                    } else if (loginState.isLoginInProgress()) {
                        return PartialState.inProgress();
                    } else if (loginState.isLoginSuccess()) {
                        return PartialState.loginComplete();
                    } else if (loginState.isError()) {
                        return PartialState.error(loginState.getError());
                    }
                    return PartialState.error(new Throwable("loginStateFlowable error"));
                });

        disposables.add(
            Flowable.merge(skipLoginFlowable, loginStateFlowable)
                    .scan(LoginViewState.initialState(), this::reduce)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(loginViewState -> screenLiveData.setValue(loginViewState),
                            throwable -> {
                                throw new IllegalStateException();
                            })
        );
    }

    private LoginViewState reduce(LoginViewState currentState, PartialState partialState) {
        if (partialState.inProgress) {
            return LoginViewState.inProgress();
        } else if (partialState.loginComplete) {
            return LoginViewState.loginComplete();
        } else if (partialState.skipLogin) {
            return LoginViewState.skipLogin();
        } else if (partialState.error != null) {
            return LoginViewState.error(partialState.error.getMessage());
        } else if (partialState.loginClicked) {
            return LoginViewState.loginClicked();
        }
        return LoginViewState.error("reduce() error");
    }

    public void skipLoginClicked() {
        skipLoginPublishSubject.onNext(true);
    }

    public void loginClicked() {
        login();
    }

    static class PartialState {
        private final boolean skipLogin;
        private final boolean inProgress;
        private final boolean loginComplete;
        private final boolean loginClicked;
        private final Throwable error;

        public PartialState(boolean skipLogin, boolean inProgress, boolean loginComplete, Throwable error, boolean loginClicked) {
            this.skipLogin = skipLogin;
            this.inProgress = inProgress;
            this.loginComplete = loginComplete;
            this.error = error;
            this.loginClicked = loginClicked;
        }

        static PartialState skipLogin() {
            return new PartialState(true, false, false, null, false);
        }

        static PartialState inProgress() {
            return new PartialState(false, true, false, null, false);
        }

        static PartialState loginComplete() {
            return new PartialState(false, false, true, null, false);
        }

        static PartialState error(Throwable throwable) {
            return new PartialState(false, false, false, throwable, false);
        }

        public static PartialState loginClicked() {
            return new PartialState(false, false, false, null, true);
        }
    }
}
