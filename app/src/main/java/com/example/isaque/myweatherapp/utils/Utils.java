package com.example.isaque.myweatherapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.isaque.myweatherapp.activities.CityListActivity;
import com.example.isaque.myweatherapp.data.SharedPrefs;
import com.example.isaque.myweatherapp.model.WeatherData;

import static com.example.isaque.myweatherapp.data.ServiceApi.BASE_URL_IMAGES;

public class Utils {

    public static String getIconLink(String path){
        return BASE_URL_IMAGES + path + ".png";
    }

    public static boolean isConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String getFormattedTemp(Context context, WeatherData weatherData) {
        final String minTemp = String.valueOf(weatherData.getMain().getTemp_min());
        final String maxTemp = String.valueOf(weatherData.getMain().getTemp_max());
        SharedPrefs prefs = new SharedPrefs(context);

        if (prefs.getDefaultUnit().equals(Constants.METRIC)) {
            return minTemp + "ºC / " + maxTemp + "ºC";
        } else {
            return minTemp + "ºF / " + maxTemp + "ºF";
        }

    }
}
