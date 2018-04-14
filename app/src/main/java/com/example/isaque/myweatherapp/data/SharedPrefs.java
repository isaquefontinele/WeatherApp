package com.example.isaque.myweatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.model.WeatherDataList;
import com.example.isaque.myweatherapp.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

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
        return preferences.getString(Constants.DEFAULT_UNIT, Constants.METRIC);
    }

    public void saveCitiesList(WeatherDataList weatherData) {
        Gson gson = new Gson();
        String json = gson.toJson(weatherData);
        editor.putString(Constants.PREFS_CITIES_LIST, json);
        editor.apply();
    }

    public WeatherDataList getCitiesList() {
        Gson gson = new Gson();
        String json = preferences.getString(Constants.PREFS_CITIES_LIST, "");
        if (json.isEmpty()) {
            return new WeatherDataList(new ArrayList<WeatherData>());
        } else {
            return gson.fromJson(json, WeatherDataList.class);
        }
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
