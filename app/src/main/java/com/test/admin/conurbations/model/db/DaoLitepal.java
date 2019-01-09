package com.test.admin.conurbations.model.db;

import android.database.Cursor;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.Album;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.model.entity.SearchHistoryBean;
import com.test.admin.conurbations.utils.FileUtils;
import com.test.admin.conurbations.utils.download.TasksManagerModel;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class DaoLitepal {

    public static Music getMusicInfo(String mid) {
        return LitePal.where("mid = ? ", mid).findFirst(Music.class);
    }

    public static NewsList getPlaylist(String pid) {
        return LitePal.where("pid = ?", pid).findFirst(NewsList.class);
    }

    public static List<Music> searchLocalMusic(String info) {
        return LitePal.where("title LIKE ? or artist LIKE ? or album LIKE ?", "%" + info + "%", "%" + info + "%", "%" + info + "%").find(Music.class);
    }

    /**
     * 获取搜索历史
     */
    public static List<SearchHistoryBean> getAllSearchInfo(String title) {
        if (title == null) {
            return LitePal.order("id desc").find(SearchHistoryBean.class);
        } else {
            return LitePal.where("title like ?", "%" + title + "%").order("id desc").find(SearchHistoryBean.class);
        }
    }


    /**
     * 增加搜索
     */
    public static void addSearchInfo(String info) {
        long id = System.currentTimeMillis();
        SearchHistoryBean queryInfo = new SearchHistoryBean(id, info);
        queryInfo.saveOrUpdate("title = ?", info);
    }

    /**
     * 删除搜索历史
     */
    public static void deleteSearchInfo(String info) {
        LitePal.deleteAll(SearchHistoryBean.class, "title = ? ", info);
    }

    /**
     * 删除所有搜索历史
     */
    public static void clearAllSearch() {
        LitePal.deleteAll(SearchHistoryBean.class);
    }


    public static List<Music> getMusicList(String pid, String order) {
        List<Music> musicLists = new ArrayList<>();
        if (pid.equals(Constants.PLAYLIST_LOVE_ID)) {
            List<Music> data = LitePal.where("isLove = ? ", "1").find(Music.class);
            musicLists.addAll(data);
        } else if (pid.equals(Constants.PLAYLIST_LOCAL_ID)) {
            List<Music> data = LitePal.where("isOnline = ? ", "0").find(Music.class);
            musicLists.addAll(data);
        } else {
            List<MusicToPlaylist> data = LitePal.where("pid = ?", pid).order(order).find(MusicToPlaylist.class);
            for (MusicToPlaylist it : data) {
                List<Music> musicList = LitePal.where("mid = ?", it.mid).find(Music.class);
                musicLists.addAll(musicList);
            }
        }
        return musicLists;
    }

    /*
     **********************************
     * 播放歌单操作
     **********************************
     */
    public static void saveOrUpdateMusic(Music music, Boolean isAsync) {
        if (isAsync) {
            music.saveOrUpdateAsync("mid = ?", music.mid);
        } else {
            music.saveOrUpdate("mid = ?", music.mid);
        }
    }

    public static void addToPlaylist(Music music, String pid) {
        saveOrUpdateMusic(music, false);
        int count = LitePal.where("mid = ? and pid = ?", music.mid, pid).count(MusicToPlaylist.class);

        if (count == 0) {
            MusicToPlaylist mtp = new MusicToPlaylist();
            mtp.mid = music.mid;
            mtp.pid = pid;
            mtp.total = 1L;
            mtp.createDate = System.currentTimeMillis();
            mtp.updateDate = System.currentTimeMillis();
            mtp.save();
        } else {
            MusicToPlaylist mtp = new MusicToPlaylist();
            mtp.total++;
            mtp.updateDate = System.currentTimeMillis();
            mtp.saveOrUpdate("mid = ? and pid =?", music.mid, pid);
        }
    }


    /**
     * 删除本地歌曲（Music、MusicToPlaylist）
     */
    public static void deleteMusic(Music music) {
        String cachePath = FileUtils.getMusicCacheDir() + music.artist + " - " + music.title + "(" + music.quality + ")";
        String downloadPath = FileUtils.getMusicDir() + music.artist + " - " + music.title + ".mp3";
        if (FileUtils.exists(cachePath)) {
            FileUtils.delFile(cachePath);
        }
        if (FileUtils.exists(downloadPath)) {
            FileUtils.delFile(downloadPath);
        }
        if (FileUtils.exists(music.uri)) {
            FileUtils.delFile(music.uri);
        }
        LitePal.deleteAll(Music.class, "mid = ? ", music.mid);
        LitePal.deleteAll(TasksManagerModel.class, "mid = ?", music.mid);
        LitePal.deleteAll(MusicToPlaylist.class, "mid = ?", music.mid);
    }

    public static void clearPlaylist(String pid) {
        LitePal.deleteAll(MusicToPlaylist.class, "pid=?", pid);
    }

    public static boolean saveOrUpdatePlaylist(NewsList playlist) {
        playlist.updateDate = System.currentTimeMillis();
        return playlist.saveOrUpdate("pid = ?", playlist.pid);
    }

    public static List<Album> updateAlbumList() {
        String sql = "SELECT music.albumid,music.album,music.artistid,music.artist,count(music.title) as num FROM music WHERE music.isonline=0 and music.type=\"local\" GROUP BY music.album";
        Cursor cursor = LitePal.findBySQL(sql);
        List<Album> results = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Album album = new MusicCursorWrapper(cursor).getAlbum();
                album.saveOrUpdate("albumId = ?", album.albumId);

                results.add(album);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
        return results;
    }

    public static List<Artist> updateArtistList() {
        String sql = "SELECT music.artistid,music.artist,count(music.title) as num FROM music where music.isonline=0 and music.type=\"local\" GROUP BY music.artist";
        Cursor cursor = LitePal.findBySQL(sql);
        List<Artist> results = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Artist artist = new MusicCursorWrapper(cursor).getArtists();
                artist.saveOrUpdate("artistId = ?", artist.artistId);
                results.add(artist);
            }
        }
        // 记得关闭游标
        if (cursor != null) {
            cursor.close();
        }
        return results;
    }
}
