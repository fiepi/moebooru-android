package com.fiepi.moebooru.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
    private static final String ARG_POST_ITEMS = "ARG_POST_ITEMS";

    private List<PostBean> mPostBeanItems;
    private int mPOS;
    private PostPagerAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mViewPager = (ViewPager) findViewById(R.id.vp_post);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPOS = bundle.getInt(ARG_POST_ITEM_POS);
        mPostBeanItems = bundle.getParcelableArrayList(ARG_POST_ITEMS);
        mAdapter = new PostPagerAdapter(getSupportFragmentManager(), mPostBeanItems);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPOS);
    }

    /**

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_download_post) {
            String site = "Konachan.com";
            PostBean postBean = mPostBeanItems.get(mViewPager.getCurrentItem());
            new ImgDownloadUtils(postBean.getFile_url(), postBean.getTags(),
                    mPostBeanItems.get(mViewPager.getCurrentItem()).getId(),
                    site, this)
                    .toDownload();
            return true;
        }else if (id == R.id.action_share_post){
            String site = "https://konachan.com";
            String url = site + "/post/show/" + mPostBeanItems.get(mViewPager.getCurrentItem()).getId();
            new ShareUtils().shareText(url, this);
        }

        return super.onOptionsItemSelected(item);
    }
    **/
}
