package com.yarullin.roman.gitapp.network.mapper;

import android.support.annotation.NonNull;

import com.yarullin.roman.gitapp.network.responce.Repo;
import com.yarullin.roman.gitapp.persistence.entity.ModelRepository;

public class MapperRepo extends BaseMapper<ModelRepository, Repo> {
    private MapperOwner mapperOwner = new MapperOwner();

    @NonNull
    @Override
    public ModelRepository mapTo(Repo repo) {
        return new ModelRepository()
                .setId(repo.getId())
                .setDescription(repo.getDescription())
                .setForksCount(repo.getForksCount())
                .setFullName(repo.getFullName())
                .setName(repo.getName())
                .setOwner(mapperOwner.mapTo(repo.getOwner()))
                .setStargazersCount(repo.getStargazersCount());
    }
}
