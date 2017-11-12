package com.fiepi.moebooru.api;

import android.os.AsyncTask;
import android.support.v4.app.SupportActivity.ExtraData;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.bean.TagBean;
import com.fiepi.moebooru.ui.MoebooruActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fiepi on 11/12/17.
 */

public class PostsAPI extends AsyncTask<SwipeRefreshLayout, Void, List<PostBean>> {

    private String url = null;
    private OkHttpClient client = new OkHttpClient();
    private List<RawPostBean> rawPostBeans = new ArrayList<>();
    private Gson gson = new Gson();

    SwipeRefreshLayout swipeRefreshLayout;

    public PostsAPI(){

    }

    @Override
    protected List<PostBean> doInBackground(SwipeRefreshLayout... swipeRefreshLayouts) {
        this.swipeRefreshLayout = swipeRefreshLayouts[0];
        return getPosts(10,1,"yuri","https://konachan.com/post.json");
    }

    @Override
    protected void onPostExecute(List<PostBean> postsList) {
        super.onPostExecute(postsList);
        Log.i("结果:", postsList.get(0).getFile_url());
        this.swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("AsyTask", "onPreExecute");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Log.d("AsyTask", "onProgressUpdate");
    }

    private List<PostBean> getPosts(Integer limit, Integer page, String tags, String url){
        this.url = url;
        RequestBody requestBody = new FormBody.Builder()
                .add("limit", String.valueOf(limit))
                .add("page", String.valueOf(page))
                .add("tags", tags)
                .build();

        return getPostBean(sendRequest(requestBody));
    }

    private Reader sendRequest(RequestBody requestBody){
        Response response = null;
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute();

            if (response.isSuccessful()){
                Log.i("sendRequest","response.isSuccessful");

                return response.body().charStream();
            }
            Log.i("sendRequest","response.isFailed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<PostBean> getPostBean(Reader reader){

        List<PostBean> postBeanList = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(reader).getAsJsonArray();
        for (JsonElement post : jsonArray){
            RawPostBean rawPostBean = gson.fromJson(post, RawPostBean.class);
            rawPostBeans.add(rawPostBean);
        }
//        Log.i("getPostBean",rawPostBeans.get(0).file_url);

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
            postBeanList.add(postBean);

        }
//        Log.i("postBeanList","长度："+postBeanList.size()+" 第一个File_url："+postBeanList.get(0).getFile_url());
        return postBeanList;
    }

}
