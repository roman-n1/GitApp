package com.yarullin.roman.gitapp.persistence.entity;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ModelOwner implements RealmModel {
    private String login;
    private int id;
    private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public ModelOwner setLogin(String login) {
        this.login = login;
        return this;
    }

    public int getId() {
        return id;
    }

    public ModelOwner setId(int id) {
        this.id = id;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public ModelOwner setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }
}
