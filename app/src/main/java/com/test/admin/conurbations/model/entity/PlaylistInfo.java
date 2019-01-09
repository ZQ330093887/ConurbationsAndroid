package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;
import com.test.admin.conurbations.model.MusicInfo;

import java.util.List;

/**
 * Created by ZQiong on 2018/11/29.
 */
public class PlaylistInfo {

    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("cover")
    public String cover;
    @SerializedName("playCount")
    public Long playCount;
    @SerializedName("id")
    public String id;
    @SerializedName("total")
    public int total;
    @SerializedName("list")
    public List<MusicInfo> list;
}
