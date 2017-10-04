package com.yarullin.roman.gitapp.features.search.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import com.yarullin.roman.gitapp.R;
import com.yarullin.roman.gitapp.base.model.BaseLoginViewModel;
import com.yarullin.roman.gitapp.base.ui.BaseLoginActivity;
import com.yarullin.roman.gitapp.features.search.model.SearchRepoViewModel;
import com.yarullin.roman.gitapp.features.search.state.SearchRepoViewState;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SearchRepoActivity extends BaseLoginActivity {
    private static final String SEARCH_KEY = "search_key";

    @BindView(R.id.pull_to_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView repoRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private SearchRepoAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Menu menu;

    private SearchView searchView;
    private SearchRepoViewModel viewModel;
    private String savedSearchQuery;

    public static Intent createIntent(Context context) {
        return new Intent(context, SearchRepoActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_repo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SearchRepoViewModel.class);
        initRecycler();
        if (savedInstanceState != null) {
            savedSearchQuery = savedInstanceState.getString(SEARCH_KEY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initRecycler() {
        layoutManager = new LinearLayoutManager(this);
        repoRecyclerView.setLayoutManager(layoutManager);
        repoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!viewModel.isLoading() && !viewModel.isLastPage() || progressBar.getVisibility() == View.VISIBLE) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        viewModel.searchNextPage(adapter.getItemCount());
                    }
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refresh();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        this.menu = menu;
        initSearchView(menu);
        viewModel.observe().observe(this, this::render);
        return true;
    }

    private void initSearchView(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        int options = searchView.getImeOptions();
        searchView.setImeOptions(options | EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        if (savedSearchQuery != null && !savedSearchQuery.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(savedSearchQuery, true);
            searchView.clearFocus();
        }

        RxSearchView.queryTextChanges(searchView)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(item -> item.length() > 2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entered -> viewModel.search(entered.toString()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_KEY, searchView.getQuery().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                viewModel.onLoginClicked();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void render(SearchRepoViewState state) {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

        MenuItem loginMenuItem = menu.findItem(R.id.action_login);
        loginMenuItem.setTitle(state.isLogined() ? R.string.logout : R.string.login);

        if (state.isLoginClicked()) {
            login();
        }

        if (state.modelRepositoryList != null && state.modelRepositoryList.getRepositoryList() != null) {
            if (repoRecyclerView.getAdapter() == null) {
                adapter = new SearchRepoAdapter(state.modelRepositoryList.getRepositoryList());

                repoRecyclerView.setAdapter(adapter);
            }
        }
        if (state.isInitialState()) {
            progressBar.setVisibility(View.GONE);
        } else if (state.isSearching()) {
            progressBar.setVisibility(View.VISIBLE);
        } else if (state.isSearchingComplete()) {
            progressBar.setVisibility(View.GONE);
        } else if (state.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        } else if (state.isRefreshingComplete()) {
            swipeRefreshLayout.setRefreshing(false);
        } else if (state.isLoginInProgress()) {
            progressBar.setVisibility(View.VISIBLE);
        } else if (state.isLoginComplete()) {
            progressBar.setVisibility(View.GONE);
        } else if (state.isLoadingNextPageComplete()) {
            progressBar.setVisibility(View.GONE);
        } if (state.isLoadingNextPage()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected BaseLoginViewModel getBaseLoginViewModel() {
        return viewModel;
    }
}
