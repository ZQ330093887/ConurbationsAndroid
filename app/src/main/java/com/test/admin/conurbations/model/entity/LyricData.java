package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class LyricData {
    @SerializedName("data")
    public LyricInfo data;
    @SerializedName("status")
    public boolean status;

    public class LyricInfo {
        @SerializedName("translate")
        public List<List<String>> translate;
        @SerializedName("lyric")
        public List<List<String>> lyric;
    }
}
