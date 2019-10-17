package com.yarullin.roman.gitapp.di.module;

import androidx.annotation.NonNull;

import com.yarullin.roman.gitapp.BuildConfig;
import com.yarullin.roman.gitapp.network.GitHubRestApi;
import com.yarullin.roman.gitapp.network.GitHubOAuthApi;
import com.yarullin.roman.gitapp.persistence.base.SharedPrefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(SharedPrefs sharedPrefs) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(logging);
        }
        httpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            String accessToken = sharedPrefs.getToken();
            if (accessToken != null) {
                Request.Builder builder = request.newBuilder();
                builder.addHeader("Authorization", "token " + accessToken);
                request = builder.build();
            }
            return chain.proceed(request);
        });
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@NonNull OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    GitHubOAuthApi provideOAuthApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl("https://github.com/")
                .build()
                .create(GitHubOAuthApi.class);
    }

    @Provides
    @Singleton
    GitHubRestApi provideRestApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl("https://api.github.com/")
                .build()
                .create(GitHubRestApi.class);
    }
}
