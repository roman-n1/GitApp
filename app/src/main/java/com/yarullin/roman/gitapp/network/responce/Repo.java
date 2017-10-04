package com.yarullin.roman.gitapp.network.responce;

import com.google.gson.annotations.SerializedName;

public class Repo {
    @SerializedName("id") private int id;
    @SerializedName("name") private String name;
    @SerializedName("full_name") private String fullName;
    @SerializedName("description") private String description;
    @SerializedName("stargazers_count") private int stargazersCount;
    @SerializedName("forks_count") private int forksCount;
    @SerializedName("owner") private Owner owner;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public Owner getOwner() {
        return owner;
    }
}
