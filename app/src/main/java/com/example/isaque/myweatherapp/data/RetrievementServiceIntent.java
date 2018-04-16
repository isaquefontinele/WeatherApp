package com.example.isaque.myweatherapp.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.example.isaque.myweatherapp.model.ForecastData;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;

import static com.example.isaque.myweatherapp.utils.Constants.ACTION_5_DAY_FORECAST;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_FLAG;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_WEATHER_BY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_WEATHER_BY_NAME;
import static com.example.isaque.myweatherapp.utils.Constants.CITY_NAME;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_CITY_NOT_FOUND;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_GETTING_DATA;
import static com.example.isaque.myweatherapp.utils.Constants.LAST_CITY;
import static com.example.isaque.myweatherapp.utils.Constants.NETWORK_CONNECTION_ERROR;
import static com.example.isaque.myweatherapp.utils.Constants.NETWORK_CONNECTION_ERROR_LOAD_DATA;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_FAIL;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_OK;
import static com.example.isaque.myweatherapp.utils.Utils.isConnected;

public class RetrievementServiceIntent extends IntentService {

    public RetrievementServiceIntent() {
        super("RetrievementServiceIntent");
    }

    ResultReceiver resultReceiver;
    String action;
    int cityId;
    boolean lastCity;
    String cityName;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        resultReceiver = (ResultReceiver) intent.getExtras().get(Constants.RESULT_RECEIVER);
        action = intent.getAction();
        cityId = intent.getIntExtra(Constants.CITY_ID, -1);
        lastCity = intent.getBooleanExtra(LAST_CITY, false);
        cityName = intent.getStringExtra(CITY_NAME);

        final ApiCall api = new ApiCall();
        final Bundle bundle = new Bundle();
        bundle.putBoolean(LAST_CITY, lastCity);
        WeatherData weatherData;
        ForecastData forecastData;
        SharedPrefs prefs = new SharedPrefs(this);
        String defaultUnit = prefs.getDefaultUnit();

        if (!isConnected(getApplicationContext())) {
            if (prefs.getCitiesList().getWeatherDataList().size() == 0) {
                bundle.putSerializable(ERROR, NETWORK_CONNECTION_ERROR);
            } else {
                bundle.putSerializable(ERROR, NETWORK_CONNECTION_ERROR_LOAD_DATA);
            }
            resultReceiver.send(RESULT_FAIL, bundle);
            return;
        }

        try{
            switch (action){
                case ACTION_WEATHER_BY_ID:
                    weatherData = api.getWeatherById(cityId, defaultUnit);
                    bundle.putSerializable(ACTION_WEATHER_BY_ID, weatherData);
                    bundle.putString(ACTION_FLAG, ACTION_WEATHER_BY_ID);
                    resultReceiver.send(RESULT_OK, bundle);
                    break;
                case ACTION_5_DAY_FORECAST:
                    forecastData = api.getForecastById(cityName, defaultUnit);
                    bundle.putSerializable(ACTION_5_DAY_FORECAST, forecastData);
                    bundle.putString(ACTION_FLAG, ACTION_5_DAY_FORECAST);
                    resultReceiver.send(RESULT_OK, bundle);
                    break;
                case ACTION_WEATHER_BY_NAME:
                    weatherData = api.getWeatherByName(cityName);
                    if (weatherData == null) {
                        bundle.putString(ERROR, ERROR_CITY_NOT_FOUND);
                        resultReceiver.send(RESULT_FAIL, bundle);
                    } else {
                        bundle.putSerializable(ACTION_WEATHER_BY_NAME, weatherData);
                        bundle.putString(ACTION_FLAG, ACTION_WEATHER_BY_NAME);
                        resultReceiver.send(RESULT_OK, bundle);
                    }
                    break;
                default:
                    bundle.putString(ERROR, ERROR_GETTING_DATA);
                    resultReceiver.send(RESULT_FAIL, bundle);
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
            bundle.putString(ERROR, ERROR_GETTING_DATA);
            resultReceiver.send(RESULT_FAIL, bundle);
        }
    }
}
