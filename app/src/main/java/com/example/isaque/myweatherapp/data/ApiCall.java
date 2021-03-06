package com.example.isaque.myweatherapp.data;

import com.example.isaque.myweatherapp.model.ForecastData;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static com.example.isaque.myweatherapp.data.ServiceApi.API_KEY;

public class ApiCall {

    private ServiceApi serviceApi;

    ApiCall(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServiceApi.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        this.serviceApi = retrofit.create(ServiceApi.class);
    }

    public WeatherData getWeatherById(int idCity, String unit) throws IOException {
        return serviceApi.getWeatherById(String.valueOf(idCity), API_KEY, unit).execute().body();
    }

    public ForecastData getForecastById(String cityName, String unit) throws IOException {
        return serviceApi.get5dayForecastById(cityName, API_KEY, unit).execute().body();
    }

    public WeatherData getWeatherByName(String cityName, String unit) throws IOException {
        return serviceApi.getWeatherByName(cityName, API_KEY, unit).execute().body();
    }

}
