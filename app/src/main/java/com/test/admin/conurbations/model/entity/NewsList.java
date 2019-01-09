package com.test.admin.conurbations.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQiong on 18/11/15.
 */
public class NewsList extends LitePalSupport implements Parcelable {
    public long id;
    //歌单id
    public String pid;
    //歌单名
    public String name;
    //歌曲数量
    public long total;
    //更新日期
    public long updateDate;
    //创建日期
    public long date;
    //描述
    public String des;
    //排列顺序
    public String order;
    //封面
    public String coverUrl;
    //类型:本地歌单、在线同步歌单、百度音乐电台、网易云歌单、百度排行榜
    public String type = Constants.PLAYLIST_LOCAL_ID;

    public long playCount;
    //歌曲集合
    public List<Music> musicList;

    public NewsList(){}

    protected NewsList(Parcel in) {
        id = in.readLong();
        pid = in.readString();
        name = in.readString();
        total = in.readLong();
        updateDate = in.readLong();
        date = in.readLong();
        des = in.readString();
        order = in.readString();
        coverUrl = in.readString();
        type = in.readString();
        playCount = in.readLong();
        musicList = in.createTypedArrayList(Music.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(pid);
        dest.writeString(name);
        dest.writeLong(total);
        dest.writeLong(updateDate);
        dest.writeLong(date);
        dest.writeString(des);
        dest.writeString(order);
        dest.writeString(coverUrl);
        dest.writeString(type);
        dest.writeLong(playCount);
        dest.writeTypedList(musicList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewsList> CREATOR = new Creator<NewsList>() {
        @Override
        public NewsList createFromParcel(Parcel in) {
            return new NewsList(in);
        }

        @Override
        public NewsList[] newArray(int size) {
            return new NewsList[size];
        }
    };
}
