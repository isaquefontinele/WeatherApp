package com.example.isaque.myweatherapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.example.isaque.myweatherapp.R;
import com.example.isaque.myweatherapp.data.SharedPrefs;
import com.example.isaque.myweatherapp.utils.Constants;

import butterknife.BindView;

public class Settings extends AppCompatActivity {

    RadioButton radioCelsius;
    RadioButton radioFahr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        radioCelsius = findViewById(R.id.radio_celsius);
        radioFahr = findViewById(R.id.radio_fahr);
        setupRadioButtons();
    }

    private void setupRadioButtons() {
        final SharedPrefs prefs = new SharedPrefs(this);
        if (prefs.getDefaultUnit().equals(Constants.METRIC)) {
            radioCelsius.setChecked(true);
        } else {
            radioFahr.setChecked(true);
        }

        radioCelsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFahr.setChecked(false);
                prefs.saveDefaultMetric();
            }
        });
        radioFahr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioCelsius.setChecked(false);
                prefs.saveDefaultImperial();
            }
        });
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
}
