package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class XiamiComment {

    @SerializedName("author")
    public boolean author;
    @SerializedName("isDelete")
    public boolean isDelete;
    @SerializedName("nickName")
    public String nickName;
    @SerializedName("isReport")
    public boolean isReport;
    @SerializedName("isLiked")
    public boolean isLiked;
    @SerializedName("topFlag")
    public int topFlag;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("gmtCreate")
    public long gmtCreate;
    @SerializedName("message")
    public String message;
    @SerializedName("userId")
    public int userId;
    @SerializedName("isOfficial")
    public int isOfficial;
    @SerializedName("objectType")
    public int objectType;
    @SerializedName("visits")
    public int visits;
    @SerializedName("commentId")
    public int commentId;
    @SerializedName("isHot")
    public boolean isHot;
    @SerializedName("objectId")
    public int objectId;
    @SerializedName("likes")
    public int likes;
}
