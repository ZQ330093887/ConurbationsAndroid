package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/20.
 */
public class SearchInfo {

    @SerializedName("result")
    public Result result;
    @SerializedName("code")
    public int code = 0;


    public class Result {
        @SerializedName("hots")
        public List<HotsItem> hots;
        @SerializedName("playlists")
        public List<NeteasePlaylistDetail.PlaylistsItem> playlists;
        @SerializedName("playlistCount")
        public int playlistCount;
        @SerializedName("mvs")
        public List<MvInfo.MvInfoDetail> mvs;
        @SerializedName("mvCount")
        public int mvCount;
        @SerializedName("artists")
        public List<ArtistInfo> artists;
        @SerializedName("artistCount")
        public int artistCount;
    }

    public class HotsItem {
        @SerializedName("first")
        public String first;
        @SerializedName("second")
        public int second;
    }


    public class ArtistInfo {
        @SerializedName("lastRank")
        public int lastRank;
        @SerializedName("img1v1Url")
        public String imgVUrl;
        @SerializedName("musicSize")
        public int musicSize;
        @SerializedName("img1v1Id")
        public long imgVId;
        @SerializedName("albumSize")
        public int albumSize;
        @SerializedName("picUrl")
        public String picUrl;
        @SerializedName("score")
        public int score;
        @SerializedName("topicPerson")
        public int topicPerson;
        @SerializedName("briefDesc")
        public String briefDesc;
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
        @SerializedName("picId")
        public long picId;
        @SerializedName("trans")
        public String trans;
    }


}
