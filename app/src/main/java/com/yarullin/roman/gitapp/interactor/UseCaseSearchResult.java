package com.yarullin.roman.gitapp.interactor;

import androidx.annotation.NonNull;

import com.yarullin.roman.gitapp.interactor.base.UseCase;
import com.yarullin.roman.gitapp.network.GitHubRestApi;
import com.yarullin.roman.gitapp.network.mapper.MapperRepoList;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepositoryList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UseCaseSearchResult extends UseCase<ModelRepositoryList> {
    private final String query;

    public UseCaseSearchResult(String query) {
        this.query = query;
    }

    @NonNull
    @Override
    protected Flowable<ModelRepositoryList> buildUseCaseObservable() {
        return gitHubRestApi.searchRepositories(query, 1, GitHubRestApi.PAGE_SIZE)
                .map(response -> new MapperRepoList(query).mapTo(response))
                .doOnNext(modelRepositoryList -> gitHubRepository.putData(modelRepositoryList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ignored -> gitHubRepository.getRepositoryList());
    }
}
