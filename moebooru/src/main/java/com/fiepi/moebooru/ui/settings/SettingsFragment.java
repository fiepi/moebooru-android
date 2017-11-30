package com.fiepi.moebooru.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import moe.shizuku.preference.CheckBoxPreference;
import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceCategory;
import moe.shizuku.preference.PreferenceFragment;

import com.fiepi.moebooru.AppConfig;
import com.fiepi.moebooru.R;

/**
 * Created by fiepi on 11/30/17.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        getPreferenceManager().setDefaultPackages(new String[]{com.fiepi.moebooru.BuildConfig.APPLICATION_ID + ".ui.settings."});

        getPreferenceManager().setSharedPreferencesName("settings");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        setPreferencesFromResource(R.xml.settings, null);

        PreferenceCategory preferenceCategory = new PreferenceCategory(getContext());
        preferenceCategory.setTitle("Test add dynamically");
        getPreferenceScreen().addPreference(preferenceCategory);

        CheckBoxPreference checkBoxPreference = new CheckBoxPreference(getContext());
        checkBoxPreference.setTitle("Test");
        checkBoxPreference.setSummaryOn("ON");
        checkBoxPreference.setSummaryOff("OFF");
        getPreferenceScreen().addPreference(checkBoxPreference);

    }

    @Override
    public DividerDecoration onCreateItemDecoration() {
        return new CategoryDivideDividerDecoration();
        //return new DefaultDividerDecoration();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged " + key);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, getString(R.string.on_preference_change_toast_message, preference.getKey(), newValue.toString()));
        return true;
    }

}