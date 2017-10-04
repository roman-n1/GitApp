package com.yarullin.roman.gitapp.persistence.entity;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ModelRepository implements RealmModel {
    private int id;
    private String name;
    private String fullName;
    private String description;
    private int stargazersCount;
    private int forksCount;
    private ModelOwner owner;

    public int getId() {
        return id;
    }

    public ModelRepository setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ModelRepository setName(String name) {
        this.name = name;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public ModelRepository setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ModelRepository setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public ModelRepository setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
        return this;
    }

    public int getForksCount() {
        return forksCount;
    }

    public ModelRepository setForksCount(int forksCount) {
        this.forksCount = forksCount;
        return this;
    }

    public ModelOwner getOwner() {
        return owner;
    }

    public ModelRepository setOwner(ModelOwner owner) {
        this.owner = owner;
        return this;
    }
}
