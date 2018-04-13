package com.example.isaque.myweatherapp.data;

import com.example.isaque.myweatherapp.model.WeatherData;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ServiceApi {

    String API_KEY = "f60a061134bee9cda9bcb889fae4ffe4";
    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    String WEATHER_URL = "weather?";

    @GET(WEATHER_URL)
    Call<WeatherData> getWeatherById(@Query("id") String id,
                                     @Query("appid") String appid);


}
