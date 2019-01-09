package com.test.admin.conurbations.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/6.
 */
public class Artist extends LitePalSupport implements Parcelable {

    public String name;
    public Long id;
    public String artistId;
    public int count;
    public String type = Constants.LOCAL;
    public String picUrl;
    public String desc;
    public int musicSize;
    public int score;
    public int albumSize;

    public List<Music> songs;

    public Artist() {
    }

    public Artist(String artistId, String name, int musicSize) {
        this.name = name;
        this.artistId = artistId;
        this.musicSize = musicSize;
    }

    protected Artist(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        artistId = in.readString();
        count = in.readInt();
        type = in.readString();
        picUrl = in.readString();
        desc = in.readString();
        musicSize = in.readInt();
        score = in.readInt();
        albumSize = in.readInt();
        songs = in.createTypedArrayList(Music.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(artistId);
        dest.writeInt(count);
        dest.writeString(type);
        dest.writeString(picUrl);
        dest.writeString(desc);
        dest.writeInt(musicSize);
        dest.writeInt(score);
        dest.writeInt(albumSize);
        dest.writeTypedList(songs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
