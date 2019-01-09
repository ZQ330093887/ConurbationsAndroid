package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;
import com.test.admin.conurbations.model.user.User;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class SongCommentData<T> {
    @SerializedName("data")
    public CommentData<T> data;
    @SerializedName("status")
    public boolean status;

    public class CommentData<T> {
        @SerializedName("total")
        public int total;
        @SerializedName("comments")
        public List<T> comments;
    }

}
