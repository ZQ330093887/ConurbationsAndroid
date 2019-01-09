package com.test.admin.conurbations.model.entity;

import android.app.LauncherActivity;

import com.google.gson.annotations.SerializedName;
import com.test.admin.conurbations.model.MusicInfo;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class NeteaseBean {
    @SerializedName("data")
    public TopData data;
    @SerializedName("status")
    public boolean status;


    public class TopData {
        @SerializedName("cover")
        public String cover;
        @SerializedName("playCount")
        public long playCount;
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String description;
        @SerializedName("list")
        public List<ListItem> list;
    }

    public class ListItem{
        @SerializedName("artists")
        public List<MusicInfo.ArtistsItem> artists;
        @SerializedName("album")
        public Album album;
        @SerializedName("name")
        public String name;
        @SerializedName("commentId")
        public int commentId;
        @SerializedName("id")
        public int id;
        @SerializedName("cp")
        public boolean cp;
    }


}
