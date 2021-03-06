package com.fiepi.moebooru.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.fiepi.moebooru.AppConfig;
import com.fiepi.moebooru.ui.PostDetailFragment;

/**
 * Created by fiepi on 11/17/17.
 */

public class PostPagerAdapter extends FragmentStatePagerAdapter{
    private static final String TAG = PostPagerAdapter.class.getSimpleName();
    private String mType = "post";
    private int mCount = 0;

    public PostPagerAdapter(FragmentManager fm, String type) {
        super(fm);
        mType = type;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = PostDetailFragment.newInstance(position, mType);
//        Log.i(TAG, "position:" + position + " type:" + mType);
        return fragment;
    }

    @Override
    public int getCount() {
        int size;
        if (mType.equals("post")){
            Log.i(TAG, "post" + AppConfig.mPostBeanPostItems.size() );
            size = AppConfig.mPostBeanPostItems.size();
            if (mCount != size){
                mCount = size;
                notifyDataSetChanged();
            }
            return mCount;
        }
        if (mType.equals("search")){
            Log.i(TAG, "search" + AppConfig.mPostBeanSearchItems.size() );
            size = AppConfig.mPostBeanSearchItems.size();
            if (mCount != size){
                mCount = size;
                notifyDataSetChanged();
            }
            return mCount;
        }
//        Log.i(TAG, "null"+mType);
        return 0;
    }
}
