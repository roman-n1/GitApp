package com.yarullin.roman.gitapp.base.ui;

import android.content.Intent;
import android.net.Uri;

import com.yarullin.roman.gitapp.BuildConfig;
import com.yarullin.roman.gitapp.GithubConfigHelper;
import com.yarullin.roman.gitapp.base.model.BaseLoginViewModel;

public abstract class BaseLoginActivity extends BaseActivity {
    private final String REDIRECT_URL = "gitapp://login";

    @Override
    protected void onStart() {
        super.onStart();
        if (containsRedirectUrl(getIntent())) {
            onHandleAuthIntent(getIntent());
            setIntent(null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (containsRedirectUrl(intent)) {
            onHandleAuthIntent(intent);
            setIntent(null);
        }
    }

    private boolean containsRedirectUrl(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri uri = intent.getData();
            if (uri.toString().startsWith(getRedirectUrl())) {
                return true;
            }
        }
        return false;
    }

    public void onHandleAuthIntent(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri uri = intent.getData();
            if (uri.toString().startsWith(getRedirectUrl())) {
                getBaseLoginViewModel().handleUri(uri);
            }
        }
    }

    protected String getRedirectUrl() {
        return REDIRECT_URL;
    }

    protected abstract BaseLoginViewModel getBaseLoginViewModel();

    protected void login() {
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, getGitHubLoginUri())
                .addCategory(Intent.CATEGORY_BROWSABLE);

        startActivity(browserIntent);
    }

    private static final String OAUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String PARAM_CLIENT_ID = "client_id";
    private static final String PARAM_CALLBACK_URI = "redirect_uri";
    private static final String PARAM_STATE = "state";

    private Uri getGitHubLoginUri() {
        return Uri.parse(OAUTH_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_CLIENT_ID, GithubConfigHelper.GITHUB_CLIENT_ID)
                .appendQueryParameter(PARAM_CALLBACK_URI, getRedirectUrl())
                .appendQueryParameter(PARAM_STATE, BuildConfig.APPLICATION_ID)
                .build();
    }
}
