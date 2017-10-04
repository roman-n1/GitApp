package com.yarullin.roman.gitapp.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    protected Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    protected Context provideContext() {
        return application.getApplicationContext();
    }
}