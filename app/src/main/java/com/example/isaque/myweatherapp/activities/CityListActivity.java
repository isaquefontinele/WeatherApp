package com.example.isaque.myweatherapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isaque.myweatherapp.data.RetrievementServiceIntent;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;
import com.example.isaque.myweatherapp.utils.Utils;
import com.example.isaque.myweatherapp.view.CityDetailFragment;
import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.dummy.DummyContent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static com.example.isaque.myweatherapp.utils.Constants.ACTION_FLAG;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_WEATHER_BY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.CITY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_UNKNOWN;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_RECEIVER;

public class CityListActivity extends BaseActivity {

    @BindView(R.id.city_list) RecyclerView recyclerView;
    private boolean mTwoPane;
    private ResultReceiverCallBack mReceiver;
    private List<WeatherData> mCitiesList;
    private SimpleItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.city_detail_container) != null) {
            mTwoPane = true;
        }


        setupRecyclerView();
        loadCities();
        setupServiceWeatherById(2172797);
    }

    private void loadCities() {
        int[] registeredCities = {707860, 519188, 1283240, 614371, 2922336};
        for (int i :registeredCities){
            setupServiceWeatherById(i);
        }
    }

    private void setupRecyclerView() {
        mCitiesList = new ArrayList<>();
        recyclerView = findViewById(R.id.city_list);
        mAdapter = new SimpleItemRecyclerViewAdapter(this, mCitiesList, mTwoPane);
        recyclerView.setAdapter(mAdapter);
    }

    private void setupServiceWeatherById(int cityId){
        mReceiver = new ResultReceiverCallBack(new Handler());
        Intent intent = new Intent(this, RetrievementServiceIntent.class);
        intent.setAction(ACTION_WEATHER_BY_ID);
        intent.putExtra(CITY_ID, cityId);
        intent.putExtra(RESULT_RECEIVER, mReceiver);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sortByName:
                Collections.sort(mCitiesList, compareByName);
                updateAdapter();
                return true;
            case R.id.sortByMinTemp:
                Collections.sort(mCitiesList, compareByMinTemp);
                updateAdapter();
                return true;
            case R.id.sortByMaxTemp:
                Collections.sort(mCitiesList, compareByMaxTemp);
                updateAdapter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAdapter(){
        mAdapter.setmCities(mCitiesList);
        mAdapter.notifyDataSetChanged();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CityListActivity mParentActivity;
        private List<WeatherData> mCities;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(CityListActivity parent, List<WeatherData> items,
                                      boolean twoPane) {
            mCities = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.city_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.cityName.setText(mCities.get(position).getName());
            holder.weatherStatus.setText(mCities.get(position).getWeather().get(0).getMain());
            Picasso.with(mParentActivity).
                    load(Utils.getIconLink(mCities.get(position).getWeather().get(0).getIcon())).
                    into(holder.iconWeather);

            holder.itemView.setTag(mCities.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(CITY_ID, mCities.get(position).getId());
                        CityDetailFragment fragment = new CityDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, CityDetailActivity.class);
                        intent.putExtra(CITY_ID, mCities.get(position).getId());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCities.size();
        }

        public void setmCities(List<WeatherData> mCities) {
            this.mCities = mCities;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView cityName;
            final TextView weatherStatus;
            final ImageView iconWeather;

            ViewHolder(View view) {
                super(view);
                cityName = view.findViewById(R.id.city_name);
                weatherStatus = view.findViewById(R.id.weather_status);
                iconWeather = view.findViewById(R.id.icon_weather);
            }
        }
    }

    public class ResultReceiverCallBack<T> extends ResultReceiver {

        public ResultReceiverCallBack(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            try {
                if (resultCode == Constants.RESULT_OK) {
                    if (mReceiver != null ) {
                        switch (resultData.getString(ACTION_FLAG)) {
                            case ACTION_WEATHER_BY_ID:
                                mCitiesList.add(((WeatherData)
                                        resultData.getSerializable(ACTION_WEATHER_BY_ID)));
                                Collections.sort(mCitiesList, compareByName);
                                updateAdapter();
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
