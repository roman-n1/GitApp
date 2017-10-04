package com.yarullin.roman.gitapp.persistence.base;

import android.support.annotation.Nullable;

public interface SharedPrefs {

    void putToken(@Nullable String accessToken);

    String getToken();

    void clearToken();
}
