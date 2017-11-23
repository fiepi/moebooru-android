package com.fiepi.moebooru.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.fiepi.moebooru.R;
import com.fiepi.moebooru.bean.TagSearchBean;
import com.fiepi.moebooru.util.ClipboardUtils;
import com.fiepi.moebooru.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fiepi on 11/18/17.
 */

public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.TagViewHolder> {
    private static final String TAG = TagViewAdapter.class.getSimpleName();

    private static final String nameTagPref = "tag_search";

    private Context mContext;

    private List<String> mTagsName;
    private List<?> mTagsStatus;
    private Map<String,?> mTagsMap;

    public TagViewAdapter(Context context){
        mContext = context;
        getTags();
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_right_tags_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.mCheckBoxTag.setText(mTagsName.get(position));
        holder.mCheckBoxTag.setChecked((Boolean) mTagsStatus.get(position));

        holder.mCheckBoxTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                new SharedPreferencesUtils().saveBoolean(nameTagPref, mTagsName.get(position), b);
            }
        });

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
                                new ClipboardUtils().copy(mTagsName.get(position), mContext);
                                Toast.makeText(mContext, mTagsName.get(position) + " has been copied.", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_remove:
                                new SharedPreferencesUtils().removeValus(nameTagPref, mTagsName.get(position));
                                getTags();
                                notifyDataSetChanged();
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
        return mTagsName.size();
    }

    private void getTags(){
        mTagsMap = new SharedPreferencesUtils().getALL(nameTagPref);
        mTagsName = new ArrayList<String>(mTagsMap.keySet());
        mTagsStatus = new ArrayList<>(mTagsMap.values());
    }

    public void TagChanged() {
        getTags();
        notifyDataSetChanged();
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
