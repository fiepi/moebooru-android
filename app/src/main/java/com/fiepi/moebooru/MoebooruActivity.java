package com.fiepi.moebooru;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moebooru);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION );
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        initToolbar();
        initSwipeRefreshLayout();
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()){
            PullPosts pullPosts = new PullPosts();
            pullPosts.execute();
        }
    }

    //设置右上角的填充菜单
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.tb_top);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search){
                    Toast.makeText(MoebooruActivity.this, "Search", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
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
        //网格
        layoutManager = new GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false);
        //瀑布流
        //layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        adapter = new PostsAdapter(getData());
    }
    private List<PostBean> getData(){
        return postBeans;
    }

    private class PullPosts extends AsyncTask<Integer, Void, List<PostBean>>{

        @Override
        protected List<PostBean> doInBackground(Integer... integers) {
            PostsAPI postsAPI = new PostsAPI();

            return postsAPI.getPosts(30,1,"matthew_kyrielite","https://konachan.com/post.json");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
