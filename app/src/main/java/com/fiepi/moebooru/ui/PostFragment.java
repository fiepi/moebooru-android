package com.fiepi.moebooru.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
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
import com.fiepi.moebooru.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    private static final int SPAN_COUNT = 3;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnPostFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private PostViewAdapter mAdapter;
    private PullPost mPullPostTask = null;

    private List<PostBean> mPostBeansItems = new ArrayList<>();

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
        mAdapter = new PostViewAdapter(mPostBeansItems);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = rootView.findViewById(R.id.scroll_view);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRefreshListener();
        pullCachePosts();
    }

    private void initRefreshListener(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPullPostTask == null || mPullPostTask.isCancelled() || mPullPostTask.getStatus() == AsyncTask.Status.FINISHED){
                    mPullPostTask = new PullPost(0);
                    mPullPostTask.execute();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostFragmentInteractionListener) {
            mListener = (OnPostFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPostFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPostFragmentInteractionListener {
        void onPostFragmentInteraction(int i);
    }

    private class PullPost extends AsyncTask<Integer, Void, List<PostBean>>{

        private Integer status;  // 0:刷新 1：加载更多

        public PullPost(Integer integer){
            status = integer;
        }

        @Override
        protected List<PostBean> doInBackground(Integer... integers) {
            if (isCancelled()){
                return null;
            }
            return new GetPost().getPosts(20, 1, "null", "https://konachan.com/post.json");
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
                        //覆盖更新
                        if (postBeans.get(0).getId() > mPostBeansItems.get(0).getId()){
                            mPostBeansItems = postBeans;
                            mAdapter.notifyDataSetChanged();
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
                    mAdapter = new PostViewAdapter(mPostBeansItems);
                    mRecyclerView.setAdapter(mAdapter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            mSwipeRefreshLayout.setRefreshing(false);
        }else {
            mPullPostTask = new PullPost(0);
            mPullPostTask.execute();
        }
    }

}
