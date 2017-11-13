package com.fiepi.moebooru.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiepi.moebooru.R;

/**
 * Created by fiepi on 11/12/17.
 */

public class PostsHolder extends RecyclerView.ViewHolder {
    ImageView ivPosts;
    TextView tvPostsID;

    public PostsHolder(View itemView){
        super(itemView);
        ivPosts = (ImageView) itemView.findViewById(R.id.iv_posts);
        tvPostsID = (TextView) itemView.findViewById(R.id.tv_posts_id);
    }
}
