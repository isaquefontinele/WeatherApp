package com.example.isaque.myweatherapp.view.adapters;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastViewPagerAdapter extends PagerAdapter {

    @BindView(R.id.date) TextView hour;
    @BindView(R.id.weather_status) TextView weatherStatus;
    @BindView(R.id.temperature) TextView temperature;
    @BindView(R.id.icon_weather) ImageView iconWeather;
    private List<WeatherData> weatherDataList;
    private ForecastData forecastData;
    private Context context;

    public ForecastViewPagerAdapter(Context context, ForecastData forecastData) {
        this.context = context;
        this.forecastData = forecastData;
        this.weatherDataList = forecastData.getWeatherDataList();
    }

    @Override
    public float getPageWidth(int position) {
        return 0.35f;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_content, null);
        ButterKnife.bind(this, view);

        try {
            hour.setText(Utils.getHour(forecastData.getWeatherDataList().get(position).getDate()));
            weatherStatus.setText(weatherDataList.get(position).getWeather().get(0).getMain());
            temperature.setText(Utils.getFormattedTemp(context, weatherDataList.get(position)));
            Picasso.with(context).
                    load(Utils.getIconLink(weatherDataList.get(position).getWeather().get(0).getIcon())).
                    into(iconWeather);

            container.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
