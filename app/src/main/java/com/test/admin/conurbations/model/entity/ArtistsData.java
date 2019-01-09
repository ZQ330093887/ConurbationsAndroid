package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/18.
 */
public class ArtistsData {
    @SerializedName("data")
    public Artists data;
    @SerializedName("status")
    public boolean status;
    @SerializedName("msg")
    public String msg;

    public class Artists {
        @SerializedName("area")
        public int area;
        @SerializedName("genre")
        public int genre;
        @SerializedName("index")
        public int index;
        @SerializedName("sex")
        public int sex;
        @SerializedName("singerlist")
        public List<SingerItem> singerList;
        @SerializedName("tags")
        public SingerTag tags;
        @SerializedName("total")
        public int total;
    }

    public class SingerItem {
        @SerializedName("country")
        public String country;
        @SerializedName("singer_id")
        public String singer_id;
        @SerializedName("singer_mid")
        public String singer_mid;
        @SerializedName("singer_pic")
        public String singer_pic;
        @SerializedName("singer_name")
        public String singer_name;
    }


    public class SingerTag {
        @SerializedName("area")
        public List<SingerCate> area;
        @SerializedName("genre")
        public List<SingerCate> genre;
        @SerializedName("index")
        public List<SingerCate> index;
        @SerializedName("sex")
        public List<SingerCate> sex;
    }

    public class SingerCate {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
    }

}
