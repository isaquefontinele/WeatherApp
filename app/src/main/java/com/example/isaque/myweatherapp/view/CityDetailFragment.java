package com.example.isaque.myweatherapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.activities.CityDetailActivity;
import com.example.isaque.myweatherapp.activities.CityListActivity;
import com.example.isaque.myweatherapp.model.ForecastData;
import com.example.isaque.myweatherapp.utils.CarouselEffectTransformer;
import com.example.isaque.myweatherapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a single City detail screen.
 * This fragment is either contained in a {@link CityListActivity}
 * in two-pane mode (on tablets) or a {@link CityDetailActivity}
 * on handsets.
 */
public class CityDetailFragment extends Fragment {

    @BindView(R.id.view_pager_forecast)
    ViewPager viewPager;
    private ForecastData forecastData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forecastData = (ForecastData) getArguments().getSerializable(Constants.FORECAST_DATA);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(forecastData.getCity().getName());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (forecastData != null) {
//            ((TextView) rootView.findViewById(R.id.city_detail)).setText(forecastData.getWeatherDataList().get(0).getWeather().get(0).getMain());

            setupViewPager();
        }
        return rootView;
    }

    private void setupViewPager() {
        ForecastViewPagerAdapter viewPagerAdapter = new ForecastViewPagerAdapter(getContext(), forecastData);
//        viewPager.setClipChildren(false);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.text_margin));

//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageTransformer(false, new CarouselEffectTransformer(getContext()));
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();
    }
}
