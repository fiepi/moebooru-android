package com.fiepi.moebooru.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;

import java.util.List;


/**
 * Created by fiepi on 11/12/17.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsHolder>{

    private List<PostBean> postBeans;

    public PostsAdapter(List<PostBean> postBeans){
        this.postBeans = postBeans;
    }

    @Override
    public PostsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post_layout, parent, false);
        PostsHolder postsHolder = new PostsHolder(view);
        return postsHolder;
    }

    public void updateData(List<PostBean> postBeans){
        this.postBeans = postBeans;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(PostsHolder holder, int position) {
        holder.tvPostsID.setText("#"+String.valueOf(this.postBeans.get(position).getId()));
        Glide.with(holder.ivPosts.getContext())
                .load(this.postBeans.get(position).getPreview_url())
                .into(holder.ivPosts);
    }

    @Override
    public int getItemCount() {
        return postBeans == null ? 0 : postBeans.size();
    }
}
