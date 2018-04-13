package com.example.isaque.myweatherapp.data;

import com.example.isaque.myweatherapp.model.ForecastData;
import com.example.isaque.myweatherapp.model.WeatherData;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static com.example.isaque.myweatherapp.data.ServiceApi.API_KEY;

public class ApiCall {

    private ServiceApi serviceApi;

    public ApiCall(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServiceApi.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        this.serviceApi = retrofit.create(ServiceApi.class);
    }

    public WeatherData getWeatherById(int idCity) throws IOException {
        return serviceApi.getWeatherById(String.valueOf(idCity), API_KEY).execute().body();
    }

    public ForecastData getForecastById(int idCity) throws IOException {
        return serviceApi.get5dayForecastById(String.valueOf(idCity), API_KEY).execute().body();
    }
}
