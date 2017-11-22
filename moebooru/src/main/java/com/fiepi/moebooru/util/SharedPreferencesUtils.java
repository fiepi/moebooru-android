package com.fiepi.moebooru.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;

/**
 * Created by fiepi on 11/22/17.
 */

public class SharedPreferencesUtils {

    private final static String TAG = SharedPreferencesUtils.class.getSimpleName();

    public SharedPreferencesUtils(){

    }

    public void saveString(String namePref, String key, String valus){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,valus);
        editor.commit();
    }

    public void saveBoolean(String namePref, String key, Boolean valus){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, valus);
        editor.commit();
    }

    public String getStringValus(String namePref, String key){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        String string = "null";
        string = sharedPreferences.getString(key, "null");
        Log.i(TAG, sharedPreferences.getString(key, "null"));
        return string;
    }
}
