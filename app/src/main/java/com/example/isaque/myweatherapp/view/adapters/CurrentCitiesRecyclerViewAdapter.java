package com.example.isaque.myweatherapp.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.data.SharedPrefs;
import com.example.isaque.myweatherapp.model.WeatherData;

import java.util.List;

public class CurrentCitiesRecyclerViewAdapter extends RecyclerView.Adapter<CurrentCitiesRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<WeatherData> mCities;
    private SharedPrefs prefs;

    public CurrentCitiesRecyclerViewAdapter(Context context, List<WeatherData> items) {
        this.context = context;
        this.mCities = items;
        this.prefs = new SharedPrefs(context);
    }

    public void setmCities(List<WeatherData> mCities) {
        this.mCities = mCities;
    }

    public void addCity(WeatherData newCity) {
        mCities.add(newCity);
    }

    @Override
    public CurrentCitiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_current_cities_list, parent, false);
        return new CurrentCitiesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrentCitiesRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.cityName.setText(mCities.get(position).getName());

        holder.trashBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.removeCity(mCities.get(position));
                setmCities(prefs.getCitiesList().getWeatherDataList());
                notifyDataSetChanged();
                Toast.makeText(context, context.getString(R.string.city_removed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView cityName;
        final ImageView trashBin;

        public ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            trashBin = itemView.findViewById(R.id.bt_delete_city);
        }
    }
}


