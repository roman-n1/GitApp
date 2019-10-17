package com.yarullin.roman.gitapp.features.authorization.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.yarullin.roman.gitapp.R;
import com.yarullin.roman.gitapp.base.model.BaseLoginViewModel;
import com.yarullin.roman.gitapp.base.ui.BaseLoginActivity;
import com.yarullin.roman.gitapp.features.authorization.model.LoginViewModel;
import com.yarullin.roman.gitapp.features.authorization.state.LoginViewState;
import com.yarullin.roman.gitapp.features.search.ui.SearchRepoActivity;

public class LoginActivity extends BaseLoginActivity {

    View progressBar;
    Button loginButton;
    Button skipLoginButton;

    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.progress_bar);
        loginButton = findViewById(R.id.login_button);
        skipLoginButton = findViewById(R.id.skip_login_button);

        loginButton.setOnClickListener(v -> viewModel.loginClicked());
        skipLoginButton.setOnClickListener(v -> viewModel.skipLoginClicked());

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

}
