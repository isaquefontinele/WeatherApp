package com.example.isaque.myweatherapp.model;

import java.io.Serializable;
import java.util.List;

public class WeatherDataList implements Serializable {

    private List<WeatherData> weatherDataList;

    public WeatherDataList(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }
}
