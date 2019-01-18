package com.test.admin.conurbations.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.MusicInfo;
import com.test.admin.conurbations.model.entity.Album;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.NeteasePlaylistDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQiong on 2018/11/29.
 */
public class MusicUtils {


    /**
     * 分享到QQ
     */
    public static void qqShare(Activity activity, Music music) {
        if (music == null) {
            ToastUtils.getInstance().showToast("暂无音乐播放!");
            return;
        }
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        String stringBuilder = activity.getString(R.string.share_content) +
                activity.getString(R.string.share_song_content, music.artist, music.title);
        textIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder);
        activity.startActivity(Intent.createChooser(textIntent, "歌曲分享"));
    }

    /**
     * 在线歌曲实体类转化成本地歌曲实体
     */

    /**
     * 在线歌单歌曲歌曲实体类转化成本地歌曲实体(即可)
     * (网易云歌曲)
     */
    public static ArrayList<Music> getMusicList(List<MusicInfo> musicInfo, String type) {
        ArrayList<Music> musicList = new ArrayList<>();

        if (musicInfo != null && musicInfo.size() > 0) {
            for (MusicInfo it : musicInfo) {
                Music music = new Music();

                if (it.id != null) {
                    music.mid = it.id;
                }
                music.title = it.name;
                music.type = type;
                music.isOnline = true;
                music.album = it.album.name;
                music.albumId = it.album.id;
                music.isCp = it.cp;
                music.isDl = it.dl;
                if (it.quality != null) {
                    music.sq = it.quality.sq;
                    music.hq = it.quality.hq;
                    music.high = it.quality.high;
                }
                if (it.artists != null) {
                    StringBuilder artistIds = new StringBuilder(it.artists.get(0).id);
                    StringBuilder artistNames = new StringBuilder(it.artists.get(0).name);
                    for (int i = 1; i < it.artists.size() - 1; i++) {
                        artistIds.append(",").append(it.artists.get(i).id);
                        artistNames.append(",").append(it.artists.get(i).name);
                    }

                    music.artist = artistNames.toString();
                    music.artistId = artistIds.toString();
                }
                music.coverUri = getAlbumPic(it.album.cover, type, 150);
                music.coverBig = it.album.cover;
                music.coverSmall = getAlbumPic(it.album.cover, type, 90);
                if (!it.cp) {
                    musicList.add(music);
                }
            }
        }

        return musicList;
    }

    public static List<Music> getNeteaseMusicList(List<NeteasePlaylistDetail.TracksItem> tracks) {
        List<Music> musicList = new ArrayList<>();
        for (NeteasePlaylistDetail.TracksItem it : tracks) {
            Music music = new Music();
            if (!TextUtils.isEmpty(it.id)) {
                music.mid = it.id;
            }
            music.title = it.name;
            music.type = Constants.NETEASE;
            music.album = it.album.name;
            music.isOnline = true;
            music.albumId = String.valueOf(it.album.id);
            if (it.artists != null) {
                StringBuilder artistIds = new StringBuilder(String.valueOf(it.artists.get(0).id));
                StringBuilder artistNames = new StringBuilder(it.artists.get(0).name);
                for (int i = 1; i < it.artists.size() - 1; i++) {
                    artistIds.append(",").append(it.artists.get(i).id);
                    artistNames.append(",").append(it.artists.get(i).name);
                }

                music.artist = artistNames.toString();
                music.artistId = artistIds.toString();
            }
            music.coverUri = getAlbumPic(it.album.picUrl, Constants.NETEASE, 150);
            music.coverBig = it.album.picUrl;
            music.coverSmall = getAlbumPic(it.album.picUrl, Constants.NETEASE, 90);
            if (it.cp != 0) {
                musicList.add(music);
            }
        }

        return musicList;
    }

    /**
     * 在线歌单歌曲歌曲实体类转化成本地歌曲实体
     */
    public static Music getMusic(MusicInfo musicInfo) {
        Music music = new Music();
        if (musicInfo.songId != null) {
            music.mid = musicInfo.songId;
        } else if (musicInfo.id != null) {
            music.mid = musicInfo.id;
        }
        music.collectId = musicInfo.id;
        music.title = musicInfo.name;
        music.isOnline = true;
        music.type = musicInfo.vendor;
        music.album = musicInfo.album.name;
        music.albumId = musicInfo.album.id;
        music.isCp = musicInfo.cp;
        music.isDl = musicInfo.dl;
        if (musicInfo.quality != null) {
            music.sq = musicInfo.quality.sq;
            music.hq = musicInfo.quality.hq;
            music.high = musicInfo.quality.high;
        }
        if (musicInfo.artists != null) {
            StringBuilder artistIds = new StringBuilder(musicInfo.artists.get(0).id);
            StringBuilder artistNames = new StringBuilder(musicInfo.artists.get(0).name);
            for (int i = 1; i < musicInfo.artists.size() - 1; i++) {
                artistIds.append(",").append(musicInfo.artists.get(i).id);
                artistNames.append(",").append(musicInfo.artists.get(i).name);
            }
            music.artist = artistNames.toString();
            music.artistId = artistIds.toString();
        }
        music.coverUri = getAlbumPic(musicInfo.album.cover, musicInfo.vendor, 150);
        music.coverBig = musicInfo.album.cover;
        music.coverSmall = getAlbumPic(musicInfo.album.cover, musicInfo.vendor, 90);
        return music;
    }

    /**
     * 根据不同的歌曲类型生成不同的图片
     */
    public static String getAlbumPic(String url, String type, int width) {
        String newUrl;
        switch (type) {
            case Constants.QQ:
                newUrl = url.replace("300x300", width + "x" + width);
                break;
            case Constants.XIAMI:
                newUrl = url + "@1e_1c_100Q_" + width + "w_" + width + "h";
                break;
            case Constants.NETEASE:
                newUrl = url + "param=" + width + "y" + width;
                break;
            case Constants.BAIDU:
                newUrl = url + "@s_1,w_" + width + ",h_" + width;
                break;
            default:
                newUrl = url;
                break;
        }
        return newUrl;
    }

    /**
     * 获取歌手名
     */
    public static Artist getArtistInfo(Music music) {

        String[] artistIds = null;
        if (music.artistId != null) {
            artistIds = music.artistId.split(",");
        }

        String[] artistNames = null;
        if (music.artist != null) {
            artistNames = music.artist.split(",");
        }

        List<Artist> artists = new ArrayList<>();

        if (artistNames != null && (artistIds != null ? artistIds.length : 0) == artistNames.length) {
            for (int i = 0; i < artistNames.length; i++) {
                Artist artist = new Artist();
                artist.artistId = artistIds[i];
                artist.name = artistNames[i];
                artist.type = music.type;
                artists.add(artist);
            }
        }


        if (artists.size() > 0) {
            return artists.get(0);
        }
        return null;
    }

    /**
     * 本地歌曲实体转化成在线歌单歌曲实体
     */
    public static MusicInfo getMusicInfo(Music music) {
        String[] artistIds = null;
        if (!TextUtils.isEmpty(music.artistId)) {
            artistIds = music.artistId.split(",");
        }

        String[] artists = null;
        if (!TextUtils.isEmpty(music.artist)) {
            artists = music.artist.split(",");
        }

        ArrayList<MusicInfo.ArtistsItem> artistsBeans = new ArrayList<>();

        if (artists != null && artists.length > 0) {
            for (int i = 0; i < artists.length; i++) {
                if (artistIds != null && artistIds.length > 0) {
                    if (artistIds[i] != null) {
                        MusicInfo.ArtistsItem artistsItem = new MusicInfo.ArtistsItem();
                        artistsItem.id = artistIds[i];
                        artistsItem.name = artists[i];
                        artistsBeans.add(artistsItem);
                    }
                }
            }
        }
        MusicInfo.Album album = new MusicInfo.Album();
        album.id = music.albumId;
        album.name = music.album;
        album.cover = music.coverUri;

        if (music.type.equals(Constants.BAIDU)) music.isCp = false;
        return new MusicInfo(music.mid, music.mid, music.title, artistsBeans, album, music.type, music.isCp, music.isDl, new MusicInfo.QualityBean(music.hq, music.sq, music.high));
    }


    /**
     * 分享链接
     */
    public static void openBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }


}
