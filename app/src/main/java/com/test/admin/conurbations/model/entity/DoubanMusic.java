package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/11/30.
 */
public class DoubanMusic {

    @SerializedName("total")
    public int total;
    @SerializedName("count")
    public int count;
    @SerializedName("start")
    public int start;
    @SerializedName("musics")
    public List<MusicsItem> musics;

    public static class MusicsItem {
        @SerializedName("image")
        public String image;
        @SerializedName("alt_title")
        public String altTitle;
        @SerializedName("author")
        public List<AuthorItem> author;
        @SerializedName("rating")
        public Rating rating;
        @SerializedName("alt")
        public String alt;
        @SerializedName("mobile_link")
        public String mobileLink;
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("tags")
        public List<TagsItem> tags;
        @SerializedName("attrs")
        public Attrs attrs;
    }


    public static class Rating {
        @SerializedName("average")
        public String average;
        @SerializedName("min")
        public int min;
        @SerializedName("max")
        public int max;
        @SerializedName("numRaters")
        public int numRaters;
    }

    public static class Attrs {
        @SerializedName("singer")
        public List<String> singer;
        @SerializedName("publisher")
        public List<String> publisher;
        @SerializedName("media")
        public List<String> media;
        @SerializedName("title")
        public List<String> title;
        @SerializedName("discs")
        public List<String> discs;
        @SerializedName("version")
        public List<String> version;
        @SerializedName("tracks")
        public List<String> tracks;
        @SerializedName("pubdate")
        public List<String> pubdate;
    }

    public static class AuthorItem {
        @SerializedName("name")
        public String name;
    }

    public static class TagsItem {
        @SerializedName("count")
        public int count;
        @SerializedName("name")
        public String name;
    }


}
