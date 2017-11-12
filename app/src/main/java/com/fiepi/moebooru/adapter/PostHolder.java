package com.fiepi.moebooru.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bm.library.PhotoView;
import com.fiepi.moebooru.R;

/**
 * Created by fiepi on 11/12/17.
 */

public class PostHolder extends RecyclerView.ViewHolder {
    PhotoView pvPosts;
    public PostHolder(View itemView){
        super(itemView);
        pvPosts = (PhotoView) itemView.findViewById(R.id.pv_posts);
    }
}
