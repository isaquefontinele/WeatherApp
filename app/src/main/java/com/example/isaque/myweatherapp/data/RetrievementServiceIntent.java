package com.example.isaque.myweatherapp.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;

import static com.example.isaque.myweatherapp.utils.Constants.ACTION_FLAG;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_WEATHER_BY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_GETTING_DATA;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_FAIL;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_OK;

public class RetrievementServiceIntent extends IntentService {

    public RetrievementServiceIntent() {
        super("RetrievementServiceIntent");
    }

    ResultReceiver resultReceiver;
    String action;
    int cityId;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        resultReceiver = (ResultReceiver) intent.getExtras().get(Constants.RESULT_RECEIVER);
        action = intent.getAction();
        cityId = intent.getIntExtra(Constants.CITY_ID, -1);

        final ApiCall api = new ApiCall();
        final Bundle bundle = new Bundle();
        WeatherData weatherData;

        try{
            switch (action){
                case ACTION_WEATHER_BY_ID:
                    weatherData = api.getWeatherById(cityId);
                    bundle.putSerializable(ACTION_WEATHER_BY_ID, weatherData);
                    bundle.putString(ACTION_FLAG, ACTION_WEATHER_BY_ID);
                    resultReceiver.send(RESULT_OK, bundle);
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
