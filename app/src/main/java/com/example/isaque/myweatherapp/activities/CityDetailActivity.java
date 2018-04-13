package com.example.isaque.myweatherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.example.isaque.myweatherapp.data.RetrievementServiceIntent;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;
import com.example.isaque.myweatherapp.view.CityDetailFragment;
import com.example.isaque.myweatherapp.R;

import static com.example.isaque.myweatherapp.utils.Constants.ACTION_FLAG;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_WEATHER_BY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.CITY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_UNKNOWN;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_RECEIVER;

/**
 * An activity representing a single City detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CityListActivity}.
 */
public class CityDetailActivity extends BaseActivity {

    private ResultReceiverCallBack mReceiver;
    private int cityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            setupFragment();
        }

        setupReceiver();
    }

    private void setupReceiver() {
        mReceiver = new ResultReceiverCallBack(new Handler());
        Intent intent = new Intent(this, RetrievementServiceIntent.class);
        intent.setAction(Constants.ACTION_5_DAY_FORECAST);
        intent.putExtra(CITY_ID, getIntent().getIntExtra(Constants.CITY_ID, -1));
        intent.putExtra(RESULT_RECEIVER, mReceiver);
        startService(intent);
        showLoading();
    }

    private void setupFragment(){
        Bundle arguments = new Bundle();
        CityDetailFragment fragment = new CityDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.city_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ResultReceiverCallBack<T> extends ResultReceiver {

        public ResultReceiverCallBack(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            hideLoading();
            super.onReceiveResult(resultCode, resultData);
            try {
                if (resultCode == Constants.RESULT_OK) {
                    if (mReceiver != null ) {
                        switch (resultData.getString(ACTION_FLAG)) {
                            case ACTION_WEATHER_BY_ID:
//                                mCitiesList.add(((WeatherData)
//                                        resultData.getSerializable(ACTION_WEATHER_BY_ID)));
//                                updateAdapter();
                                break;
                        }
                    }
                } else {
                    showError(resultData.getString(ERROR));
                }
            } catch (Exception e){
                showError(ERROR_UNKNOWN);
                e.printStackTrace();
            }
        }
    }
}
