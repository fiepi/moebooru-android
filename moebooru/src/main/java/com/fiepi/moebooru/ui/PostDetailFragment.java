package com.fiepi.moebooru.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.util.ImgDownloadUtils;
import com.fiepi.moebooru.util.ShareUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

public class PostDetailFragment extends Fragment {

    private static final String TAG = PostDetailFragment.class.getSimpleName();

    private static final String IMAGE_URL = "image_url";
    private static final String POST = "POST";
    private String mImageUrl;
    private PhotoView mPhotoView;
    private Toolbar mToolbar;
    private ProgressWheel mProgressLoading;
    private PostBean mPostBean;

    public PostDetailFragment() {

    }

    public static PostDetailFragment newInstance(PostBean postBean) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(POST, postBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启菜单
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mPostBean = getArguments().getParcelable(POST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        mPhotoView = (PhotoView) rootView.findViewById(R.id.pv_post);
        mPhotoView.enable();
        mProgressLoading = (ProgressWheel) rootView.findViewById(R.id.progress_loading);
        mProgressLoading.setBarColor(Color.WHITE);
        mProgressLoading.setSpinSpeed(0.5f);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_detail);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setTitle("Post " + mPostBean.getId());

        RequestOptions requestOptions = new RequestOptions()
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(getContext())
                .load(mPostBean.getSample_url())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        mProgressLoading.setProgress(0.0f);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressLoading.setProgress(0.0f);
                        return false;
                    }
                })
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_download_post) {
            String site = "Konachan.com";
            new ImgDownloadUtils(mPostBean.getFile_url(), mPostBean.getTags(), mPostBean.getId(), site, getActivity()).toDownload();
            return true;
        }else if (id == R.id.action_share_post){
            String site = "https://konachan.com";
            String url = site + "/post/show/" + mPostBean.getId();
            new ShareUtils().shareText(url, getActivity());
        }

        return super.onOptionsItemSelected(item);
    }

}
