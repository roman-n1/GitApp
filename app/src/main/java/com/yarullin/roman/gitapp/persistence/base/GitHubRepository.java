package com.yarullin.roman.gitapp.persistence.base;

import androidx.annotation.NonNull;

import com.yarullin.roman.gitapp.persistence.entity.ModelRepositoryList;

public interface GitHubRepository {
    @NonNull
    ModelRepositoryList getRepositoryList();

    void putData(@NonNull ModelRepositoryList repositoryList);

    void putDataNextPage(@NonNull ModelRepositoryList repositoryList);
}
