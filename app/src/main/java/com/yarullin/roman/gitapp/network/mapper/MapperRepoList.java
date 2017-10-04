package com.yarullin.roman.gitapp.network.mapper;

import com.yarullin.roman.gitapp.network.responce.SearchRepoResult;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepositoryList;

public class MapperRepoList {
    private final MapperRepo mapperRepo = new MapperRepo();
    private final String query;

    public MapperRepoList(String query) {
        this.query = query;
    }

    public ModelRepositoryList mapTo(SearchRepoResult searchRepoResult) {
        return new ModelRepositoryList()
                .setRepositoryList(mapperRepo.mapTo(searchRepoResult.getItems()))
                .setTotalCount(searchRepoResult.getTotalCount())
                .setCurrentSearchQuery(query);
    }
}
