package com.fiepi.moebooru.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.api.PostsAPI;

public class MoebooruActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moebooru);
        initSwipeRefreshLayout();
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()){
            PostsAPI postsAPI = new PostsAPI();
            postsAPI.execute(swipeRefreshLayout);
        }
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorMoebooru, R.color.colorAccent, R.color.colorFavorite);
    }
}
