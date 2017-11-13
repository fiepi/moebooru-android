package com.fiepi.moebooru;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fiepi.moebooru.adapter.PostsAdapter;
import com.fiepi.moebooru.api.PostsAPI;
import com.fiepi.moebooru.bean.PostBean;

import java.util.ArrayList;
import java.util.List;

public class MoebooruActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<PostBean> postBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moebooru);
        initSwipeRefreshLayout();
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()){
            PullPosts pullPosts = new PullPosts();
            pullPosts.execute();
        }
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorMoebooru, R.color.colorPrimary, R.color.colorFavorite);
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_posts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initData(){
        layoutManager = new GridLayoutManager(this, 3, OrientationHelper.VERTICAL,false);
        adapter = new PostsAdapter(getData());
    }
    private List<PostBean> getData(){
        return postBeans;
    }

    private class PullPosts extends AsyncTask<Integer, Void, List<PostBean>>{

        @Override
        protected List<PostBean> doInBackground(Integer... integers) {
            PostsAPI postsAPI = new PostsAPI();

            return postsAPI.getPosts(20,1,"matthew_kyrielite","https://konachan.com/post.json");
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

        @Override
        protected void onPostExecute(List<PostBean> postsList) {
            super.onPostExecute(postsList);
            swipeRefreshLayout.setRefreshing(false);
            if (!postsList.isEmpty()){
                postBeans = postsList;
                initData();
                initView();
            }
        }
    }
}
