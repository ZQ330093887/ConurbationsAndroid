package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class RadioChannelData {

    @SerializedName("result")
    public ChannelInfos result;
    @SerializedName("error_code")
    public int errorCode;

    public static class ChannelInfos {
        @SerializedName("channel")
        public String channel;
        @SerializedName("count")
        public Object count;
        @SerializedName("ch_name")
        public String chName;
        @SerializedName("artistid")
        public Object artistid;
        @SerializedName("avatar")
        public Object avatar;
        @SerializedName("songlist")
        public List<CHSongInfo> songlist;
        @SerializedName("channelid")
        public Object channelid;
    }

    public static class CHSongInfo {
        @SerializedName("all_rate")
        public String allRate;
        @SerializedName("charge")
        public int charge;
        @SerializedName("method")
        public int method;
        @SerializedName("artist")
        public String artist;
        @SerializedName("thumb")
        public String thumb;
        @SerializedName("all_artist_id")
        public String allArtistId;
        @SerializedName("resource_type")
        public String resourceType;
        @SerializedName("havehigh")
        public int havehigh;
        @SerializedName("title")
        public String title;
        @SerializedName("songid")
        public String songid;
        @SerializedName("artist_id")
        public String artistId;
        @SerializedName("flow")
        public int flow;
    }
}
