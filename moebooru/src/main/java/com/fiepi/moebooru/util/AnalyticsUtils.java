package com.fiepi.moebooru.util;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by fiepi on 11/25/17.
 */

public class AnalyticsUtils {
    public AnalyticsUtils(){

    }

    public void getAnalytics(String id, String name, String type, FirebaseAnalytics firebaseAnalytics){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}
