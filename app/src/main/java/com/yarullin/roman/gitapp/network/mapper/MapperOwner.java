package com.yarullin.roman.gitapp.network.mapper;

import android.support.annotation.NonNull;

import com.yarullin.roman.gitapp.network.responce.Owner;
import com.yarullin.roman.gitapp.persistence.entity.ModelOwner;

public class MapperOwner extends BaseMapper<ModelOwner, Owner> {
    @NonNull
    @Override
    public ModelOwner mapTo(Owner owner) {
        return new ModelOwner()
                .setId(owner.getId())
                .setLogin(owner.getLogin())
                .setAvatarUrl(owner.getAvatarUrl());
    }
}
