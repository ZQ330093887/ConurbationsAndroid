package com.test.admin.conurbations.player.playqueue;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.db.DaoLitepal;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/10.
 */
public class PlayHistoryLoader {


    private String TAG = "PlayQueueLoader";

    /**
     * 添加歌曲到播放历史
     */
    public static void addSongToHistory(Music music) {
        try {
            DaoLitepal.addToPlaylist(music, Constants.PLAYLIST_HISTORY_ID);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取播放历史
     */
    public static List<Music> getPlayHistory() {
        return DaoLitepal.getMusicList(Constants.PLAYLIST_HISTORY_ID, "updateDate desc");
    }

    /**
     * 清除播放历史
     */
    public static void clearPlayHistory() {
        try {
            DaoLitepal.clearPlaylist(Constants.PLAYLIST_HISTORY_ID);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
