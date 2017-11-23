package com.fiepi.moebooru.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by fiepi on 11/23/17.
 */

public class ClipboardUtils {

    public ClipboardUtils(){

    }

    /**
     * 文本复制
     * @param content
     */
    public void copy(String content, Context context)
    {
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("tag", content);
        cmb.setPrimaryClip(clip);
    }
    /**
     * 文本粘贴
     * @param context
     * @return
     */
    public String paste(Context context)
    {
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = cmb.getPrimaryClip();
        return clip.toString();
    }
}
