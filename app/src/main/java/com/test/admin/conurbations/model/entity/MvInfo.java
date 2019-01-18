package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZQiong on 2018/11/29.
 */
public class MvInfo implements Serializable {

    @SerializedName("code")
    public int code;
    @SerializedName("data")
    public List<MvInfoDetail> data;
    @SerializedName("hasMore")
    public boolean hasMore;
    @SerializedName("updateTime")
    public long updateTime;

    public static class MvInfoDetail implements Serializable {
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


    public static class MvDetailInfo implements Serializable {
        @SerializedName("code")
        public int code;
        @SerializedName("data")
        public MvInfoDetailInfo data;
    }

    public static class SimilarMvInfo implements Serializable {
        @SerializedName("code")
        public int code;
        @SerializedName("mvs")
        public List<MvInfoDetail> data;
    }


    public static class ArtistsItem implements Serializable {
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
    }

    public static class MvUrlInfo implements Serializable {
        public String p240;
        @SerializedName("480")
        public String p480;
        @SerializedName("720")
        public String p720;
        @SerializedName("1080")
        public String p1080;
    }

    public static class MvInfoDetailInfo implements Serializable {
        @SerializedName("publishTime")
        public String publishTime;
        @SerializedName("brs")
        public MvUrlInfo brs;
        @SerializedName("isReward")
        public boolean isReward;
        @SerializedName("commentThreadId")
        public String commentThreadId;
        @SerializedName("artistId")
        public int artistId;
        @SerializedName("likeCount")
        public int likeCount;
        @SerializedName("commentCount")
        public int commentCount;
        @SerializedName("cover")
        public String cover;
        @SerializedName("subCount")
        public int subCount;
        @SerializedName("duration")
        public int duration;
        @SerializedName("playCount")
        public int playCount;
        @SerializedName("shareCount")
        public int shareCount;
        @SerializedName("coverId")
        public long coverId;
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
        @SerializedName("nType")
        public int nType;
        @SerializedName("desc")
        public String desc;
    }


}
