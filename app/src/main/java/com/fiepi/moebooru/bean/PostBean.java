package com.fiepi.moebooru.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiepi on 11/12/17.
 */

public class PostBean implements Parcelable {

    public static final int FAV_FAVED = 1;
    public static final int FAV_UNFAVED = 0;
    public static final int FAV_UNKNOWN = -1;

    private long id;
    private long local_id;
    private List<TagBean> tags;
    private long created_at;
    private long creator_id;
    private String author;
    private int change;
    private String source;
    private int score;
    private String md5;
    private long file_size;
    private String file_url;
    private boolean is_shown_in_index;
    private String preview_url;
    private int preview_width;
    private int preview_height;
    private int actual_preview_width;
    private int actual_preview_height;
    private String sample_url;
    private int sample_width;
    private int sample_height;
    private int sample_file_size;
    private String jpeg_url;
    private int jpeg_width;
    private int jpeg_height;
    private int jpeg_file_size;
    private String rating;
    private boolean has_children;
    private long parent_id;
    private String status;
    private int width;
    private int height;
    private boolean is_held;
    private List<Long> children;
    private int vote;
    private int fav;

    public static final Creator<PostBean> CREATOR = new Creator<PostBean>() {
        @Override
        public PostBean createFromParcel(Parcel parcel) {
            return new PostBean(parcel);
        }

        @Override
        public PostBean[] newArray(int i) {
            return new PostBean[i];
        }
    };

    public PostBean(){

    }

    public long getLocal_id() {
        return this.local_id;
    }

    public void setLocal_id(long local_id) {
        this.local_id = local_id;
    }

    public boolean isHas_children() {
        return this.has_children;
    }

    public void setHas_children(boolean b) {
        this.has_children = b;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getCreator_id() {
        return this.creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<TagBean> getTags() {
        return this.tags;
    }

    public void setTags(List<TagBean> list) {
        this.tags = list;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int i) {
        this.score = i;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String str) {
        this.rating = str;
    }

    public long getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public List<Long> getChildren() {
        return this.children;
    }

    public void setChildren(List<Long> children) {
        this.children = children;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getFile_size() {
        return this.file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    private String getUrl(String url){
        String regex = "//konachan.*";
        if (url.matches(regex)){
            return "https:"+url;
        }
        return url;
    }

    public String getFile_url() {
        if (!TextUtils.isEmpty(this.file_url)) {
            return getUrl(this.file_url);
        }
        if (TextUtils.isEmpty(this.jpeg_url)) {
            return getUrl(this.sample_url);
        }
        return getUrl(this.jpeg_url);
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPreview_url() {
        return getUrl(this.preview_url);
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public int getPreview_width() {
        return this.preview_width;
    }

    public void setPreview_width(int preview_width) {
        this.preview_width = preview_width;
    }

    public int getPreview_height() {
        return this.preview_height;
    }

    public void setPreview_height(int preview_height) {
        this.preview_height = preview_height;
    }

    public int getSample_file_size() {
        return this.sample_file_size;
    }

    public void setSample_file_size(int sample_file_size) {
        this.sample_file_size = sample_file_size;
    }

    public String getSample_url() {
        return getUrl(this.sample_url);
    }

    public void setSample_url(String sample_url) {
        this.sample_url = sample_url;
    }

    public int getSample_width() {
        return this.sample_width;
    }

    public void setSample_width(int sample_width) {
        this.sample_width = sample_width;
    }

    public int getSample_height() {
        return this.sample_height;
    }

    public void setSample_height(int sample_height) {
        this.sample_height = sample_height;
    }

    public int getJpeg_file_size() {
        return this.jpeg_file_size;
    }

    public void setJpeg_file_size(int jpeg_file_size) {
        this.jpeg_file_size = jpeg_file_size;
    }

    public String getJpeg_url() {
        if (!TextUtils.isEmpty(this.jpeg_url)) {
            return getUrl(this.jpeg_url);
        }
        if (TextUtils.isEmpty(this.file_url)) {
            return getUrl(this.sample_url);
        }
        return getUrl(this.file_url);
    }

    public void setJpeg_url(String jpeg_url) {
        this.jpeg_url = jpeg_url;
    }

    public int getJpeg_width() {
        return this.jpeg_width;
    }

    public void setJpeg_width(int jpeg_width) {
        this.jpeg_width = jpeg_width;
    }

    public int getJpeg_height() {
        return this.jpeg_height;
    }

    public void setJpeg_height(int jpeg_height) {
        this.jpeg_height = jpeg_height;
    }

    public int getVote() {
        return this.vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getFav() {
        return this.fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public boolean isFaved() {
        return this.fav == 1;
    }

    public boolean isFavUnknow() {
        return this.fav == -1;
    }

    public boolean isUnFaved() {
        return this.fav == 0;
    }

    public void setFaved() {
        this.fav = 1;
    }

    public void setUnFaved() {
        this.fav = 0;
    }

    public void setFavUnknown() {
        this.fav = -1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        int j;
        parcel.writeLong(this.id);
        parcel.writeLong(this.local_id);
        parcel.writeString(this.author);
        parcel.writeLong(this.created_at);
        parcel.writeLong(this.creator_id);
        parcel.writeString(this.status);
        parcel.writeString(this.source);
        if (this.tags == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeList(this.tags);
        }
        parcel.writeInt(this.score);
        parcel.writeString(this.rating);
        parcel.writeLong(this.parent_id);
        if (this.has_children) {
            j = 1;
        } else {
            j = 0;
        }
        parcel.writeByte((byte) j);
        if (this.children == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeList(this.children);
        }
        parcel.writeString(this.md5);
        parcel.writeLong(this.file_size);
        parcel.writeString(this.file_url);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeString(this.preview_url);
        parcel.writeInt(this.preview_width);
        parcel.writeInt(this.preview_height);
        parcel.writeInt(this.sample_file_size);
        parcel.writeString(this.sample_url);
        parcel.writeInt(this.sample_width);
        parcel.writeInt(this.sample_height);
        parcel.writeInt(this.jpeg_file_size);
        parcel.writeString(this.jpeg_url);
        parcel.writeInt(this.jpeg_width);
        parcel.writeInt(this.jpeg_height);
        parcel.writeInt(this.vote);
        parcel.writeInt(this.fav);

    }

    protected PostBean(Parcel parcel){
        this.id = parcel.readLong();
        this.local_id = parcel.readLong();
        this.author = parcel.readString();
        this.created_at = parcel.readLong();
        this.creator_id = parcel.readLong();
        this.status = parcel.readString();
        this.source = parcel.readString();

        if (parcel.readByte() == (byte) 1) {
            this.tags = new ArrayList();
            parcel.readList(this.tags, TagBean.class.getClassLoader());
        } else {
            this.tags = null;
        }
        this.score = parcel.readInt();
        this.rating = parcel.readString();
        this.parent_id = parcel.readLong();
        this.has_children = parcel.readByte() != (byte) 0;
        if (parcel.readByte() == (byte) 1) {
            this.children = new ArrayList();
            parcel.readList(this.children, Long.class.getClassLoader());
        } else {
            this.children = null;
        }
        this.md5 = parcel.readString();
        this.file_size = parcel.readLong();
        this.file_url = parcel.readString();
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.preview_url = parcel.readString();
        this.preview_width = parcel.readInt();
        this.preview_height = parcel.readInt();
        this.sample_file_size = parcel.readInt();
        this.sample_url = parcel.readString();
        this.sample_width = parcel.readInt();
        this.sample_height = parcel.readInt();
        this.jpeg_file_size = parcel.readInt();
        this.jpeg_url = parcel.readString();
        this.jpeg_width = parcel.readInt();
        this.jpeg_height = parcel.readInt();
        this.vote = parcel.readInt();
        this.fav = parcel.readInt();
    }

}
