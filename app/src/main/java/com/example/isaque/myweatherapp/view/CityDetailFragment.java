package com.example.isaque.myweatherapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.model.ForecastData;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;
import com.example.isaque.myweatherapp.utils.Utils;
import com.example.isaque.myweatherapp.view.adapters.ForecastViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityDetailFragment extends Fragment {

    @BindView(R.id.view_pager_day_1) ViewPager viewPager1;
    @BindView(R.id.view_pager_day_2) ViewPager viewPager2;
    @BindView(R.id.view_pager_day_3) ViewPager viewPager3;
    @BindView(R.id.view_pager_day_4) ViewPager viewPager4;
    @BindView(R.id.view_pager_day_5) ViewPager viewPager5;
    @BindView(R.id.week_day_1) TextView day1;
    @BindView(R.id.week_day_2) TextView day2;
    @BindView(R.id.week_day_3) TextView day3;
    @BindView(R.id.week_day_4) TextView day4;
    @BindView(R.id.week_day_5) TextView day5;
    private ForecastData forecastData;
    private Map<String, List<WeatherData>> weatherDataMap;
    private List<String> weekDays = new ArrayList<>();

    public CityDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        forecastData = (ForecastData) getArguments().getSerializable(Constants.FORECAST_DATA);
        filterForecastByDay(forecastData.getWeatherDataList());

        setToolbarTitle();
    }

    private void setToolbarTitle() {
        Activity activity = this.getActivity();
        TextView toolbarTitle = activity.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(forecastData.getCity().getName());
    }

    private void filterForecastByDay(List<WeatherData> weatherList) {
        weatherDataMap = new HashMap<>();
        String dayOfWeek;

        for (WeatherData weatherData : weatherList) {
            dayOfWeek = Utils.getDayOfWeek(weatherData.getDate());
            if (!weatherDataMap.containsKey(dayOfWeek)) {
                weatherDataMap.put(dayOfWeek, new ArrayList<WeatherData>());
                weekDays.add(dayOfWeek);
            }
            weatherDataMap.get(dayOfWeek).add(weatherData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (forecastData != null) {
            setupViewPagers();
        }
        return rootView;
    }

    private void setupViewPagers() {
        ViewPager[] viewPagers = {viewPager1, viewPager2, viewPager3, viewPager4, viewPager5};
        TextView[] titleDays = {day1, day2, day3, day4, day5};

        List<WeatherData> weatherDataList;
        for (int i = 0; i < 5; i++) {
            weatherDataList = weatherDataMap.get(weekDays.get(i));
            forecastData.setList(weatherDataList);
            titleDays[i].setText(weekDays.get(i));
            viewPagers[i].setPageMargin(getResources().getDimensionPixelOffset(R.dimen.view_pager_margin));
            viewPagers[i].setAdapter(new ForecastViewPagerAdapter(getContext(), forecastData));
        }
    }
}
