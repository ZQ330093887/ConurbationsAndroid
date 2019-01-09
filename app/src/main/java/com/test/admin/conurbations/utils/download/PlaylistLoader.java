package com.test.admin.conurbations.utils.download;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.db.DaoLitepal;
import com.test.admin.conurbations.model.entity.NewsList;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class PlaylistLoader {

    /**
     * 扫描歌单歌曲
     */
    public static List<Music> getMusicForPlaylist(String pid, String order) {
        if (order == null) {
            return DaoLitepal.getMusicList(pid, "");
        } else {
            return DaoLitepal.getMusicList(pid, order);
        }
    }

    /**
     * 新增歌单
     *
     * @param name
     * @return
     */
    public static boolean createDefaultPlaylist(String pid, String name) {
        NewsList playlist = new NewsList();
        playlist.pid = pid;
        playlist.date = System.currentTimeMillis();
        playlist.updateDate = System.currentTimeMillis();
        playlist.name = name;
        playlist.type = pid;
        if (!pid.equals(Constants.PLAYLIST_QUEUE_ID))
            playlist.order = "updateDate desc";
        return DaoLitepal.saveOrUpdatePlaylist(playlist);
    }

    public static NewsList getHistoryPlaylist() {
        return DaoLitepal.getPlaylist(Constants.PLAYLIST_HISTORY_ID);
    }

    public static NewsList getFavoritePlaylist() {
        return DaoLitepal.getPlaylist(Constants.PLAYLIST_LOVE_ID);
    }

}
