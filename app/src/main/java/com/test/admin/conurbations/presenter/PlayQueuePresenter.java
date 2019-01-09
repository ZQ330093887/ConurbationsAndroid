package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.fragments.IPlayQueueContract;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.PlayManager;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class PlayQueuePresenter {
    private IPlayQueueContract mvpView;

    public PlayQueuePresenter(IPlayQueueContract mvpView) {
        this.mvpView = mvpView;
    }

    public void loadSongs() {
        List<Music> musicList = PlayManager.getPlayList();
        mvpView.showSongs(musicList);
    }

    public void clearQueue() {
        PlayManager.clearQueue();
    }
}
