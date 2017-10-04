package com.yarullin.roman.gitapp.features.search.state;

import com.yarullin.roman.gitapp.features.search.model.SearchRepoViewModel;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepositoryList;

public class SearchRepoViewState {
    private static final int INITIAL = 0;
    private static final int SEARCHING = 1;
    private static final int NEXT_PAGE = 2;
    private static final int REFRESHING = 3;
    private static final int LOGIN = 4;
    private static final int LOGOUT = 5;
    private static final int ERROR = 6;
    private static final int LOGIN_CLICKED = 7;

    private final int state;
    private final boolean inProgress;
    private final boolean isLogined;
    public final ModelRepositoryList modelRepositoryList;
    public final String searchQuery;
    public final Throwable error;

    private SearchRepoViewState(int state, boolean inProgress, boolean isLogined, ModelRepositoryList modelRepositoryList, String searchQuery, Throwable error) {
        this.state = state;
        this.inProgress = inProgress;
        this.isLogined = isLogined;
        this.modelRepositoryList = modelRepositoryList;
        this.searchQuery = searchQuery;
        this.error = error;
    }

    public static SearchRepoViewState initialState(boolean isLogined) {
        return new SearchRepoViewState(INITIAL, false, isLogined,  null, null, null);
    }

    public boolean isInitialState() {
        return state == INITIAL;
    }

    public boolean isLogined() {
        return isLogined;
    }

    public static SearchRepoViewState searching(SearchRepoViewState previousState, String query) {
        return new SearchRepoViewState(SEARCHING, true, previousState.isLogined(), previousState.modelRepositoryList, query, null);
    }

    public boolean isSearching() {
        return state == SEARCHING && inProgress;
    }

    public static SearchRepoViewState searchingComplete(SearchRepoViewState previousState, SearchRepoViewModel.PartialState partialState) {
        return new SearchRepoViewState(SEARCHING, false, previousState.isLogined(), partialState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isSearchingComplete() {
        return state == SEARCHING && !inProgress;
    }

    public static SearchRepoViewState loadingNextPage(SearchRepoViewState previousState) {
        return new SearchRepoViewState(NEXT_PAGE, true, previousState.isLogined(), previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isLoadingNextPage() {
        return state == NEXT_PAGE && inProgress;
    }

    public static SearchRepoViewState loadedNextPage(SearchRepoViewState previousState) {
        return new SearchRepoViewState(NEXT_PAGE, false, previousState.isLogined(), previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isLoadingNextPageComplete() {
        return state == NEXT_PAGE && !inProgress;
    }

    public static SearchRepoViewState refreshing(SearchRepoViewState previousState) {
        return new SearchRepoViewState(REFRESHING, true, previousState.isLogined(),previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isRefreshing() {
        return state == REFRESHING && inProgress;
    }

    public static SearchRepoViewState refreshed(SearchRepoViewState previousState) {
        return new SearchRepoViewState(REFRESHING, false, previousState.isLogined(),previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isRefreshingComplete() {
        return state == REFRESHING && !inProgress;
    }

    public static SearchRepoViewState loginInProgress(SearchRepoViewState previousState) {
        return new SearchRepoViewState(LOGIN, true, previousState.isLogined(),previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isLoginInProgress() {
        return state == LOGIN && inProgress;
    }

    public static SearchRepoViewState loginFinished(SearchRepoViewState previousState) {
        return new SearchRepoViewState(LOGIN, false, true, previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isLoginComplete() {
        return state == LOGIN && !inProgress;
    }

    public static SearchRepoViewState error(SearchRepoViewState previousState, SearchRepoViewModel.PartialState partialState) {
        return new SearchRepoViewState(ERROR, false, previousState.isLogined(),previousState.modelRepositoryList, previousState.searchQuery, partialState.error);
    }

    public boolean isError() {
        return error != null;
    }

    public boolean isLastPage() {
        return modelRepositoryList.getTotalCount() == modelRepositoryList.getRepositoryList().size();
    }

    public static SearchRepoViewState logoutFinished(SearchRepoViewState previousState) {
        return new SearchRepoViewState(LOGOUT, false, false, previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public static SearchRepoViewState loginClicked(SearchRepoViewState previousState) {
        return new SearchRepoViewState(LOGIN_CLICKED, false, previousState.isLogined, previousState.modelRepositoryList, previousState.searchQuery, null);
    }

    public boolean isLoginClicked() {
        return state == LOGIN_CLICKED;
    }
}
