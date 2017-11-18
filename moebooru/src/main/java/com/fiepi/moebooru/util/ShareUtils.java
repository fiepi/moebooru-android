package com.fiepi.moebooru.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by fiepi on 11/18/17.
 */

public class ShareUtils {

    public ShareUtils(){

    }

    public void shareImage(Uri uri, Context context){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        context.startActivity(intent);
    }

    public void shareText(String string, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, string);
        intent.setType("text/plain");
        context.startActivity(intent);
    }
}
