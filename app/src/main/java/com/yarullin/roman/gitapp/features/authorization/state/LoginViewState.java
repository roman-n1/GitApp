package com.yarullin.roman.gitapp.features.authorization.state;

public class LoginViewState {
    public final boolean skipLogin;
    public final boolean requestInProgress;
    public final boolean requestOk;
    public final String error;
    public final boolean isLoginClicked;

    private LoginViewState(boolean skipLogin, boolean requestInProgress, boolean requestOk, String error, boolean isLoginClicked) {
        this.skipLogin = skipLogin;
        this.requestInProgress = requestInProgress;
        this.requestOk = requestOk;
        this.error = error;
        this.isLoginClicked = isLoginClicked;
    }

    public static LoginViewState initialState() {
        return new LoginViewState(false, false, false, null, false);
    }

    public static LoginViewState skipLogin() {
        return new LoginViewState(true, false, false, null, false);
    }

    public static LoginViewState inProgress() {
        return new LoginViewState(false, true, false, null, false);
    }

    public static LoginViewState loginComplete() {
        return new LoginViewState(false, false, true, null, false);
    }

    public static LoginViewState loginClicked() {
        return new LoginViewState(false, false, false, null, true);
    }

    public static LoginViewState error(String error) {
        return new LoginViewState(false, false, false, error, false);
    }
}
