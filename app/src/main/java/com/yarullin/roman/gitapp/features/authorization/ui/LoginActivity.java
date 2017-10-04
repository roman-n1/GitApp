package com.yarullin.roman.gitapp.features.authorization.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.yarullin.roman.gitapp.R;
import com.yarullin.roman.gitapp.base.model.BaseLoginViewModel;
import com.yarullin.roman.gitapp.base.ui.BaseLoginActivity;
import com.yarullin.roman.gitapp.features.authorization.model.LoginViewModel;
import com.yarullin.roman.gitapp.features.authorization.state.LoginViewState;
import com.yarullin.roman.gitapp.features.search.ui.SearchRepoActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseLoginActivity {

    @BindView(R.id.progress_bar) View progressBar;
    @BindView(R.id.login_button) Button loginButton;
    @BindView(R.id.skip_login_button) Button skipLoginButton;

    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.observe().observe(this, this::render);
    }

    protected void render(LoginViewState viewModel) {
        progressBar.setVisibility(viewModel.requestInProgress ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!viewModel.requestInProgress);
        skipLoginButton.setEnabled(!viewModel.requestInProgress);

        if (viewModel.isLoginClicked) {
            login();
        }

        if (viewModel.error != null) {
            Snackbar.make(getWindow().getDecorView().getRootView(), viewModel.error, Snackbar.LENGTH_LONG).show();
        }

        if (viewModel.skipLogin) {
            startActivity(SearchRepoActivity.createIntent(this));
        }

        if (viewModel.requestOk) {
            startActivity(SearchRepoActivity.createIntent(this));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected BaseLoginViewModel getBaseLoginViewModel() {
        return viewModel;
    }

    @OnClick(R.id.login_button)
    void onLoginButtonClicked() {
        viewModel.loginClicked();
    }

    @OnClick(R.id.skip_login_button)
    void onSkipLoginButtonClicked() {
        viewModel.skipLoginClicked();
    }
}
