package com.yarullin.roman.gitapp.application;

import android.app.Application;

import com.yarullin.roman.gitapp.di.component.AppComponent;
import com.yarullin.roman.gitapp.di.component.DaggerAppComponent;
import com.yarullin.roman.gitapp.di.module.AppModule;

import io.realm.Realm;

public class GitApp extends Application {
    private static GitApp instance;
    private AppComponent component;

    public static AppComponent getComponent() {
        return instance.component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRealm();
        initDi();
    }

    private void initDi() {
        component = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void initRealm() {
        Realm.init(this);
    }
}