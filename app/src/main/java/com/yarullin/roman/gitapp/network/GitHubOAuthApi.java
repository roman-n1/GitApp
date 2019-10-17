package com.yarullin.roman.gitapp.network;

import androidx.annotation.NonNull;

import com.yarullin.roman.gitapp.network.responce.AccessTokenModel;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GitHubOAuthApi {

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    Flowable<AccessTokenModel> getAccessToken(@NonNull @Field("code") String code,
                                              @NonNull @Field("client_id") String clientId,
                                              @NonNull @Field("client_secret") String clientSecret,
                                              @NonNull @Field("state") String state);
}
