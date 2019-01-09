package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class BaiduSongInfo {

    @SerializedName("data")
    public Data data;
    @SerializedName("errorCode")
    int errorCode;


    public static class Data {
        @SerializedName("xcode")
        public String xcode;
        @SerializedName("songList")
        public List<SongItem> songList;
    }

    public static class SongItem {
        @SerializedName("songName")
        public String songName;
        @SerializedName("albumName")
        public String albumName;
        @SerializedName("linkCode")
        public int linkCode;
        @SerializedName("format")
        public String format;
        @SerializedName("albumId")
        public int albumId;
        @SerializedName("artistId")
        public String artistId;
        @SerializedName("ting_uid")
        public String tingUid;
        @SerializedName("source")
        public String source;
        @SerializedName("songPicBig")
        public String songPicBig;
        @SerializedName("version")
        public String version;
        @SerializedName("queryId")
        public String queryId;
        @SerializedName("songLink")
        public String songLink;
        @SerializedName("size")
        public int size;
        @SerializedName("rate")
        public int rate;
        @SerializedName("lrcLink")
        public String lrcLink;
        @SerializedName("copyType")
        public int copyType;
        @SerializedName("artistName")
        public String artistName;
        @SerializedName("time")
        public int time;
        @SerializedName("relateStatus")
        public String relateStatus;
        @SerializedName("songPicSmall")
        public String songPicSmall;
        @SerializedName("songId")
        public int songId;
        @SerializedName("songPicRadio")
        public String songPicRadio;
        @SerializedName("showLink")
        public String showLink;
        @SerializedName("resourceType")
        public String resourceType;
    }

}
