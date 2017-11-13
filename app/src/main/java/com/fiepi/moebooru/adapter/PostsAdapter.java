package com.fiepi.moebooru.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;

import java.util.List;

import static com.fiepi.moebooru.R.color.colorAccent;


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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(PostsHolder holder, int position) {
        holder.tvPostsID.setText("#"+String.valueOf(this.postBeans.get(position).getId()));
        holder.tvPostsSize.setText(String.valueOf(this.postBeans.get(position).getWidth()) + "x" + String.valueOf(this.postBeans.get(position).getHeight()));
        holder.ivPosts.setBackgroundColor(colorAccent);
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
//        Glide.with(holder.ivPosts.getContext()).clear(holder.ivPosts);
        Glide.with(holder.ivPosts.getContext())
                .load(this.postBeans.get(position).getPreview_url())
                .apply(requestOptions)
                .into(holder.ivPosts);

    }

    @Override
    public int getItemCount() {
        return postBeans == null ? 0 : postBeans.size();
    }
}
