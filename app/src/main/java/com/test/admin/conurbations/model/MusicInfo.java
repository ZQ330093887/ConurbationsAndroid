package com.test.admin.conurbations.model;

import com.google.gson.annotations.SerializedName;
import com.test.admin.conurbations.model.entity.Album;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQiong on 2018/11/29.
 */
public class MusicInfo implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("songId")
    public String songId;
    @SerializedName("name")
    public String name;
    @SerializedName("artists")
    public List<ArtistsItem> artists;
    @SerializedName("album")
    public Album album;
    @SerializedName("vendor")
    public String vendor;
    @SerializedName("dl")
    public boolean dl;
    @SerializedName("cp")
    public boolean cp;
    @SerializedName("quality")
    public QualityBean quality;

    public MusicInfo(String mid, String mid1, String title, ArrayList<ArtistsItem> artistsBeans, Album album, String type, boolean isCp, boolean isDl, QualityBean qualityBean) {
        this.id = mid;
        this.songId = mid1;
        this.name = title;
        this.artists = artistsBeans;
        this.album = album;
        this.vendor = type;
        this.dl = isDl;
        this.cp = isCp;
        this.quality = qualityBean;
    }


    public static class QualityBean implements Serializable {
        @SerializedName("192")
        public boolean high;
        @SerializedName("320")
        public boolean hq;
        @SerializedName("999")
        public boolean sq;

        public QualityBean(boolean hq, boolean sq, boolean high) {
            this.high = high;
            this.hq = hq;
            this.sq = sq;
        }
    }


    public static class Album implements Serializable {
        public String id;
        public String name;
        public String cover;
    }

    public static class ArtistsItem implements Serializable {
        public String id;
        public String name;
    }


    public static class CollectResult implements Serializable {
        @SerializedName("failedList")
        public List<CollectFailed> failedList;
    }

    public static class CollectDetail {
        @SerializedName("id")
        public String id;
        @SerializedName("vendor")
        public String vendor;


        public CollectDetail(String id, String vendor) {
            this.id = id;
            this.vendor = vendor;
        }
    }


    public static class CollectBatch2Bean {
        @SerializedName("collects")
        public List<CollectDetail> collects;

        public CollectBatch2Bean(List<CollectDetail> collects) {
            this.collects = collects;
        }
    }


    public class CollectFailed {
        @SerializedName("id")
        public String id;
        @SerializedName("msg")
        public String msg;
    }

}
