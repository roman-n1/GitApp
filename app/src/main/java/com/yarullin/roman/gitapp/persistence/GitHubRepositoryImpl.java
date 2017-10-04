package com.yarullin.roman.gitapp.persistence;

import android.support.annotation.NonNull;

import com.yarullin.roman.gitapp.persistence.base.GitHubRepository;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepositoryList;

import io.realm.Realm;

public class GitHubRepositoryImpl implements GitHubRepository {
    @NonNull
    @Override
    public ModelRepositoryList getRepositoryList() {
        Realm realm = Realm.getDefaultInstance();
        ModelRepositoryList result = realm.where(ModelRepositoryList.class).findFirst();
        if (result == null) {
            realm.beginTransaction();
                result = realm.createObject(ModelRepositoryList.class);
            realm.commitTransaction();
        }
        return result;
    }

    @Override
    public void putData(@NonNull ModelRepositoryList repositoryList) {
        try (Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction(realm -> {
                ModelRepositoryList saved = realm.where(ModelRepositoryList.class).findFirst();
                if (saved == null) {
                    saved = realm.createObject(ModelRepositoryList.class);
                }
                saved.setTotalCount(repositoryList.getTotalCount());
                saved.setCurrentSearchQuery(repositoryList.getCurrentSearchQuery());
                saved.setRepositoryList(repositoryList.getRepositoryList());
                saved.setLastUpdateTimestamp(System.currentTimeMillis());
            });
        }
    }

    @Override
    public void putDataNextPage(@NonNull ModelRepositoryList repositoryList) {
        try (Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction(realm -> {
                ModelRepositoryList saved = realm.where(ModelRepositoryList.class).findFirst();
                if (saved == null) {
                    saved = realm.createObject(ModelRepositoryList.class);
                    saved.setCurrentSearchQuery(repositoryList.getCurrentSearchQuery());
                    saved.setTotalCount(saved.getTotalCount());
                }
                saved.getRepositoryList().addAll(repositoryList.getRepositoryList());
                saved.setLastUpdateTimestamp(System.currentTimeMillis());
            });
        }
    }
}
