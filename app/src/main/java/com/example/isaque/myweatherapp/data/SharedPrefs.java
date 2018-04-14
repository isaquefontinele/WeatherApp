package com.example.isaque.myweatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.isaque.myweatherapp.utils.Constants;

public class SharedPrefs {

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        preferences = context.getSharedPreferences(Constants.SHARE_PREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveDefaultMetric() {
        editor.putString(Constants.DEFAULT_UNIT, Constants.METRIC).apply();
    }

    public void saveDefaultImperial() {
        editor.putString(Constants.DEFAULT_UNIT, Constants.IMPERIAL).apply();
    }

    public String getDefaultUnit() {
        return preferences.getString(Constants.DEFAULT_UNIT, Constants.IMPERIAL);
    }
}
