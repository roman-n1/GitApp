package com.yarullin.roman.gitapp.interactor.base;

import android.support.annotation.NonNull;

import io.reactivex.Flowable;

public abstract class UseCase<ReturnType> extends BaseUseCase {

    protected UseCase() {}

    public Flowable<ReturnType> execute() {
        return buildUseCaseObservable();
    }

    @NonNull
    protected abstract Flowable<ReturnType> buildUseCaseObservable();
}
