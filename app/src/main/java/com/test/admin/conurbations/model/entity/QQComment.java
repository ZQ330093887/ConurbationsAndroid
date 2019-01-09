package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class QQComment {

    @SerializedName("avatarurl")
    public String avatarurl;
    @SerializedName("nick")
    public String nick;
    @SerializedName("commentid")
    public String commentid;
    @SerializedName("rootcommentcontent")
    public String rootcommentcontent;
    @SerializedName("rootcommentid")
    public String rootcommentid;
    @SerializedName("middlecommentcontent")
    public Object middlecommentcontent;
    @SerializedName("rootcommentnick")
    public String rootcommentnick;
    @SerializedName("time")
    public long time;
    @SerializedName("uin")
    public String uin;
}
