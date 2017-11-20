package com.fiepi.moebooru.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.TagBean;

import java.nio.file.CopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiepi on 11/18/17.
 */

public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.TagViewHolder> {
    private static final String TAG = TagViewAdapter.class.getSimpleName();

    private List<String> mTags = new ArrayList<>();
    private Context mContext;

    public TagViewAdapter(List<String> items, Context context){
        mTags = items;
        mContext = context;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_right_tags_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.mCheckBoxTag.setText(mTags.get(position));
//        Log.i(TAG, mTags.get(position));
        holder.mImageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click more icon");
                PopupMenu popupMenu = new PopupMenu(mContext, holder.mImageViewMore);
                popupMenu.inflate(R.menu.tag_option);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.i(TAG, "click Menu");
                        switch (menuItem.getItemId()) {
                            case R.id.menu_copy:

                                break;
                            case R.id.menu_remove:

                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }


    public class TagViewHolder extends RecyclerView.ViewHolder {

        public CheckBox mCheckBoxTag;
        public ImageView mImageViewMore;

        public TagViewHolder(View itemView) {
            super(itemView);
            this.mCheckBoxTag = (CheckBox) itemView.findViewById(R.id.cb_tag);
            this.mImageViewMore = (ImageView) itemView.findViewById(R.id.iv_tag_more);
        }
    }
}
