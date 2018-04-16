package com.example.isaque.myweatherapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.isaque.myweatherapp.activities.CityListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<CityListActivity> mActivitRule =
            new ActivityTestRule<>(CityListActivity.class);

    @Before
    public void waitService() throws InterruptedException {
        Thread.sleep(1500);
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.isaque.myweatherapp", appContext.getPackageName());
    }

    @Test
    public void openSettings(){

    }

//    @Test
//    public void teste(){
//
//    }
//    @Test
//    public void teste(){
//
//    }
//    @Test
//    public void teste(){
//
//    }
//    @Test
//    public void teste(){
//
//    }
//    @Test
//    public void teste(){
//
//    }

}

