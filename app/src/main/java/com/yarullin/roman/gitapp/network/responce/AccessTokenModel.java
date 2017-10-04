package com.yarullin.roman.gitapp.network.responce;

import com.google.gson.annotations.SerializedName;

public class AccessTokenModel {
    @SerializedName("id") private long id;
    @SerializedName("token") private String token;
    @SerializedName("hashed_token") private String hashedToken;
    @SerializedName("access_token") private String accessToken;
    @SerializedName("token_type") private String tokenType;

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getHashedToken() {
        return hashedToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
