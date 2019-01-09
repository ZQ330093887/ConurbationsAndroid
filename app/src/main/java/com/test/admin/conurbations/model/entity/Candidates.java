package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/10.
 */
public class Candidates {
    @SerializedName("song")
    public String song;
    @SerializedName("hitlayer")
    public int hitlayer;
    @SerializedName("singer")
    public String singer;
    @SerializedName("language")
    public String language;
    @SerializedName("originame")
    public String originame;
    @SerializedName("duration")
    public int duration;
    @SerializedName("transuid")
    public String transuid;
    @SerializedName("score")
    public int score;
    @SerializedName("uid")
    public String uid;
    @SerializedName("transname")
    public String transname;
    @SerializedName("accesskey")
    public String accesskey;
    @SerializedName("adjust")
    public int adjust;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("soundname")
    public String soundname;
    @SerializedName("krctype")
    public int krctype;
    @SerializedName("origiuid")
    public String origiuid;
    @SerializedName("id")
    public String id;
    @SerializedName("sounduid")
    public String sounduid;

    public class KugouLyric {
        @SerializedName("ugc")
        public int ugc;
        @SerializedName("proposal")
        public String proposal;
        @SerializedName("candidates")
        public List<Candidates> candidates;
        @SerializedName("ugccount")
        public int ugccount;
        @SerializedName("keyword")
        public String keyword;
        @SerializedName("info")
        public String info;
        @SerializedName("status")
        public int status;
    }

    public class KugouLyricInfo {
        @SerializedName("charset")
        public String charset;
        @SerializedName("fmt")
        public String fmt;
        @SerializedName("content")
        public String content;
        @SerializedName("info")
        public String info;
        @SerializedName("status")
        public int status;
    }

}
