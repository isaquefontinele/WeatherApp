package com.example.isaque.myweatherapp.model;

import java.io.Serializable;
import java.util.Date;
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


    public class Coordinates{
        private double lon;
        private double lat;
    }

    public class Weather{
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public class Main{
        private double temp;
        private int pressure;
        private int humidity;
        private double temp_min;
        private double temp_max;
    }

    public class Wind{
        private double speed;
        private int deg;
    }

    public class Clouds{
        private int all;
    }

    public class Sys{
        private int type;
        private int id;
        private double message;
        private String country;
        private String sunrise;
        private String sunset;
    }


    // Gets and Sets
    public Coordinates getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Date getDt() {
        Date date = new Date(dt);
        return date;
    }

    public Sys getSys() {
        return sys;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }
}
