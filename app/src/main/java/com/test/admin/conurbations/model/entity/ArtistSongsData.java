package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;
import com.test.admin.conurbations.model.MusicInfo;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class ArtistSongsData {
    @SerializedName("data")
    public ArtistSongs data;
    @SerializedName("status")
    public Boolean status;


    public class ArtistSongs {
        @SerializedName("detail")
        public ArtistItem detail;
        @SerializedName("songs")
        public List<MusicInfo> songs;
    }


    public class AlbumData {
        @SerializedName("data")
        public AlbumBean data;
        @SerializedName("status")
        public boolean status;
        @SerializedName("msg")
        public String msg;
    }

    public class AlbumBean {
        @SerializedName("name")
        public String name;
        @SerializedName("cover")
        public String cover;
        @SerializedName("desc")
        public String desc;
        @SerializedName("publishTime")
        public long publishTime;
        @SerializedName("artist")
        public ArtistItem artist;
        @SerializedName("songs")
        public List<MusicInfo> songs;
    }


    public class ArtistItem {
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;
        @SerializedName("cover")
        public String cover;
        @SerializedName("desc")
        public String desc;
    }
}
