package com.test.admin.conurbations.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

/**
 * Created by ZQiong on 2018/11/29.
 */
public class Music extends LitePalSupport implements Parcelable {

    // 歌曲类型 本地/网络
    public String type;
    public long id;
    // 歌曲id
    public String mid;
    // 音乐标题
    public String title;
    // 艺术家
    public String artist;//{123,123,13}
    // 专辑
    public String album;
    // 专辑id
    public String artistId;//{123,123,13}
    // 专辑id
    public String albumId;
    // 专辑内歌曲个数
    public int trackNumber;
    // 持续时间
    public long duration;
    // 收藏
    public boolean isLove;
    // [本地|网络]
    public boolean isOnline = true;
    // 音乐路径
    public String uri;
    // [本地|网络] 音乐歌词地址
    public String lyric;
    // [本地|网络]专辑封面路径
    public String coverUri;
    // [网络]专辑封面
    public String coverBig;
    // [网络]small封面
    public String coverSmall;
    // 文件名
    public String fileName;
    // 文件大小
    public long fileSize;
    // 发行日期
    public String year;
    //更新日期
    public long date;
    //在线歌曲是否限制播放，false 可以播放
    public boolean isCp;
    //在线歌曲是否付费歌曲，false 不能下载
    public boolean isDl = true;
    //收藏id
    public String collectId;
    //音乐品质，默认标准模式
    public int quality = 128000;
    //音乐品质选择
    public boolean hq; //192
    public boolean sq; //320
    public boolean high; //999
    //是否有mv 0代表无，1代表有
    public int hasMv;

    public Music() {
    }

    protected Music(Parcel in) {
        type = in.readString();
        id = in.readLong();
        mid = in.readString();
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        artistId = in.readString();
        albumId = in.readString();
        trackNumber = in.readInt();
        duration = in.readLong();
        isLove = in.readByte() != 0;
        isOnline = in.readByte() != 0;
        uri = in.readString();
        lyric = in.readString();
        coverUri = in.readString();
        coverBig = in.readString();
        coverSmall = in.readString();
        fileName = in.readString();
        fileSize = in.readLong();
        year = in.readString();
        date = in.readLong();
        isCp = in.readByte() != 0;
        isDl = in.readByte() != 0;
        collectId = in.readString();
        quality = in.readInt();
        hq = in.readByte() != 0;
        sq = in.readByte() != 0;
        high = in.readByte() != 0;
        hasMv = in.readInt();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeLong(id);
        dest.writeString(mid);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(artistId);
        dest.writeString(albumId);
        dest.writeInt(trackNumber);
        dest.writeLong(duration);
        dest.writeByte((byte) (isLove ? 1 : 0));
        dest.writeByte((byte) (isOnline ? 1 : 0));
        dest.writeString(uri);
        dest.writeString(lyric);
        dest.writeString(coverUri);
        dest.writeString(coverBig);
        dest.writeString(coverSmall);
        dest.writeString(fileName);
        dest.writeLong(fileSize);
        dest.writeString(year);
        dest.writeLong(date);
        dest.writeByte((byte) (isCp ? 1 : 0));
        dest.writeByte((byte) (isDl ? 1 : 0));
        dest.writeString(collectId);
        dest.writeInt(quality);
        dest.writeByte((byte) (hq ? 1 : 0));
        dest.writeByte((byte) (sq ? 1 : 0));
        dest.writeByte((byte) (high ? 1 : 0));
        dest.writeInt(hasMv);
    }
}
