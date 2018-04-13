package com.example.isaque.myweatherapp.model;

import java.io.Serializable;
import java.util.List;

public class WeatherData implements Serializable {

    private Coordinates coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int id;
    private String name;
    private int cod;


    private class Coordinates{
        private double lon;
        private double lat;
    }

    private class Weather{
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    private class Main{
        private double temp;
        private int pressure;
        private int humidity;
        private double temp_min;
        private double temp_max;
    }

    private class Wind{
        private double speed;
        private int deg;
    }

    private class Clouds{
        private int all;
    }

    private class Sys{
        private int type;
        private int id;
        private double message;
        private String country;
        private String sunrise;
        private String sunset;
    }
}
