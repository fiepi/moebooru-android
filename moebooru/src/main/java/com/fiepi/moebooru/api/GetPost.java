package com.fiepi.moebooru.api;

import android.util.Log;

import com.fiepi.moebooru.AppConfig;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.bean.TagBean;
import com.fiepi.moebooru.util.FileUtils;
import com.fiepi.moebooru.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by fiepi on 11/15/17.
 */

public class GetPost {
    private static final String TAG = GetPost.class.getSimpleName();
    private String mURL = null;
    private String mRating = "all";
    private List<RawPostBean> mRawPostBeanList = new ArrayList<>();
    private List<PostBean> mPostBeanList = new ArrayList<>();
    private Gson mGson = new Gson();
    private int mLimit = 40;
    private int mPage = 1;
    private String mType = "";

    private HttpLoggingInterceptor logInterceptor;
    private OkHttpClient mClient;

    public GetPost(){
        Integer i = new SharedPreferencesUtils().getIntValue("settings", "limit");
        if (i != 0){
            this.mLimit = i;
        }
        mRating = new SharedPreferencesUtils().getStringValue("settings", "rating");
        logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG+":Okhttp-Log", message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        mClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .build();
    }

    public List<PostBean> getPosts( int page, String tags, String url){
        this.mPage = page;
        if (tags.equals("null")){
            mType = "post";
            if (!(mRating.equals("all")||mRating.equals("null"))){
                this.mURL = url + "?page=" + page + "&limit=" + this.mLimit + "&tags=rating:" + mRating;
            }else {
                this.mURL = url + "?page=" + page + "&limit=" + this.mLimit;
            }
        }else {
            mType = "search";
            if (!(mRating.equals("all")||mRating.equals("null"))){
                this.mURL = url + "?page=" + page + "&limit=" + this.mLimit + "&tags=rating:" + mRating + "+" + tags;
            }else {
                this.mURL = url + "?page=" + page + "&limit=" + this.mLimit + "&tags=" + tags;
            }
        }
        Log.i(TAG, mURL);
        mPostBeanList = getPostBean(getRawPostBean(sendRequest()));
        if (mPostBeanList == null){
            Log.i(TAG,"mPostBeanList 结果为空");
            return null;
        }
        Log.i(TAG, "获得数据");
        return mPostBeanList;
    }

    private String sendRequest(){
        Response response = null;
        Request request = new Request.Builder()
                .url(mURL)
                .get()
                .addHeader(AppConfig.HEADER_USER_AGENT, AppConfig.HEADER_USER_AGENT_INFO)
                .build();
        try {
            response = mClient.newCall(request).execute();

            if (response.isSuccessful()){
                Log.i(TAG,"response.isSuccessful. code:" + response.code());
                String data = response.body().string();
//                Log.i(TAG, data);
                return data;
            }else {
                Log.i(TAG,"response.isFailed. code:" + response.code());
//                Log.i(TAG, response.body().charStream().toString());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"异常");
        return null;
    }

    public List<PostBean> getPostBean(List<RawPostBean> rawPostBeans){

        if (rawPostBeans == null){
            Log.i(TAG,"rawPostBeans 为空");
            return null;
        }
        for (RawPostBean rawPostBean : rawPostBeans){
            PostBean postBean = new PostBean();
            postBean.setId(rawPostBean.id);
            postBean.setCreated_at(rawPostBean.created_at * 1000);
            postBean.setCreator_id(rawPostBean.creator_id);
            postBean.setAuthor(rawPostBean.author);
            if (!rawPostBean.source.isEmpty()){
                postBean.setSource(rawPostBean.source);
            }else {
                Log.i(TAG, "rawPostBean.source.isEmpty()");
            }
            postBean.setMd5(rawPostBean.md5);
            postBean.setScore(rawPostBean.score);
            postBean.setRating(rawPostBean.rating);
            postBean.setHas_children(rawPostBean.has_children);
            postBean.setParent_id(rawPostBean.parent_id);
            postBean.setStatus(rawPostBean.status);
            postBean.setWidth(rawPostBean.width);
            postBean.setHeight(rawPostBean.height);
            postBean.setFile_size(rawPostBean.file_size);
            if (!rawPostBean.file_url.isEmpty()){
                postBean.setFile_url(rawPostBean.file_url);
            }else {
                Log.i(TAG,"rawPostBean.file_url");
            }
            postBean.setPreview_url(rawPostBean.preview_url);
            postBean.setPreview_height(rawPostBean.actual_preview_height);
            postBean.setPreview_width(rawPostBean.actual_preview_width);
            if (!rawPostBean.jpeg_url.isEmpty()){
                postBean.setJpeg_url(rawPostBean.jpeg_url);
                postBean.setJpeg_height(rawPostBean.jpeg_height);
                postBean.setJpeg_width(rawPostBean.jpeg_width);
                postBean.setJpeg_file_size(rawPostBean.jpeg_file_size);
            }else {
                Log.i(TAG, "jpeg is null");
            }
            postBean.setSample_file_size(rawPostBean.sample_file_size);
            postBean.setSample_url(rawPostBean.sample_url);
            postBean.setSample_height(rawPostBean.sample_height);
            postBean.setSample_width(rawPostBean.sample_width);

            List tagsList = new ArrayList();
            for (String tag : rawPostBean.tags.split(" ")){
                TagBean tagBean = new TagBean();
                tagBean.setName(tag);
                tagsList.add(tagBean);
            }
            postBean.setTags(tagsList);

            mPostBeanList.add(postBean);
        }

        return mPostBeanList;

    }

    private List<RawPostBean> getRawPostBean(String data){
        if (data == null){
            Log.i(TAG,"string 为空");
            return null;
        }
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(data).getAsJsonArray();
        String string = jsonArray.toString();
        if (!jsonArray.isJsonNull()){
            //保存 json 供下次打开时读取缓存中的图片
            //只在刷新时保存
            if (this.mPage == 1 && mType.equals("post")){
                Log.i(TAG, mType);
                new FileUtils().saveJson(string);
            }
            Log.i(TAG, mType);
            //转换为 List<RawPostBean>
            for (JsonElement post : jsonArray){
                RawPostBean rawPostBean = mGson.fromJson(post, RawPostBean.class);
                mRawPostBeanList.add(rawPostBean);
            }

//        Log.i("TAGS", String.valueOf(postBeanList.get(0).getTags().size()));
            return mRawPostBeanList;
        }
        Log.i("getPostBean","结果为空");
        return null;
    }
}
