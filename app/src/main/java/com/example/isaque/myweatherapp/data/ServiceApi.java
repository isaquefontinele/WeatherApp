package com.example.isaque.myweatherapp.data;

import com.example.isaque.myweatherapp.model.ForecastData;
import com.example.isaque.myweatherapp.model.WeatherData;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ServiceApi {

    String API_KEY = "f60a061134bee9cda9bcb889fae4ffe4";
    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    String BASE_URL_IMAGES = "http://openweathermap.org/img/w/";

    String WEATHER_URL = "weather?";
    String FORECAST_URL = "forecast?";

    @GET(WEATHER_URL)
    Call<WeatherData> getWeatherById(@Query("id") String id,
                                     @Query("appid") String appid,
                                     @Query("units") String units);

    @GET(FORECAST_URL)
    Call<ForecastData> get5dayForecastById(@Query("id") String id,
                                           @Query("appid") String appid,
                                           @Query("units") String units);
}
