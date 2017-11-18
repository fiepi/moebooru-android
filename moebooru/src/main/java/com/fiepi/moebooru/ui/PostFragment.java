package com.fiepi.moebooru.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.api.GetPost;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.ui.adapter.PostViewAdapter;
import com.fiepi.moebooru.ui.listener.OnRcvScrollListener;
import com.fiepi.moebooru.ui.listener.PostItemClickListener;
import com.fiepi.moebooru.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment implements PostItemClickListener {

    private static final String TAG = PostFragment.class.getSimpleName();
    private static final int SPAN_COUNT = 3;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private static final String ARG_POST_ITEM_POS = "ARG_POST_ITEM_POS";
    private static final String ARG_POST_ITEMS = "ARG_POST_ITEMS";
    private static final String ARG_POST_ITEM = "ARG_POST_ITEM";

    private Integer mPAGE = 1;
    private Integer mLIMIT = 30;
    private String mTAGS = "null";
    private String mURL = "https://konachan.com/post.json";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PostItemClickListener mListener = this;
    private PostViewAdapter mAdapter;
    private PullPost mPullPostTask = null;
    private List<PostBean> mPostBeansItems = new ArrayList<>();

    private FragmentManager mFragmentManager;

    public PostFragment() {
    }

    public static PostFragment newInstance(int columnCount) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_post);
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = rootView.findViewById(R.id.scroll_view);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new PostViewAdapter(mPostBeansItems, mListener);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pullCachePosts();
        initRefreshListener();
        initScrollListener();
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

    private void loadMore(){
        if (mPullPostTask == null || mPullPostTask.getStatus() == AsyncTask.Status.FINISHED){
            mPAGE = mPAGE + 1;
            Log.i(TAG, "正在加载页码:"+mPAGE);
            mPullPostTask = new PullPost(1, mPAGE);
            mPullPostTask.execute();
        }
    }

    @Override
    public void onPostItemClick(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARG_POST_ITEM_POS,pos);
        args.putParcelableArrayList(ARG_POST_ITEMS, (ArrayList<PostBean>) mPostBeansItems);
        Intent intent = new Intent(getContext(), PostDetailActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }


    private class PullPost extends AsyncTask<Integer, Void, List<PostBean>>{

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
            return new GetPost().getPosts(mLIMIT, page, mTAGS, mURL);
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
                if (!postBeans.isEmpty()){
                    if (!mPostBeansItems.isEmpty()){
//                        Log.i(TAG,"刷新成功");
                        if (postBeans.get(0).getId() > mPostBeansItems.get(0).getId()){
                            Log.i(TAG,"有新数据");
                            mPostBeansItems.clear();
//                            Log.i(TAG,"清理后 mPostBeansItems 的大小: " + mPostBeansItems.size());
                            for (int i = 0; i < postBeans.size(); i++){
                                mPostBeansItems.add(postBeans.get(i));
                                mAdapter.notifyDataSetChanged();
                            }
//                            Log.i(TAG,"mAdapter.getItemCount(): "+mAdapter.getItemCount());
                            mPAGE = 1;
                        }
                    }else {
                        //逐条更新
                        for (int i = 0; i < postBeans.size(); i++){
                            mPostBeansItems.add(postBeans.get(i));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }else if (status == 1){
                if (!postBeans.isEmpty()){
//                    Log.i(TAG,"正在加载的页数："+ page +" 接收数据大小：" + postBeans.size() + " 原有大小：" + mPostBeansItems.size());
                    for (int i = 0; i < postBeans.size(); i++){
                        mPostBeansItems.add(postBeans.get(i));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void pullCachePosts(){
        File file = new FileUtils().getFile();
        if (file.exists()){
            try {
                mPostBeansItems = new FileUtils().getPostBeanFromFile();
                if (!mPostBeansItems.isEmpty()){
                    mAdapter = new PostViewAdapter(mPostBeansItems, mListener);
                    mRecyclerView.setAdapter(mAdapter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            mPullPostTask = new PullPost(0, mPAGE);
            mPullPostTask.execute();
        }
    }
}
