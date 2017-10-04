package com.yarullin.roman.gitapp.di.module;

import android.content.Context;
import android.preference.PreferenceManager;

import com.yarullin.roman.gitapp.persistence.GitHubRepositoryImpl;
import com.yarullin.roman.gitapp.persistence.base.GitHubRepository;
import com.yarullin.roman.gitapp.persistence.base.SharedPrefs;
import com.yarullin.roman.gitapp.persistence.SharedPrefsImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistenceModule {
    @Singleton
    @Provides
    public SharedPrefs provideSharedPrefs(Context context) {
        return new SharedPrefsImpl(PreferenceManager.getDefaultSharedPreferences(context));
    }

    @Singleton
    @Provides
    public GitHubRepository provideGitHubRepository() {
        return new GitHubRepositoryImpl();
    }
}
