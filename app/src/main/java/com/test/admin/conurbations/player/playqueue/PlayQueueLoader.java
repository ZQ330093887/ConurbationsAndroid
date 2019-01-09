package com.test.admin.conurbations.player.playqueue;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.db.DaoLitepal;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class PlayQueueLoader {

    /**
     * 获取播放队列
     */
    public static List<Music> getPlayQueue() {
        return DaoLitepal.getMusicList(Constants.PLAYLIST_QUEUE_ID, "");
    }

    /**
     * 添加歌曲到歌单
     */
    public static void updateQueue(List<Music> musics) {
        clearQueue();
        //这里应该等到clearQueue执行完在做操作
        for (Music it : musics) {
            DaoLitepal.addToPlaylist(it, Constants.PLAYLIST_QUEUE_ID);
        }
    }

    /**
     * 清空播放列表
     */
    public static void clearQueue() {
        DaoLitepal.clearPlaylist(Constants.PLAYLIST_QUEUE_ID);
    }

}
