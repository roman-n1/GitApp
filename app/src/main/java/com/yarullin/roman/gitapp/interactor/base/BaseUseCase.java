package com.yarullin.roman.gitapp.interactor.base;

import com.yarullin.roman.gitapp.application.GitApp;
import com.yarullin.roman.gitapp.network.GitHubOAuthApi;
import com.yarullin.roman.gitapp.network.GitHubRestApi;
import com.yarullin.roman.gitapp.persistence.base.GitHubRepository;
import com.yarullin.roman.gitapp.persistence.base.SharedPrefs;

import javax.inject.Inject;

public abstract class BaseUseCase {
    @Inject protected GitHubOAuthApi gitHubOAuthApi;
    @Inject protected GitHubRestApi gitHubRestApi;
    @Inject protected SharedPrefs sharedPrefs;
    @Inject protected GitHubRepository gitHubRepository;

    protected BaseUseCase() {
        GitApp.getComponent().inject(this);
    }
}