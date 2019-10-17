package com.yarullin.roman.gitapp.interactor;

import androidx.annotation.NonNull;

import com.yarullin.roman.gitapp.BuildConfig;
import com.yarullin.roman.gitapp.GithubConfigHelper;
import com.yarullin.roman.gitapp.interactor.base.UseCase;
import com.yarullin.roman.gitapp.network.responce.AccessTokenModel;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class UseCaseLogin extends UseCase<AccessTokenModel> {
    private final String tokenCode;

    public UseCaseLogin(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    @NonNull
    @Override
    protected Flowable<AccessTokenModel> buildUseCaseObservable() {
        return gitHubOAuthApi.getAccessToken(
                    tokenCode,
                    GithubConfigHelper.getClientId(),
                    GithubConfigHelper.getSecret(),
                    BuildConfig.APPLICATION_ID)
                .doOnNext(accessTokenModel -> sharedPrefs.putToken(accessTokenModel.getAccessToken()))
                .subscribeOn(Schedulers.io());
    }
}
