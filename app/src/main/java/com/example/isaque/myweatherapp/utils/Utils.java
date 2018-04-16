package com.example.isaque.myweatherapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.activities.CityListActivity;
import com.example.isaque.myweatherapp.data.SharedPrefs;
import com.example.isaque.myweatherapp.model.WeatherData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.isaque.myweatherapp.data.ServiceApi.BASE_URL_IMAGES;

public class Utils {

    public static String getIconLink(String path){
        return BASE_URL_IMAGES + path + ".png";
    }

    public static boolean isConnected(Context context){
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String getFormattedMinMaxTemp(Context context, WeatherData weatherData) {
        final String minTemp = String.valueOf(weatherData.getMain().getTemp_min());
        final String maxTemp = String.valueOf(weatherData.getMain().getTemp_max());
        SharedPrefs prefs = new SharedPrefs(context);

        if (prefs.getDefaultUnit().equals(Constants.METRIC)) {
            return minTemp + "ºC / " + maxTemp + "ºC";
        } else {
            return minTemp + "ºF / " + maxTemp + "ºF";
        }
    }

    public static String getFormattedTemp(Context context, WeatherData weatherData) {
        final String temp = String.valueOf(weatherData.getMain().getTemp());
        SharedPrefs prefs = new SharedPrefs(context);

        if (prefs.getDefaultUnit().equals(Constants.METRIC)) {
            return temp + "ºC";
        } else {
            return temp + "ºF";
        }
    }

    public static String getFormattedWind(Context context, WeatherData.Wind wind) {
        final String speed = String.valueOf(round(wind.getSpeed()));
        final String deg = String.valueOf(round(wind.getDeg()));
        SharedPrefs prefs = new SharedPrefs(context);

        if (prefs.getDefaultUnit().equals(Constants.METRIC)) {
            return speed + "m/s - " + deg + "º";
        } else {
            return speed + "mph - " + deg + "º";
        }
    }

    public static String getFormattedHumidity(WeatherData weatherData) {
        return String.valueOf(weatherData.getMain().getHumidity()) + "%";
    }

    public static String getFormattedDate(Context context, Date date) {
        final SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE - dd");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM");
        final SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa");
        return sdf1.format(date) + context.getString(R.string.of) + sdf2.format(date) + " - " + sdf3.format(date);
    }

    public static String getDayOfWeek(Date date) {
        final SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE - dd/MM");
        return sdf1.format(date);
    }

    public static String getHour(Date date) {
        final SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
        return sdf1.format(date);
    }

    private static double round(double value) {
        final int places = 2;
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
