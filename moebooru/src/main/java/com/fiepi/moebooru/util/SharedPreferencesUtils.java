package com.fiepi.moebooru.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.Map;

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

    public String getStringValue(String namePref, String key){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        String string = "null";
        string = sharedPreferences.getString(key, "null");
//        Log.i(TAG, sharedPreferences.getString(key, "null"));
        return string;
    }

    public Integer getIntValue(String namePref, String key){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        Integer vulue = Integer.valueOf(sharedPreferences.getString(key, "0"));
        Log.i(TAG, "取 int："+vulue);
        return vulue;
    }

    public Map<String,?> getALL(String namePref){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }

    public void removeValue(String namePref, String nameKey){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(nameKey);
        editor.commit();
    }

    public String getSelectedTags(String namePref){
        Context appContext = new FileUtils().getContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(namePref, appContext.MODE_PRIVATE);
        Map<String, ?> allTag = sharedPreferences.getAll();
        String tags = "";
        for (Map.Entry<String, ?>  entry : allTag.entrySet()){
            if(entry.getValue().equals(true)){
                if (tags == ""){
                    tags = entry.getKey();
                }else {
                    tags = tags + "+" + entry.getKey();
                }
            }
        }
        return tags;
    }
}
