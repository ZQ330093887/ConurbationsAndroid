package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/11/29.
 */
public class BaiduList {

    @SerializedName("error_code")
    public int errorCode;
    @SerializedName("content")

    public List<ContentItem> content;

    public static class ContentItem {
        @SerializedName("pic_s210")
        public String picS210;
        @SerializedName("web_url")
        public String webUrl;
        @SerializedName("pic_s444")
        public String picS444;
        @SerializedName("name")
        public String name;
        @SerializedName("count")
        public int count;
        @SerializedName("comment")
        public String comment;
        @SerializedName("type")
        public int type;
        @SerializedName("pic_s192")
        public String picS192;
        @SerializedName("content")
        public List<Item> content;
        @SerializedName("pic_s260")
        public String picS260;
    }

    public static class Item {
        @SerializedName("all_rate")
        public String allRate;
        @SerializedName("song_id")
        public String songId;
        @SerializedName("rank_change")
        public String rankChange;
        @SerializedName("biaoshi")
        public String biaoshi;
        @SerializedName("author")
        public String author;
        @SerializedName("album_id")
        public String albumId;
        @SerializedName("title")
        public String title;
        @SerializedName("album_title")
        public String albumTitle;
    }
}
