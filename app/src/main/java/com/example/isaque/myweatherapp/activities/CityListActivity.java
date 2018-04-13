package com.example.isaque.myweatherapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isaque.myweatherapp.data.RetrievementServiceIntent;
import com.example.isaque.myweatherapp.model.WeatherData;
import com.example.isaque.myweatherapp.utils.Constants;
import com.example.isaque.myweatherapp.view.CityDetailFragment;
import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import static com.example.isaque.myweatherapp.utils.Constants.ACTION_FLAG;
import static com.example.isaque.myweatherapp.utils.Constants.ACTION_WEATHER_BY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.CITY_ID;
import static com.example.isaque.myweatherapp.utils.Constants.RESULT_RECEIVER;

public class CityListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private ResultReceiverCallBack mReceiver;
    private List<WeatherData> mCitiesList;
    private RecyclerView recyclerView;
    private SimpleItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.city_detail_container) != null) {
            mTwoPane = true;
        }


        setupRecyclerView();
        setupServiceWeatherById(2172797);
    }

    private void setupRecyclerView() {
        mCitiesList = new ArrayList<>();
        recyclerView = findViewById(R.id.city_list);
        mAdapter = new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane);
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

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CityListActivity mParentActivity;
        private List<DummyContent.DummyItem> mCities;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(CityDetailFragment.ARG_ITEM_ID, item.id);
                    CityDetailFragment fragment = new CityDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.city_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CityDetailActivity.class);
                    intent.putExtra(CityDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };



        SimpleItemRecyclerViewAdapter(CityListActivity parent,
                                      List<DummyContent.DummyItem> items,
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
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mCities.get(position).id);
            holder.mContentView.setText(mCities.get(position).content);

            holder.itemView.setTag(mCities.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mCities.size();
        }

        public void setmCities(List<DummyContent.DummyItem> mCities) {
            this.mCities = mCities;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.city_name);
                mContentView = (TextView) view.findViewById(R.id.weather_status);
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
                                break;
//                            mAdapter.setmCities(mCitiesList);
//                            mAdapter.notifyDataSetChanged();
//                        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mMoviesList));

                        }
                    }

                } else {
//                    mMoviesList.clear();
//                    mAdapter.notifyDataSetChanged();
//                    showError(resultData.getString(ERROR));
                }
            } catch (Exception e){
//                showError(ERROR_UNKNOWN);
                e.printStackTrace();
            }
        }
    }
}
