package com.fiepi.moebooru.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.ui.adapter.PostPagerAdapter;
import com.fiepi.moebooru.util.ImgDownloadUtils;
import com.fiepi.moebooru.util.ShareUtils;

import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = PostDetailActivity.class.getSimpleName();
    private static final String ARG_POST_ITEM_POS = "ARG_POST_ITEM_POS";
    private static final String ARG_POST_TYPE = "ARG_POST_TYPE";

    private int mPOS;
    private PostPagerAdapter mAdapter;
    private ViewPager mViewPager;

    private String mType = "post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mViewPager = (ViewPager) findViewById(R.id.vp_post);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPOS = bundle.getInt(ARG_POST_ITEM_POS);
        mType = bundle.getString(ARG_POST_TYPE);
        mAdapter = new PostPagerAdapter(getSupportFragmentManager(), mType);
        Log.i(TAG, mType);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPOS);
    }
}
