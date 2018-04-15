package com.example.isaque.myweatherapp.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.model.ForecastData;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForecastViewPagerAdapter extends PagerAdapter {

    List<WeatherData> weatherDataList;
    ForecastData forecastData;
    Context context;

    public ForecastViewPagerAdapter(Context context, ForecastData forecastData) {
        this.context = context;
        this.forecastData = forecastData;
        this.weatherDataList = forecastData.getWeatherDataList();
    }

    @Override
    public float getPageWidth(int position) {
        return 0.55f;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_content, null);

        try {
//            final TextView cityName = view.findViewById(R.id.city_name);
            final TextView date = view.findViewById(R.id.date);
            final TextView weatherStatus = view.findViewById(R.id.weather_status);
            final TextView temperature = view.findViewById(R.id.temp_min_max);
            final ImageView iconWeather = view.findViewById(R.id.icon_weather);

            date.setText(Utils.getDayOfWeek(forecastData.getWeatherDataList().get(position).getDate()));
//            cityName.setText(forecastData.getCity().getName());
            weatherStatus.setText(weatherDataList.get(position).getWeather().get(0).getMain());
            temperature.setText(Utils.getFormattedTemp(context, weatherDataList.get(position)));
            Picasso.with(context).
                    load(Utils.getIconLink(weatherDataList.get(position).getWeather().get(0).getIcon())).
                    into(iconWeather);

        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return weatherDataList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
