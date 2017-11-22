package com.fiepi.moebooru.api;

import android.util.Log;

import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.bean.TagBean;
import com.fiepi.moebooru.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by fiepi on 11/15/17.
 */

public class GetPost {
    private static final String TAG = GetPost.class.getSimpleName();
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_ACCEPT_INFO = "application/json";

    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_USER_AGENT_INFO = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";

    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_ACCEPT_ENCODING_INFO = "gzip, deflate, br";

    private static final String HEADER_CONNECTION = "Connection";
    private static final String HEADER_CONNECTION_INFO = "keep-alive";

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_TYPE_INFO = "application/json";

    private String mURL = null;
    private List<RawPostBean> mRawPostBeanList = new ArrayList<>();
    private List<PostBean> mPostBeanList = new ArrayList<>();
    private Gson mGson = new Gson();

    private HttpLoggingInterceptor logInterceptor;

    private OkHttpClient mClient;

    public GetPost(){
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

    public List<PostBean> getPosts(int limit, int page, String tags, String url){
        this.mURL = url;
        Log.i(TAG,"请求体: limit:" + limit + " page:" + page + " tags:" + tags + " url:" +url);
        RequestBody requestBody;
        if (tags == "null"){
            requestBody = new FormBody.Builder()
                    .add("limit", String.valueOf(limit))
                    .add("page", String.valueOf(page))
                    .build();
            Log.i(TAG,"请求体不带 Tag");
        }else {
            requestBody = new FormBody.Builder()
                    .add("limit", String.valueOf(limit))
                    .add("page", String.valueOf(page))
                    .add("tags", tags)
                    .build();
            Log.i(TAG,"请求体带 Tag");
        }
        Log.i(TAG, "完成请求体");
        mPostBeanList = getPostBean(getRawPostBean(sendRequest(requestBody)));
        if (mPostBeanList == null){
            Log.i(TAG,"mPostBeanList 结果为空");
            return null;
        }
        Log.i(TAG, "获得数据");
        return mPostBeanList;
    }

    private String sendRequest(RequestBody requestBody){
        Response response = null;
        Request request = new Request.Builder()
                .url(mURL)
                .post(requestBody)
                .addHeader(HEADER_USER_AGENT, HEADER_USER_AGENT_INFO)
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
            postBean.setSource(rawPostBean.source);
            postBean.setMd5(rawPostBean.md5);
            postBean.setScore(rawPostBean.score);
            postBean.setRating(rawPostBean.rating);
            postBean.setHas_children(rawPostBean.has_children);
            postBean.setParent_id(rawPostBean.parent_id);
            postBean.setStatus(rawPostBean.status);
            postBean.setWidth(rawPostBean.width);
            postBean.setHeight(rawPostBean.height);
            postBean.setFile_size(rawPostBean.file_size);
            postBean.setFile_url(rawPostBean.file_url);
            postBean.setPreview_url(rawPostBean.preview_url);
            postBean.setPreview_height(rawPostBean.actual_preview_height);
            postBean.setPreview_width(rawPostBean.actual_preview_width);
            postBean.setJpeg_url(rawPostBean.jpeg_url);
            postBean.setJpeg_height(rawPostBean.jpeg_height);
            postBean.setJpeg_width(rawPostBean.jpeg_width);
            postBean.setJpeg_file_size(rawPostBean.jpeg_file_size);
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
            new FileUtils().saveJson(string);
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
