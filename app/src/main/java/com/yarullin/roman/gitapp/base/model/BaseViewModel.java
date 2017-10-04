package com.yarullin.roman.gitapp.base.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel<ScreenModel> extends ViewModel {
    protected final CompositeDisposable disposables = new CompositeDisposable();
    protected final MutableLiveData<ScreenModel> screenLiveData = new MutableLiveData<>();

    public LiveData<ScreenModel> observe() {
        return screenLiveData;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
