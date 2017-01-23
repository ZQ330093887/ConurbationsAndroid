package com.test.admin.conurbations.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by laucherish on 16/3/15.
 */
public class TopNews implements Parcelable {
    private String image;
    private int type;
    private int id;
    private String ga_prefix;
    private String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeInt(this.type);
        dest.writeInt(this.id);
        dest.writeString(this.ga_prefix);
        dest.writeString(this.title);
    }

    public TopNews() {
    }

    protected TopNews(Parcel in) {
        this.image = in.readString();
        this.type = in.readInt();
        this.id = in.readInt();
        this.ga_prefix = in.readString();
        this.title = in.readString();
    }

    public static final Creator<TopNews> CREATOR = new Creator<TopNews>() {
        public TopNews createFromParcel(Parcel source) {
            return new TopNews(source);
        }

        public TopNews[] newArray(int size) {
            return new TopNews[size];
        }
    };
}
