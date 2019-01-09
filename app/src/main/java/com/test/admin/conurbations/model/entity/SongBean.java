package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class SongBean {

    @SerializedName("data")
    public UrlData data;
    @SerializedName("status")
    public boolean status;
    @SerializedName("msg")
    public String msg;

    public class UrlData {
        @SerializedName("url")
        public String url;
    }
}
