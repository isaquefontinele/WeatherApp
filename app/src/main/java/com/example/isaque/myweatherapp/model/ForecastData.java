package com.example.isaque.myweatherapp.model;

import java.io.Serializable;
import java.util.List;

public class ForecastData implements Serializable {

    private String cod;
    private double message;
    private int cnt;
    private List<WeatherData> list;
    private City city;


    public class City {
        private int id;
        private String name;
        private WeatherData.Coordinates coord;
        private String country;

        public String getName() {
            return name;
        }
    }

    // Gets and Sets
    public String getCod() {
        return cod;
    }

    public double getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public List<WeatherData> getWeatherDataList() {
        return list;
    }

    public void setList(List<WeatherData> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }
}
