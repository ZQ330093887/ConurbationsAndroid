package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class AlbumSongList {
    @SerializedName("is_collect")
    public int isCollect;
    @SerializedName("share_pic")
    public String sharePic;
    @SerializedName("albumInfo")
    public AlbumDetailInfo albumInfo;
    @SerializedName("share_title")
    public String shareTitle;
    @SerializedName("songlist")
    public List<BaiduMusicList.SongListItem> songlist;


    public class AlbumDetailInfo {
        @SerializedName("comment_num")
        public int commentNum;
        @SerializedName("country")
        public String country;
        @SerializedName("pic_s1000")
        public String picS;
        @SerializedName("resource_type_ext")
        public String resourceTypeExt;
        @SerializedName("gender")
        public String gender;
        @SerializedName("language")
        public String language;
        @SerializedName("collect_num")
        public int collectNum;
        @SerializedName("title")
        public String title;
        @SerializedName("hot")
        public String hot;
        @SerializedName("pic_big")
        public String picBig;
        @SerializedName("listen_num")
        public String listenNum;
        @SerializedName("price")
        public String price;
        @SerializedName("favorites_num")
        public int favoritesNum;
        @SerializedName("info")
        public String info;
        @SerializedName("share_num")
        public int shareNum;
        @SerializedName("area")
        public String area;
        @SerializedName("ai_presale_flag")
        public String aiPresaleFlag;
        @SerializedName("pic_radio")
        public String picRadio;
        @SerializedName("my_num")
        public int myNum;
        @SerializedName("author")
        public String author;
        @SerializedName("pic_s500")
        public String picS5;
        @SerializedName("all_artist_id")
        public String allArtistId;
        @SerializedName("buy_url")
        public String buyUrl;
        @SerializedName("pic_small")
        public String picSmall;
        @SerializedName("publishcompany")
        public String publishcompany;
        @SerializedName("all_artist_ting_uid")
        public String allArtistTingUid;
        @SerializedName("artist_id")
        public String artistId;
        @SerializedName("song_sale")
        public int songSale;
        @SerializedName("songs_total")
        public String songsTotal;
        @SerializedName("publishtime")
        public String publishtime;
        @SerializedName("recommend_num")
        public int recommendNum;
        @SerializedName("artist_ting_uid")
        public String artistTingUid;
        @SerializedName("album_id")
        public String albumId;
    }
}
