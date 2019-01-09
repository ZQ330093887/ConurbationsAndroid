package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class NeteaseComment {
    @SerializedName("beReplied")
    public List<BeRepliedItem> beReplied;
    @SerializedName("commentId")
    public int commentId;
    @SerializedName("time")
    public long time;
    @SerializedName("user")
    public SongComment.User user;
    @SerializedName("content")
    public String content;

    public class BeRepliedItem {
        @SerializedName("user")
        public SongComment.User user;
        @SerializedName("content")
        public String content;
        @SerializedName("status")
        public int status;
    }

}
