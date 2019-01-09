package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class ArtistMusicList {

    @SerializedName("songlist")
    public List<BaiduMusicList.SongListItem> songList;
    @SerializedName("error_code")
    public int errorCode;
    @SerializedName("havemore")
    public int haveMore;
    @SerializedName("songnums")
    public int songNums;

}
