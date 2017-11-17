package com.fiepi.moebooru.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class PostDetailFragment extends Fragment {

    private static final String TAG = PostDetailFragment.class.getSimpleName();

    private static final String ARG_POST_ITEM_POS = "ARG_POST_ITEM_POS";
    private static final String ARG_POST_ITEMS = "ARG_POST_ITEMS";
    private static final String ARG_POST_ITEM = "ARG_POST_ITEM";

    private static final String IMAGE_URL = "image";
    private String mImageUrl;
    private PhotoView mPhotoView;

    public PostDetailFragment() {

    }

    public static PostDetailFragment newInstance(String url) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        mPhotoView = (PhotoView) rootView.findViewById(R.id.pv_post);
        mPhotoView.enable();
        RequestOptions requestOptions = new RequestOptions()
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getContext())
                .load(mImageUrl)
                .apply(requestOptions)
                .into(mPhotoView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
