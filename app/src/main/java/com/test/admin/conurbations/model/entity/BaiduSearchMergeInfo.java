package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/20.
 */
public class BaiduSearchMergeInfo {
    @SerializedName("result")
    public Result result;
    @SerializedName("error_code")
    public int errorCode;


    public class Result {
        @SerializedName("artist_info")
        public ArtistInfo artistInfo;
        @SerializedName("syn_words")
        public String synWords;
        @SerializedName("user_info")
        public UserInfo userInfo;
        @SerializedName("album_info")
        public AlbumInfo albumInfo;
        @SerializedName("song_info")
        public SongInfoRes songInfo;
        @SerializedName("tag_info")
        public TagInfo tagInfo;
        @SerializedName("query")
        public String query;
        @SerializedName("playlist_info")
        public PlaylistInfo playlistInfo;
        @SerializedName("rqt_type")
        public int rqtType;
        @SerializedName("video_info")
        public VideoInfoData videoInfo;
        @SerializedName("topic_info")
        public TopicInfo topicInfo;
    }


    public class ArtistInfo {
        @SerializedName("total")
        public int total;
        @SerializedName("artist_list")
        public List<ArtistListItem> artistList;
    }

    public class UserInfo {
        @SerializedName("total")
        public int total;
    }

    public class TopicInfo {
        @SerializedName("total")
        public int total;
    }

    public class VideoInfoData {
        @SerializedName("total")
        public int total;
    }

    public class TagInfo {
        @SerializedName("total")
        public int total;
    }

    public class AlbumInfo {
        @SerializedName("album_list")
        public List<AlbumListItem> albumList;
        @SerializedName("total")
        public int total;
    }

    public class SongInfoRes {
        @SerializedName("song_list")
        public List<BaiduMusicList.SongListItem> songList;
        @SerializedName("total")
        public int total;
    }


    public class ArtistListItem {
        @SerializedName("country")
        public String country;
        @SerializedName("song_num")
        public int songNum;
        @SerializedName("album_num")
        public int albumNum;
        @SerializedName("author")
        public String author;
        @SerializedName("avatar_middle")
        public String avatarMiddle;
        @SerializedName("ting_uid")
        public String tingUid;
        @SerializedName("artist_desc")
        public String artistDesc;
        @SerializedName("artist_source")
        public String artistSource;
        @SerializedName("artist_id")
        public String artistId;
    }


    public class AlbumListItem {
        @SerializedName("resource_type_ext")
        public String resourceTypeExt;
        @SerializedName("author")
        public String author;
        @SerializedName("all_artist_id")
        public String allArtistId;
        @SerializedName("publishtime")
        public String publishtime;
        @SerializedName("album_desc")
        public String albumDesc;
        @SerializedName("company")
        public String company;
        @SerializedName("album_id")
        public String albumId;
        @SerializedName("pic_small")
        public String picSmall;
        @SerializedName("title")
        public String title;
        @SerializedName("hot")
        public int hot;
        @SerializedName("artist_id")
        public String artistId;

    }


}
