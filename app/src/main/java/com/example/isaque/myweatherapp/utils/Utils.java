package com.example.isaque.myweatherapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.example.isaque.myweatherapp.data.ServiceApi.BASE_URL_IMAGES;

public class Utils {

    public static String getIconLink(String path){
        return BASE_URL_IMAGES + path + ".png";
    }

    public static boolean isConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
