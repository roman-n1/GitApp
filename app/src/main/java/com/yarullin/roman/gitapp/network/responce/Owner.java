package com.yarullin.roman.gitapp.network.responce;

import com.google.gson.annotations.SerializedName;

public class Owner {
    @SerializedName("login") private String login;
    @SerializedName("id") private int id;
    @SerializedName("avatar_url") private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
