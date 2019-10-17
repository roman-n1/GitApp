package com.yarullin.roman.gitapp.interactor;

import androidx.annotation.NonNull;

import com.yarullin.roman.gitapp.interactor.base.UseCase;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class UseCaseLogout extends UseCase<Boolean> {
    @NonNull
    @Override
    protected Flowable<Boolean> buildUseCaseObservable() {
        return Flowable.create(e -> {
            sharedPrefs.clearToken();
            e.onNext(true);
        }, BackpressureStrategy.LATEST);
    }
}
