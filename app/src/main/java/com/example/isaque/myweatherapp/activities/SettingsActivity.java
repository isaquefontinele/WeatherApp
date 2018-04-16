package com.example.isaque.myweatherapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.data.RetrievementServiceIntent;
import com.example.isaque.myweatherapp.data.SharedPrefs;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;
import com.example.isaque.myweatherapp.view.adapters.CurrentCitiesRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.isaque.myweatherapp.utils.Constants.ACTION_FLAG;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_WEATHER_BY_NAME;
import static com.example.isaque.myweatherapp.utils.Constants.CITY_NAME;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_CITY_NOT_FOUND;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_UNKNOWN;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_RECEIVER;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.radio_metric) RadioButton radioMetric;
    @BindView(R.id.radio_imperial) RadioButton radioImperial;
    @BindView(R.id.search_cities_bar) SearchView searchView;
    @BindView(R.id.search_result) TextView searchResult;
    @BindView(R.id.bt_add_city) ImageButton buttomAddCity;
    @BindView(R.id.result_line) LinearLayout resultLine;
    @BindView(R.id.recycler_current_cities) RecyclerView recyclerViewCurrentCities;
    private ResultReceiverCallBack mReceiver;
    private WeatherData searchedCity;
    private SharedPrefs prefs;
    private Context mContext;
    private List<WeatherData> mCitiesList;
    private CurrentCitiesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        ButterKnife.bind(this, this);

        prefs = new SharedPrefs(this);
        setupToolbar();
        setupSearchView();
        setupRecyclerView();
        setupRadioButtons();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRadioButtons() {
        final SharedPrefs prefs = new SharedPrefs(this);
        recyclerViewCurrentCities.requestFocus();
        if (prefs.getDefaultUnit().equals(Constants.METRIC)) {
            radioMetric.setChecked(true);
        } else {
            radioImperial.setChecked(true);
        }

        radioMetric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioImperial.setChecked(false);
                prefs.saveDefaultMetric();
            }
        });
        radioImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMetric.setChecked(false);
                prefs.saveDefaultImperial();
            }
        });
    }

    private void setupRecyclerView() {
        prefs = new SharedPrefs(this);
        mCitiesList = prefs.getCitiesList().getWeatherDataList();
        mAdapter = new CurrentCitiesRecyclerViewAdapter(this, mCitiesList);
        recyclerViewCurrentCities.setAdapter(mAdapter);
    }

    private void setupSearchView() {
        buttomAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchedCity != null) {
                    addNewCity();
                }
            }
        });

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.isEmpty()) {
                    resultLine.setVisibility(View.GONE);
                } else {
                    resultLine.setVisibility(View.VISIBLE);
                    searchCityByName(query);
                }
                return false;
            }
        });
        searchView.setFocusableInTouchMode(true);
    }

    private void addNewCity() {
        if (prefs.getCurrentCitiesNameList().contains(searchedCity.getName())) {
            Toast.makeText(mContext, getString(R.string.city_already_added), Toast.LENGTH_SHORT).show();
        } else {
            prefs.addNewCity(searchedCity);
            Toast.makeText(mContext, getString(R.string.new_city_added), Toast.LENGTH_SHORT).show();
            mAdapter.addCity(searchedCity);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void searchCityByName(String query) {
        mReceiver = new ResultReceiverCallBack(new Handler());
        Intent intent = new Intent(this, RetrievementServiceIntent.class);
        intent.setAction(ACTION_WEATHER_BY_NAME);
        intent.putExtra(CITY_NAME, query);
        intent.putExtra(RESULT_RECEIVER, mReceiver);
        startService(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, CityListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpTo(this, new Intent(this, CityListActivity.class));
    }

    public class ResultReceiverCallBack<T> extends ResultReceiver {
        public ResultReceiverCallBack(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            try {
                if (resultCode == Constants.RESULT_OK) {
                    if (mReceiver != null) {
                        switch (resultData.getString(ACTION_FLAG)) {
                            case ACTION_WEATHER_BY_NAME:
                                searchedCity = (WeatherData) resultData.getSerializable(ACTION_WEATHER_BY_NAME);
                                searchResult.setText(String.format("%s, %s", searchedCity.getName(), searchedCity.getSys().getCountry()));
                                buttomAddCity.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                } else {
                    if (resultData.getString(ERROR).equals(ERROR_CITY_NOT_FOUND)) {
                        searchResult.setText(getString(R.string.no_city_found));
                        buttomAddCity.setVisibility(View.GONE);
                        searchedCity = null;
                    } else {
                        showError(resultData.getString(ERROR));
                    }
                }
            } catch (Exception e) {
                showError(ERROR_UNKNOWN);
                e.printStackTrace();
            }
        }
    }
}
