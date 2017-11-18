package com.fiepi.moebooru.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiepi.moebooru.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiepi on 11/18/17.
 */

public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.TagViewHolder> {
    private static final String TAG = TagViewAdapter.class.getSimpleName();

    private List<String> mTags = new ArrayList<>();

    public TagViewAdapter(List<String> items){
        mTags = items;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_right_tags_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.mTextViewTag.setText(mTags.get(position));
        Log.i(TAG, mTags.get(position));
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextViewTag;

        public TagViewHolder(View itemView) {
            super(itemView);
            this.mTextViewTag = (TextView) itemView.findViewById(R.id.tv_tag);
        }
    }


}
