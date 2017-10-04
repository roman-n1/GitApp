package com.yarullin.roman.gitapp.network;

import com.yarullin.roman.gitapp.network.responce.SearchRepoResult;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubRestApi {
    int PAGE_SIZE = 30;

    @GET("search/repositories")
    Flowable<SearchRepoResult> searchRepositories(@Query(value = "q", encoded = true) String query, @Query("page") int page, @Query("per_page") int perPage);
}
