package com.fiepi.moebooru.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.api.GetPost;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.ui.adapter.PostViewAdapter;
import com.fiepi.moebooru.ui.listener.OnRcvScrollListener;
import com.fiepi.moebooru.ui.listener.PostItemClickListener;
import com.fiepi.moebooru.util.AnalyticsUtils;
import com.fiepi.moebooru.util.SharedPreferencesUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Map;

import static com.fiepi.moebooru.AppConfig.*;

public class PostSearchActivity extends AppCompatActivity implements PostItemClickListener {

    private static final String TAG = PostSearchActivity.class.getSimpleName();

    private static final int SPAN_COUNT = 3;

    private static final String ARG_POST_ITEM_POS = "ARG_POST_ITEM_POS";
    private static final String ARG_POST_TYPE = "ARG_POST_TYPE";

    private Integer mPAGE = 1;
    private String mTAGS = "null";
    private String mURL = "null";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PostItemClickListener mListener = this;
    private PostViewAdapter mAdapter;

    private PullPost mPullPostTask = null;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_post_search);
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = findViewById(R.id.scroll_view_search);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new PostViewAdapter(mListener, "search");
        mRecyclerView.setAdapter(mAdapter);

        mTAGS = getSelectedTags();
        initRefreshListener();
        initScrollListener();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        toolbar.setSubtitle(mTAGS);

        mSwipeRefreshLayout.setRefreshing(true);
        new PullPost(0, mPAGE).execute();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        new AnalyticsUtils().getAnalytics(TAG, mTAGS, "search" ,mFirebaseAnalytics);
    }

    @Override
    public void onPostItemClick(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARG_POST_ITEM_POS, pos);
        args.putString(ARG_POST_TYPE, "search");
        Intent intent = new Intent(this, PostDetailActivity.class);
        intent.putExtras(args);
        startActivity(intent);

    }

    private String getSelectedTags(){
        Map<String, ?> allTag = new SharedPreferencesUtils().getALL(tagPref);
        String tags = "";
//        Log.i(TAG, tagPref.getAll().toString());
        for (Map.Entry<String, ?>  entry : allTag.entrySet()){
            if(entry.getValue().equals(true)){
                if (tags == ""){
                    tags = entry.getKey();
                }else {
                    tags = tags + "+" + entry.getKey();
                }
            }
        }
//        Log.i(TAG, "TAG: "+ tags);
        return tags;
    }

    private void initRefreshListener(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPullPostTask == null || mPullPostTask.isCancelled() || mPullPostTask.getStatus() == AsyncTask.Status.FINISHED){
                    mPullPostTask = new PullPost(0,1);
                    mPullPostTask.execute();
                }
            }
        });
    }

    private void initScrollListener(){
        mRecyclerView.setOnScrollListener(new OnRcvScrollListener(){
            @Override
            public void onBottom() {
                super.onBottom();
                Log.i(TAG,"加载更多");
                mSwipeRefreshLayout.setRefreshing(true);
                loadMore();
            }

            public void onScroll(){

            }
        });
    }

    private void loadMore(){
        if (mPullPostTask == null || mPullPostTask.getStatus() == AsyncTask.Status.FINISHED){
            mPAGE = mPAGE + 1;
            Log.i(TAG, "正在加载页码:"+mPAGE);
            mPullPostTask = new PullPost(1, mPAGE);
            mPullPostTask.execute();
        }
    }

    private class PullPost extends AsyncTask<Integer, Void, List<PostBean>> {

        private Integer status;  // 0:刷新 1：加载更多
        private Integer page;

        public PullPost(Integer status, Integer page){
            this.status = status;
            this.page = page;
        }

        @Override
        protected List<PostBean> doInBackground(Integer... integers) {
            if (isCancelled()){
                return null;
            }
            String domain = new SharedPreferencesUtils().getStringValue(booruUsedPref, booruDomainKey);
            String booruType = new SharedPreferencesUtils().getStringValue(booruUsedPref, booruTypeKey);
            if (domain == "null"){
                cancel(true);
            }
            mURL = booruType + domain + "/post.json";

            Log.i(TAG, "url:" + mURL);

            return new GetPost().getPosts(page, mTAGS, mURL);
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
        protected void onPostExecute(List<PostBean> postBeans) {
            super.onPostExecute(postBeans);
            if (mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (status == 0){
                if (postBeans != null){
                    mPostBeanSearchItems.clear();
                    for (int i = 0; i < postBeans.size(); i++){
                        mPostBeanSearchItems.add(postBeans.get(i));
                        mAdapter.updateData("search");
                    }
                    mPAGE = 1;
                }else {
                    Log.i(TAG,"结果为空");
                }
            }else if (status == 1){
                if (postBeans != null){
//                    Log.i(TAG,"正在加载的页数："+ page +" 接收数据大小：" + postBeans.size() + " 原有大小：" + mPostBeanSearchItems.size());
                    for (int i = 0; i < postBeans.size(); i++){
                        mPostBeanSearchItems.add(postBeans.get(i));
                        mAdapter.updateData("search");
                    }
                }else {
                    Log.i(TAG,"结果为空");
                }
            }
        }
    }
}
