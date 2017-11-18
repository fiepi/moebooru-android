package com.fiepi.moebooru.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.ui.PostDetailFragment;

import java.util.List;

/**
 * Created by fiepi on 11/17/17.
 */

public class PostPagerAdapter extends FragmentStatePagerAdapter{
    private static final String TAG = PostPagerAdapter.class.getSimpleName();
    private static final String POST = "POST";
    private List<PostBean> mPostBeanItems;

    public PostPagerAdapter(FragmentManager fm, List<PostBean> data) {
        super(fm);
        mPostBeanItems = data;
    }

    @Override
    public Fragment getItem(int position) {
        PostBean postBean = mPostBeanItems.get(position);
        Fragment fragment = PostDetailFragment.newInstance(postBean);
        return fragment;
    }

    @Override
    public int getCount() {
        return mPostBeanItems.size();
    }
}
