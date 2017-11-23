package com.fiepi.moebooru.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fiepi on 11/23/17.
 */

public class TagSearchBean implements Parcelable {

    private String name;
    private Boolean status;


    public TagSearchBean(){

    }

    public String getName(){
        return name;
    }

    public Boolean getStatus(){
        return status;
    }

    public void setName(String s){
        this.name = s;
    }

    public void setStatus(Boolean b){
        this.status = b;
    }

    protected TagSearchBean(Parcel in) {
        this.name = in.readString();
        this.status = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte((byte)(this.status? 1:0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TagSearchBean> CREATOR = new Creator<TagSearchBean>() {
        @Override
        public TagSearchBean createFromParcel(Parcel in) {
            return new TagSearchBean(in);
        }

        @Override
        public TagSearchBean[] newArray(int size) {
            return new TagSearchBean[size];
        }
    };
}
