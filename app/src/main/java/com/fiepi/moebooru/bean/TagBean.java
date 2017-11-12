package com.fiepi.moebooru.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by fiepi on 11/12/17.
 */

public class TagBean implements Parcelable, Comparable<TagBean> {

    public static final Creator<TagBean> CREATOR = new Creator<TagBean>(){

        @Override
        public TagBean createFromParcel(Parcel parcel) {
            return new TagBean(parcel);
        }

        @Override
        public TagBean[] newArray(int i) {
            return new TagBean[i];
        }
    };

    public long _id;
    public int count;
    public long id;
    public String name;
    public int type;

    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getCount(){
        return this.count;
    }

    public void setCount(int i){
        this.count = i;
    }

    public int getType(){
        return this.type;
    }

    public void setType(int i){
        this.type = i;
    }

    public void setType(String type){
        if (type != null){
            if (type.equals("general")) {
                this.type = 0;
            } else if (type.equals("artist")) {
                this.type = 1;
            } else if (type.equals("copyright")) {
                this.type = 3;
            } else if (type.equals("character")) {
                this.type = 4;
            } else if (type.equals("circle")) {
                this.type = 5;
            } else if (type.equals("faults")) {
                this.type = 6;
            } else {
                this.type = 2;
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TagBean)) {
            return false;
        }
        TagBean tagBean = (TagBean) obj;
        if (this.count != tagBean.count) {
            return false;
        }
        if (this.id != tagBean.id) {
            return false;
        }
        if (this.type != tagBean.type) {
            return false;
        }
        if (this.name != null) {
            if (this.name.equals(tagBean.name)) {
                return true;
            }
        } else if (tagBean.name == null) {
            return true;
        }
        return false;
    }

    public int hashCode(){
        return (((((this.name != null ? this.name.hashCode() : 0) + (((int) (this.id ^ (this.id >>> 32))) * 31)) * 31) + this.count) * 31) + this.type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this._id);
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.count);
        parcel.writeInt(this.type);
    }

    @Override
    public int compareTo(@NonNull TagBean tagBean) {
        return getName().compareTo(tagBean.getName());
    }

    protected TagBean(Parcel parcel){
        this._id = parcel.readLong();
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.count = parcel.readInt();
        this.type = parcel.readInt();
    }

    public TagBean(){

    }
}
