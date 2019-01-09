package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;
import com.test.admin.conurbations.model.MusicInfo;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class SearchData {
    @SerializedName("data")
    public Data data;
    @SerializedName("status")
    public boolean status;
    @SerializedName("msg")
    public String msg;


    public class Data {
        @SerializedName("qq")
        public Qq qq;
        @SerializedName("xiami")
        public Xiami xiami;
        @SerializedName("netease")
        public Netease netease;
    }

    public class Qq {
        @SerializedName("total")
        public String total;
        @SerializedName("songs")
        public List<MusicInfo> songs;
        @SerializedName("keyword")
        public String keyword;
    }

    public class Xiami {
        @SerializedName("total")
        public String total;
        @SerializedName("songs")
        public List<MusicInfo> songs;
    }

    public class Netease {
        @SerializedName("total")
        public String total;
        @SerializedName("songs")
        public List<MusicInfo> songs;
    }

    public class SongDetail {
        @SerializedName("data")
        public MusicInfo data;
        @SerializedName("status")
        public boolean status;
        @SerializedName("msg")
        public String msg;
    }

    public class BatchSongDetail {
        @SerializedName("data")
        public List<MusicInfo> data;
        @SerializedName("status")
        public boolean status;
        @SerializedName("msg")
        public String msg;
        @SerializedName("log")
        public LogDetail log;
    }


    public class LogDetail {
        @SerializedName("msg")
        public String msg;
    }

    public class SearchSingleData {
        @SerializedName("data")
        public SearchResult data;
        @SerializedName("status")
        public boolean status;
        @SerializedName("msg")
        public String msg;
    }

    public class SearchResult {
        @SerializedName("total")
        public String total;
        @SerializedName("songs")
        public List<MusicInfo> songs;
    }
}
