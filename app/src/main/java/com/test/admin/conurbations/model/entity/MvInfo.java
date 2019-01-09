package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/11/29.
 */
public class MvInfo {

    @SerializedName("code")
    public int code;
    @SerializedName("data")
    public List<MvInfoDetail> data;
    @SerializedName("hasMore")
    public boolean hasMore;
    @SerializedName("updateTime")
    public long updateTime;

    public static class MvInfoDetail {
        @SerializedName("lastRank")
        public int lastRank;
        @SerializedName("artistId")
        public int artistId;
        @SerializedName("cover")
        public String cover;
        @SerializedName("duration")
        public int duration;
        @SerializedName("playCount")
        public int playCount;
        @SerializedName("score")
        public int score;
        @SerializedName("subed")
        public boolean subed;
        @SerializedName("briefDesc")
        public String briefDesc;
        @SerializedName("artists")
        public List<ArtistsItem> artists;
        @SerializedName("name")
        public String name;
        @SerializedName("artistName")
        public String artistName;
        @SerializedName("id")
        public int id;
        @SerializedName("mark")
        public int mark;
        @SerializedName("desc")
        public String desc;
    }

    public static class ArtistsItem {
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
    }
}
