package com.fiepi.moebooru.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.fiepi.moebooru.bean.TagBean;

import java.io.File;
import java.util.List;

/**
 * Created by fiepi on 11/17/17.
 */

public class ImgDownloadUtils {

    private Context mContext;
    private String mUrl;
    private String mSite;
    private List<TagBean> mTags;
    private long mID;

    public ImgDownloadUtils(String url, List<TagBean> tagBeans, long id, String site, Context context){
        mUrl = url;
        mTags = tagBeans;
        mID = id;
        mSite = site;
        mContext = context;
    }

    public void toDownload(){
        downloadImg(getFileName());
    }

    private String getFileName() {
        String fileName;
        fileName = mUrl.substring(mUrl.length()-4, mUrl.length());
        String tags = null;
        for (int i = 0; i < mTags.size(); i++){
            if (tags == null){
                tags = mTags.get(i).getName();
            }else {
                tags = tags + " " + mTags.get(i).getName();
            }
        }
        fileName =mSite + "_" + mID + " " + tags + fileName;
        fileName = fileName.replaceAll("/", "_");
        return fileName;
    }

    private void downloadImg(String fileName){

        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };

        File tmpDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/Moebooru/" + mSite);
        if (!tmpDir.exists()){
            tmpDir.mkdir();
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/Moebooru/" + mSite + "/" + fileName);
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(mContext,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }else {
                DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mUrl));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
                request.setDestinationUri(Uri.fromFile(file));
                request.setTitle(mSite + " " + mID);
                request.setDescription("Download from " + mSite);
                downloadManager.enqueue(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
