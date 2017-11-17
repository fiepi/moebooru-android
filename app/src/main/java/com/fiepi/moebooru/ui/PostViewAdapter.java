package com.fiepi.moebooru.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.PostBean;
import com.fiepi.moebooru.ui.widget.FixedImageView;

import java.util.List;


public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {

    private static final String TAG = PostViewAdapter.class.getSimpleName();

    private List<PostBean> mPostBeanItems;
    private PostItemClickListener mListener;

    public PostViewAdapter(List<PostBean> items, PostItemClickListener listener) {
        mPostBeanItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mPostBeanItems.get(position);
        int whidth = mPostBeanItems.get(position).getWidth();
        int height = mPostBeanItems.get(position).getHeight();
        holder.mTextViewID.setText("#" + mPostBeanItems.get(position).getId());
        holder.mTextViewSize.setText(whidth + "x" + height);
        holder.mImageViewPost.setWidthAndHeightWeight(whidth, height);
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(holder.mImageViewPost.getContext())
                .load(this.mPostBeanItems.get(position).getPreview_url())
                .apply(requestOptions)
                .into(holder.mImageViewPost);

        holder.mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    Log.i(TAG, "点击 Item：" + position);
                    mListener.onPostItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostBeanItems == null ? 0 : mPostBeanItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final FixedImageView mImageViewPost;
        public final TextView mTextViewID;
        public final TextView mTextViewSize;
        public PostBean mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageViewPost = (FixedImageView) view.findViewById(R.id.iv_post);
            mTextViewID = (TextView) view.findViewById(R.id.tv_post_id);
            mTextViewSize = (TextView) view.findViewById(R.id.tv_post_size);
        }

        @Override
        public String toString() {
            return super.toString() + mTextViewID.getText();
        }
    }
}
