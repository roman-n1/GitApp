package com.yarullin.roman.gitapp.persistence;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.yarullin.roman.gitapp.persistence.base.SharedPrefs;

public class SharedPrefsImpl implements SharedPrefs {
    private static final String KEY_ACCESS_TOKEN = "SharedPrefsImpl.KEY_ACCESS_TOKEN";
    private final SharedPreferences prefs;
//    private final SharedPreferences.Editor editor;

    public SharedPrefsImpl(SharedPreferences prefs) {
        this.prefs = prefs;
//        this.editor = prefs.edit();
    }

    @Override
    public void putToken(@Nullable String accessToken) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void clearToken() {
        prefs.edit().remove(KEY_ACCESS_TOKEN).apply();
    }
}
