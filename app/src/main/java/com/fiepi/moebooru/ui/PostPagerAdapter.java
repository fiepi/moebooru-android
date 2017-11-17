package com.fiepi.moebooru.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fiepi.moebooru.bean.PostBean;

import java.util.List;

/**
 * Created by fiepi on 11/17/17.
 */

public class PostPagerAdapter extends FragmentStatePagerAdapter{
    private static final String IMAGE_URL = "image";

    private List<PostBean> mPostBeanItems;

    public PostPagerAdapter(FragmentManager fm, List<PostBean> data) {
        super(fm);
        mPostBeanItems = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mPostBeanItems.get(position).getFile_url();
        Fragment fragment = PostDetailFragment.newInstance(url);
        return fragment;
    }

    @Override
    public int getCount() {
        return mPostBeanItems.size();
    }
}
