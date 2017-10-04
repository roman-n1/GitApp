package com.yarullin.roman.gitapp.persistence.entity;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ModelRepositoryList implements RealmModel {
    private int totalCount;
    private RealmList<ModelRepository> repositoryList;
    private long lastUpdateTimestamp;
    private String query;

    public ModelRepositoryList setCurrentSearchQuery(String query) {
        this.query = query;
        return this;
    }

    public String getCurrentSearchQuery() {
        return query;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ModelRepositoryList setTotalCount(int page) {
        this.totalCount = page;
        return this;
    }

    public RealmList<ModelRepository> getRepositoryList() {
        return repositoryList;
    }

    public ModelRepositoryList setRepositoryList(List<ModelRepository> repositoryList) {
        this.repositoryList = new RealmList<>();
        this.repositoryList.addAll(repositoryList);
        return this;
    }

    public ModelRepositoryList setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        return this;
    }
}
