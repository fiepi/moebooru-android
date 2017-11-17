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

    private int mPOS = 1;
    private List<PostBean> mPostBeansItems = new ArrayList<>();
    private PostBean mPostBean;
    private Toolbar mToolbar;

    private PhotoView mPhotoView;

    public PostDetailFragment() {

    }

    public static PostDetailFragment newInstance(int pos, PostBean postBean) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POST_ITEM_POS, pos);
        args.putParcelable(ARG_POST_ITEM, postBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        mPhotoView = (PhotoView) rootView.findViewById(R.id.pv_post);
        mPhotoView.enable();
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            mPOS = getArguments().getInt(ARG_POST_ITEM_POS);
            mPostBean = getArguments().getParcelable(ARG_POST_ITEM);
            mToolbar.setTitle("ID: " + mPostBean.getId());
            initToolbarMenuListener();
            RequestOptions requestOptions = new RequestOptions()
                    .centerInside()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(this)
                    .load(mPostBean.getJpeg_url())
                    .apply(requestOptions)
                    .into(mPhotoView);
        }
    }

    private void initToolbarMenuListener(){
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_download_post:
                        downloadPost();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void downloadPost(){
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };

        long id = mPostBean.getId();
        String str = mPostBean.getFile_url();
        String ext = str.substring(str.length()-4, str.length());
        String site = "Konachan.com";
        String tags = null;
        for(int i = 0; i < mPostBean.getTags().size(); i++){
            if (tags == null){
                tags = mPostBean.getTags().get(i).getName();
            }else {
                tags = tags + " " + mPostBean.getTags().get(i).getName();
            }
        }
        String fileName = site +"_" + id + " " + tags + ext;
        fileName = fileName.replaceAll("/", "_");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/Moebooru/" + fileName);

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(getActivity(),
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }else {
                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
                request.setDestinationUri(Uri.fromFile(file));
                downloadManager.enqueue(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onButtonPressed(Uri uri) {

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
