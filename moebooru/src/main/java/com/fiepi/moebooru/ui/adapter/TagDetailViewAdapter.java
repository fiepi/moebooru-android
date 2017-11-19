package com.fiepi.moebooru.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.TagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiepi on 11/18/17.
 */

public class TagDetailViewAdapter extends RecyclerView.Adapter<TagDetailViewAdapter.TagDetailViewHolder> {
    private static final String TAG = TagDetailViewAdapter.class.getSimpleName();

    private List<TagBean> mTags = new ArrayList<>();

    public TagDetailViewAdapter(List<TagBean> items){
        mTags = items;
    }

    @Override
    public TagDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_tags_item, parent, false);
        return new TagDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagDetailViewHolder holder, int position) {
        holder.mTextViewTagDetail.setText(mTags.get(position).getName());
        Log.i(TAG, mTags.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public class TagDetailViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextViewTagDetail;

        public TagDetailViewHolder(View itemView) {
            super(itemView);
            this.mTextViewTagDetail = (TextView) itemView.findViewById(R.id.tv_tag_detail);
        }
    }


}
