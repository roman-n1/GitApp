package com.yarullin.roman.gitapp.interactor;

import android.support.annotation.NonNull;

import com.yarullin.roman.gitapp.interactor.base.UseCase;
import com.yarullin.roman.gitapp.network.GitHubRestApi;
import com.yarullin.roman.gitapp.network.mapper.MapperRepoList;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepositoryList;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class UseCaseSearchLoadNextPage extends UseCase<ModelRepositoryList> {
    private final String searchQuery;
    private final int offset;
    private final int page;

    public UseCaseSearchLoadNextPage(String searchQuery, int offset) {
        this.searchQuery = searchQuery;
        this.offset = offset;
        page = offset / GitHubRestApi.PAGE_SIZE + 1;
    }

    @NonNull
    @Override
    protected Flowable<ModelRepositoryList> buildUseCaseObservable() {
        return gitHubRestApi.searchRepositories(searchQuery, page, GitHubRestApi.PAGE_SIZE)
                .map(response -> new MapperRepoList(searchQuery).mapTo(response))
                .doOnNext(modelRepositoryList -> gitHubRepository.putDataNextPage(modelRepositoryList))
                .subscribeOn(Schedulers.io());
    }
}
