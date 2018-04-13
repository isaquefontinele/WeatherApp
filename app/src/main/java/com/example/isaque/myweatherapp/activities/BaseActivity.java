package com.example.isaque.myweatherapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.model.WeatherData;

import java.util.Comparator;

import static com.example.isaque.myweatherapp.utils.Constants.ERROR_GETTING_DATA;
import static com.example.isaque.myweatherapp.utils.Constants.ERROR_UNKNOWN;
import static com.example.isaque.myweatherapp.utils.Constants.NETWORK_CONNECTION_ERROR;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        progress = new ProgressDialog(this);
    }

    void showLoading(){
        progress.setTitle(getString(R.string.loading));
        progress.setMessage(getString(R.string.wait_while_loading));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    void hideLoading(){
        progress.dismiss();
    }

    void showError(String error){
        switch (error){
            case NETWORK_CONNECTION_ERROR:
                Toast.makeText(this, getString(R.string.network_error),
                        Toast.LENGTH_LONG).show();
                break;
            case ERROR_GETTING_DATA:
                Toast.makeText(this, getString(R.string.error_getting_data),
                        Toast.LENGTH_LONG).show();
                break;
            case ERROR_UNKNOWN:
                Toast.makeText(this, getString(R.string.unknown_error),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }



    public Comparator<WeatherData> compareByName = new Comparator<WeatherData>() {
        @Override
        public int compare(WeatherData weatherData, WeatherData t1) {
            return weatherData.getName().compareTo(t1.getName());
        }
    };
    public Comparator<WeatherData> compareByMinTemp = new Comparator<WeatherData>() {
        @Override
        public int compare(WeatherData weatherData, WeatherData t1) {
            return Double.compare(weatherData.getMain().getTemp_min(), t1.getMain().getTemp_min());
        }
    };
    public Comparator<WeatherData> compareByMaxTemp = new Comparator<WeatherData>() {
        @Override
        public int compare(WeatherData weatherData, WeatherData t1) {
            return Double.compare(weatherData.getMain().getTemp_max(), t1.getMain().getTemp_max());
        }
    };
}
