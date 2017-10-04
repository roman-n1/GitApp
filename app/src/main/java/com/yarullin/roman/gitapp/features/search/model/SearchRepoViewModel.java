package com.yarullin.roman.gitapp.features.search.model;

import android.support.annotation.Nullable;

import com.yarullin.roman.gitapp.application.GitApp;
import com.yarullin.roman.gitapp.base.model.BaseLoginViewModel;
import com.yarullin.roman.gitapp.features.search.state.SearchRepoViewState;
import com.yarullin.roman.gitapp.interactor.UseCaseSearchResult;
import com.yarullin.roman.gitapp.interactor.UseCaseSearchLoadNextPage;
import com.yarullin.roman.gitapp.persistence.base.SharedPrefs;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepositoryList;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class SearchRepoViewModel extends BaseLoginViewModel<SearchRepoViewState> {
    @Inject protected SharedPrefs sharedPrefs;

    private final PublishSubject<String> searchSubject;
    private final PublishSubject<Integer> nextPageSubject;
    private final PublishSubject<Boolean> pullToRefreshSubject;

    private SearchRepoViewState currentState;

    public SearchRepoViewModel() {
        GitApp.getComponent().inject(this);
        this.searchSubject = PublishSubject.create();
        this.nextPageSubject = PublishSubject.create();
        this.pullToRefreshSubject = PublishSubject.create();
        merge();
    }

    private void merge() {
        Flowable<PartialState> loginFlowable = getLoginFlowable()
                .map(loginState -> {
                    if (loginState.isLogoutSuccess()) {
                        return PartialState.logoutComplete();
                    } else if (loginState.isLoginClicked()) {
                        return PartialState.loginClicked();
                    } else if (loginState.isLoginInProgress()) {
                        return PartialState.loginInProgress();
                    } else if (loginState.isLoginSuccess()) {
                        return PartialState.loginComplete();
                    } else if (loginState.isError()) {
                        return PartialState.error(loginState.getError());
                    }
                    return PartialState.error(new Throwable("loginStateFlowable error"));
                });

        List<Publisher<PartialState>> flowableList = new ArrayList<>();
        flowableList.add(createSearchFlowable());
        flowableList.add(createNextPageFlowable());
        flowableList.add(createPullToRefreshFlowable());
        flowableList.add(loginFlowable);

        disposables.add(
                Flowable.merge(flowableList)
                        .scan(SearchRepoViewState.initialState(sharedPrefs.getToken() != null), this::reduce)
                        .doOnNext(state -> currentState = state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(loginViewState -> screenLiveData.setValue(loginViewState),
                                throwable -> {
                                    throw new IllegalStateException(throwable);
                                })
        );
    }


    private SearchRepoViewState reduce(SearchRepoViewState previousState, PartialState partialState) {
        switch (partialState.state) {
            case PartialState.STATE_LOGOUT_FINISHED:
                return SearchRepoViewState.logoutFinished(previousState);
            case PartialState.STATE_LOGIN_CLICKED:
                return SearchRepoViewState.loginClicked(previousState);
            case PartialState.STATE_INITIAL:
                return SearchRepoViewState.searchingComplete(previousState, partialState);
            case PartialState.STATE_SEARCHING:
                return SearchRepoViewState.searching(previousState, partialState.query);
            case PartialState.STATE_SEARCHING_COMPLETE:
                return SearchRepoViewState.searchingComplete(previousState, partialState);
            case PartialState.STATE_LOADING_NEXT_PAGE:
                return SearchRepoViewState.loadingNextPage(previousState);
            case PartialState.STATE_LOADED_NEXT_PAGE:
                return SearchRepoViewState.loadedNextPage(previousState);
            case PartialState.STATE_REFRESHING_WITH_PTR:
                return SearchRepoViewState.refreshing(previousState);
            case PartialState.STATE_REFRESHED_WITH_PTR:
                return SearchRepoViewState.refreshed(previousState);
            case PartialState.STATE_LOGIN_IN_PROGRESS:
                return SearchRepoViewState.loginInProgress(previousState);
            case PartialState.STATE_LOGIN_FINISHED:
                return SearchRepoViewState.loginFinished(previousState);
            case PartialState.STATE_ERROR:
                return SearchRepoViewState.error(previousState, partialState);
            default:
                return previousState;
        }
    }


    public void search(@Nullable String query) {
        if (query == null) {
            query = ""; //Because nulls isn't allowed in RxJava 2
        }
        searchSubject.onNext(query);
    }

    public void searchNextPage(int offset) {
        nextPageSubject.onNext(offset);
    }

    public void refresh() {
        pullToRefreshSubject.onNext(true);
    }

    private Flowable<PartialState> createPullToRefreshFlowable() {
        return pullToRefreshSubject.toFlowable(BackpressureStrategy.LATEST).switchMap(ignored -> toPullToRefreshFlowable());
    }

    private Flowable<PartialState> toPullToRefreshFlowable() {
        return new UseCaseSearchResult(currentState.searchQuery)
                .execute()
                .map(ignored -> PartialState.refreshedWithPtr())
                .startWith(PartialState.refreshingWithPtr())
                .onErrorResumeNext(throwable -> {
                    return Flowable.just(PartialState.error(throwable));
                });
    }

    private Flowable<PartialState> createSearchFlowable() {
        return searchSubject.toFlowable(BackpressureStrategy.LATEST).switchMap(this::toSearchFlowable);
    }

    private Flowable<PartialState> toSearchFlowable(@Nullable String query) {
        return new UseCaseSearchResult(query)
                .execute()
                .observeOn(AndroidSchedulers.mainThread())
                .map(modelRepositoryList -> PartialState.searchCompleted(modelRepositoryList))
                .startWith(PartialState.searching(query))
                .onErrorResumeNext(throwable -> {
                    return Flowable.just(PartialState.error(throwable));
                });
    }

    private Flowable<PartialState> createNextPageFlowable() {
        return nextPageSubject.toFlowable(BackpressureStrategy.LATEST).flatMap(offset -> toNextPageFlowable(offset));
    }

    private Flowable<PartialState> toNextPageFlowable(int offset) {
        return new UseCaseSearchLoadNextPage(currentState.searchQuery, offset)
                .execute()
                .map(ignored -> PartialState.nextPageLoaded())
                .startWith(PartialState.loadNextPage())
                .onErrorResumeNext(throwable -> {
                    return Flowable.just(PartialState.error(throwable));
                });
    }

    public boolean isLoading() {
        return currentState.isLoadingNextPage();
    }

    public boolean isLastPage() {
        return currentState.isLastPage();
    }

    public void onLoginClicked() {
        if (sharedPrefs.getToken() == null) {
            login();
        } else {
            logout();
        }
    }

    public static class PartialState {
        private static final int STATE_INITIAL = 0;
        private static final int STATE_SEARCHING = 1;
        private static final int STATE_SEARCHING_COMPLETE = 2;
        private static final int STATE_LOADING_NEXT_PAGE = 3;
        private static final int STATE_LOADED_NEXT_PAGE = 4;
        private static final int STATE_REFRESHING_WITH_PTR = 5;
        private static final int STATE_REFRESHED_WITH_PTR = 6;
        private static final int STATE_LOGIN_IN_PROGRESS = 7;
        private static final int STATE_LOGIN_FINISHED = 8;
        private static final int STATE_ERROR = 9;
        private static final int STATE_LOGIN_CLICKED = 10;
        private static final int STATE_LOGOUT_FINISHED = 11;

        public final int state;
        public final String query;
        public final Throwable error;
        public final ModelRepositoryList modelRepositoryList;

        private PartialState(int state, String query, ModelRepositoryList modelRepositoryList, Throwable error) {
            this.state = state;
            this.query = query;
            this.modelRepositoryList = modelRepositoryList;
            this.error = error;
        }

        static PartialState searching(String query) {
            return new PartialState(STATE_SEARCHING, query, null, null);
        }

        static PartialState searchCompleted(ModelRepositoryList modelRepositoryList) {
            return new PartialState(STATE_SEARCHING_COMPLETE, null, modelRepositoryList, null);
        }

        static PartialState loadNextPage() {
            return new PartialState(STATE_LOADING_NEXT_PAGE, null, null, null);
        }

        static PartialState nextPageLoaded() {
            return new PartialState(STATE_LOADED_NEXT_PAGE, null, null, null);
        }

        static PartialState refreshingWithPtr() {
            return new PartialState(STATE_REFRESHING_WITH_PTR, null, null, null);
        }

        static PartialState refreshedWithPtr() {
            return new PartialState(STATE_REFRESHED_WITH_PTR, null, null, null);
        }

        static PartialState loginInProgress() {
            return new PartialState(STATE_LOGIN_IN_PROGRESS, null, null, null);
        }

        static PartialState loginComplete() {
            return new PartialState(STATE_LOGIN_FINISHED, null, null, null);
        }

        static PartialState error(Throwable throwable) {
            return new PartialState(STATE_ERROR, null, null, throwable);
        }

        static PartialState initial(ModelRepositoryList modelRepositoryList) {
            return new PartialState(STATE_INITIAL, null, modelRepositoryList, null);
        }

        static PartialState loginClicked() {
            return new PartialState(STATE_LOGIN_CLICKED, null, null, null);
        }

        public static PartialState logoutComplete() {
            return new PartialState(STATE_LOGOUT_FINISHED, null, null, null);
        }
    }
}
