package com.fiepi.moebooru.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

    private int mRefreshStatus = 0;

    private RecyclerView mRecyclerView;
    private PostViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int LAYOUT_MANAGER = 2;
    private static final int SPAN_COUNT = 3;

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

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
        new PullPost(0).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_view, container, false);

        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            mRecyclerView = (RecyclerView) view;
//            if (LAYOUT_MANAGER == 2) {
//                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT,
//                        StaggeredGridLayoutManager.VERTICAL));
//            } else {
//                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//        }
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new PostViewAdapter(mPostBeansItems);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PostBean item);
    }

    private class PullPost extends AsyncTask<Integer, Void, List<PostBean>>{

        private Integer status = 0;
        public PullPost(Integer integer){
            status = integer;
        }

        @Override
        protected List<PostBean> doInBackground(Integer... integers) {
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
            mPostBeansItems = postBeans;
            Log.i(TAG,mPostBeansItems.get(0).getFile_url());
            mAdapter = new PostViewAdapter(mPostBeansItems);
            mRecyclerView.setAdapter(mAdapter);

//            if (status == 0){
//                if (!postBeans.isEmpty()){
//                    if (mRefreshStatus == 0) {
//                        if (postBeans.get(0).getId() > mPostBeansItems.get(0).getId()){
//                            mPostBeansItems = postBeans;
////                            initData();
////                            initView();
//                        }
//                        mRefreshStatus = 1;
//                    }else {
//                        for (int i = 0; i < postBeans.size(); i++){
//                            postBeans.add(postBeans.get(i));
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//            }else if (status == 1){
//                if (!postBeans.isEmpty()){
//                    for (int i = 0; i < postBeans.size(); i++){
//                        postBeans.add(postBeans.get(i));
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
        }
    }

    private void pullCachePosts(){
        File file = new File(getContext().getFilesDir(),"posts.json");
        if (file.exists()){
            try {
                mPostBeansItems = new FileUtils().getPostBeanFromFile();
                if (!mPostBeansItems.isEmpty()){
//                    initData();
//                    initView();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            new PullPost(0).execute();
        }
    }

}
