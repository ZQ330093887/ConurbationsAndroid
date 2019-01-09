package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class NeteasePlaylistDetail {

    @SerializedName("code")
    public int code;
    @SerializedName("playlist")
    public PlaylistsItem playlist;

    public static class PlaylistsItem {
        @SerializedName("description")
        public String description;
        @SerializedName("privacy")
        public int privacy;
        @SerializedName("trackNumberUpdateTime")
        public long trackNumberUpdateTime;
        @SerializedName("subscribed")
        public Object subscribed;
        @SerializedName("shareCount")
        public int shareCount;
        @SerializedName("trackCount")
        public int trackCount;
        @SerializedName("adType")
        public int adType;
        @SerializedName("coverImgId_str")
        public String coverImgIdStr;
        @SerializedName("specialType")
        public int specialType;
        @SerializedName("copywriter")
        public String copywriter;
        @SerializedName("id")
        public long id;
        @SerializedName("tag")
        public String tag;
        @SerializedName("totalDuration")
        public int totalDuration;
        @SerializedName("ordered")
        public boolean ordered;
        @SerializedName("creator")
        public Creator creator;
        @SerializedName("subscribers")
        public List<SubscribersItem> subscribers;
        @SerializedName("commentThreadId")
        public String commentThreadId;
        @SerializedName("highQuality")
        public boolean highQuality;
        @SerializedName("updateTime")
        public long updateTime;
        @SerializedName("trackUpdateTime")
        public long trackUpdateTime;
        @SerializedName("userId")
        public int userId;
        @SerializedName("tracks")
        public List<TracksItem> tracks;
        @SerializedName("tags")
        public List<String> tags;
        @SerializedName("anonimous")
        public boolean anonimous;
        @SerializedName("commentCount")
        public int commentCount;
        @SerializedName("cloudTrackCount")
        public int cloudTrackCount;
        @SerializedName("coverImgUrl")
        public String coverImgUrl;
        @SerializedName("playCount")
        public int playCount;
        @SerializedName("coverImgId")
        public long coverImgId;
        @SerializedName("createTime")
        public long createTime;
        @SerializedName("name")
        public String name;
        @SerializedName("subscribedCount")
        public int subscribedCount;
        @SerializedName("status")
        public int status;
        @SerializedName("newImported")
        public boolean newImported;
    }

    public final class TracksItem {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("ar")
        public List<MvInfo.ArtistsItem> artists;
        @SerializedName("al")
        public AlbumItem album;
        @SerializedName("publishTime")
        public long publishTime;
        @SerializedName("cp")
        public int cp;
    }

    public final class AlbumItem {
        @SerializedName("picUrl")
        public String picUrl;
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
    }

    public final class SubscribersItem {
        @SerializedName("birthday")
        public long birthday;
        @SerializedName("detailDescription")
        public String detailDescription;
        @SerializedName("backgroundUrl")
        public String backgroundUrl;
        @SerializedName("gender")
        public int gender;
        @SerializedName("city")
        public int city;
        @SerializedName("signature")
        public String signature;
        @SerializedName("description")
        public String description;
        @SerializedName("remarkName")
        public String remarkName;
        @SerializedName("accountStatus")
        public int accountStatus;
        @SerializedName("avatarImgId")
        public long avatarImgId;
        @SerializedName("defaultAvatar")
        public boolean defaultAvatar;
        @SerializedName("backgroundImgIdStr")
        public String backgroundImgIdStr;
        @SerializedName("avatarImgIdStr")
        public String avatarImgIdSt;
        @SerializedName("province")
        public int province;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("expertTags")
        public Object expertTags;
        @SerializedName("djStatus")
        public int djStatus;
        @SerializedName("avatarUrl")
        public String avatarUrl;
        @SerializedName("authStatus")
        public int authStatus;
        @SerializedName("vipType")
        public int vipType;
        @SerializedName("followed")
        public boolean followed;
        @SerializedName("userId")
        public int userId;
        @SerializedName("mutual")
        public boolean mutual;
        @SerializedName("avatarImgId_str")
        public String avatarImgIdStr;
        @SerializedName("authority")
        public int authority;
        @SerializedName("userType")
        public int userType;
        @SerializedName("backgroundImgId")
        public long backgroundImgId;
        @SerializedName("experts")
        public Object experts;
    }

    public final class Creator {
        @SerializedName("birthday")
        public long birthday;
        @SerializedName("detailDescription")
        public String detailDescription;
        @SerializedName("backgroundUrl")
        public String backgroundUrl;
        @SerializedName("gender")
        public int gender;
        @SerializedName("city")
        public int city;
        @SerializedName("signature")
        public String signature;
        @SerializedName("description")
        public String description;
        @SerializedName("remarkName")
        public String remarkName;
        @SerializedName("accountStatus")
        public int accountStatus;
        @SerializedName("avatarImgId")
        public long avatarImgId;
        @SerializedName("defaultAvatar")
        public boolean defaultAvatar;
        @SerializedName("backgroundImgIdStr")
        public String backgroundImgIdStr;
        @SerializedName("avatarImgIdStr")
        public String avatarImgIdstr;
        @SerializedName("province")
        public int province;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("expertTags")
        public List expertTags;
        @SerializedName("djStatus")
        public int djStatus;
        @SerializedName("avatarUrl")
        public String avatarUrl;
        @SerializedName("authStatus")
        public int authStatus;
        @SerializedName("vipType")
        public int vipType;
        @SerializedName("followed")
        public boolean followed;
        @SerializedName("userId")
        public int userId;
        @SerializedName("mutual")
        public boolean mutual;
        @SerializedName("avatarImgId_str")
        public String avatarImgIdStr;
        @SerializedName("authority")
        public int authority;
        @SerializedName("userType")
        public int userType;
        @SerializedName("backgroundImgId")
        public long backgroundImgId;
    }
}
