package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/11/30.
 */
public class BaiduMusicList {
    @SerializedName("song_list")
    public List<SongListItem> songList;
    @SerializedName("error_code")
    public int errorCode;
    @SerializedName("billboard")
    public Billboard billboard;


    public static class Billboard {
        @SerializedName("pic_s210")
        public String picS;
        @SerializedName("billboard_type")
        public int billboardType;
        @SerializedName("web_url")
        public String webUrl;
        @SerializedName("pic_s640")
        public String picS640;
        @SerializedName("pic_s444")
        public String picS444;
        @SerializedName("havemore")
        public int havemore;
        @SerializedName("name")
        public String name;
        @SerializedName("billboard_no")
        public String billboardNo;
        @SerializedName("comment")
        public String comment;
        @SerializedName("pic_s192")
        public String picS192;
        @SerializedName("update_date")
        public String updateDate;
        @SerializedName("pic_s260")
        public String picS260;
    }

    public static class SongListItem {
        @SerializedName("piao_id")
        public String piaoId;
        @SerializedName("resource_type_ext")
        public String resourceTypeExt;
        @SerializedName("mv_provider")
        public String mvProvider;
        @SerializedName("artist_name")
        public String artistName;
        @SerializedName("biaoshi")
        public String biaoshi;
        @SerializedName("is_first_publish")
        public int isFirstPublish;
        @SerializedName("del_status")
        public String delStatus;
        @SerializedName("album_1000_1000")
        public String pic1000;
        @SerializedName("korean_bb_song")
        public String koreanBbSong;
        @SerializedName("title")
        public String title;
        @SerializedName("pic_big")
        public String picBig;
        @SerializedName("pic_huge")
        public String picHuge;
        @SerializedName("all_rate")
        public String allRate;
        @SerializedName("song_source")
        public String songSource;
        @SerializedName("song_id")
        public String songId;
        @SerializedName("album_500_500")
        public String album500;
        @SerializedName("havehigh")
        public int havehigh;
        @SerializedName("rank")
        public String rank;
        @SerializedName("pic_premium")
        public String picPremium;
        @SerializedName("album_800_800")
        public String album800;
        @SerializedName("info")
        public String info;
        @SerializedName("si_proxycompany")
        public String siProxycompany;
        @SerializedName("has_mv_mobile")
        public int hasMvMobile;
        @SerializedName("charge")
        public int charge;
        @SerializedName("pic_radio")
        public String picRadio;
        @SerializedName("learn")
        public int learn;
        @SerializedName("author")
        public String author;
        @SerializedName("pic_s500")
        public String picS;
        @SerializedName("all_artist_id")
        public String allArtistId;
        @SerializedName("resource_type")
        public String resourceType;
        @SerializedName("has_filmtv")
        public String hasFilmtv;
        @SerializedName("pic_small")
        public String picSmall;
        @SerializedName("has_mv")
        public int hasMv;
        @SerializedName("bitrate_fee")
        public String bitrateFee;
        @SerializedName("all_artist_ting_uid")
        public String allArtistTingUid;
        @SerializedName("artist_id")
        public String artistId;
        @SerializedName("high_rate")
        public String highRate;
        @SerializedName("versions")
        public String versions;
        @SerializedName("album_id")
        public String albumId;
        @SerializedName("copy_type")
        public String copyType;
        @SerializedName("ting_uid")
        public String tingUid;
        @SerializedName("album_title")
        public String albumTitle;

    }
}
