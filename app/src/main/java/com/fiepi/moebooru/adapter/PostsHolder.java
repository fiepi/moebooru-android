package com.fiepi.moebooru.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.widget.AutoHeightImageView;

/**
 * Created by fiepi on 11/12/17.
 */

public class PostsHolder extends RecyclerView.ViewHolder {
    AutoHeightImageView ivPosts;
    TextView tvPostsID;
    TextView tvPostsSize;

    public PostsHolder(View itemView){
        super(itemView);
        ivPosts = (AutoHeightImageView) itemView.findViewById(R.id.iv_posts);
        tvPostsID = (TextView) itemView.findViewById(R.id.tv_posts_id);
        tvPostsSize = (TextView) itemView.findViewById(R.id.tv_posts_size);
    }
}
