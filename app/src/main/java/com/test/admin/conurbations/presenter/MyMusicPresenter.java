package com.test.admin.conurbations.presenter;

import android.app.Activity;

import com.test.admin.conurbations.activitys.IMuMusicContractView;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.user.UserStatus;
import com.test.admin.conurbations.player.playqueue.PlayHistoryLoader;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.download.DownloadLoader;
import com.test.admin.conurbations.utils.download.SongLoader;
import com.test.admin.conurbations.utils.rom.OnlinePlaylistUtils;

import java.util.List;


/**
 * Created by zhouqiong on 2016/12/12.
 */

public class MyMusicPresenter {

    private IMuMusicContractView mView;
    private Activity context;

    public MyMusicPresenter(IMuMusicContractView iMuMusicContractView, Activity context) {
        this.mView = iMuMusicContractView;
        this.context = context;
    }

    /**
     * 更新播放历史
     */
    public void updateHistory() {
        new Thread(() -> {
            List<Music> data = PlayHistoryLoader.getPlayHistory();
            context.runOnUiThread(() -> mView.showHistory(data));
        }).start();
    }

    /**
     * 更新播放历史
     */
    public void updateLocal() {
        new Thread(() -> {
            List<Music> data = SongLoader.getLocalMusic(context, false);
            context.runOnUiThread(() -> mView.showSongs(data));
        }).start();
    }

    /**
     * 更新本地歌单
     */
    public void updateFavorite() {
        new Thread(() -> {
            List<Music> data = SongLoader.getFavoriteSong();
            context.runOnUiThread(() -> mView.showLoveList(data));
        }).start();
    }


    /**
     * 更新本地歌单
     */
    public void updateDownload() {
        new Thread(() -> {
            List<Music> data = DownloadLoader.getDownloadList();
            context.runOnUiThread(() -> mView.showDownloadList(data));
        }).start();
    }


    public void loadSongs() {
        updateLocal();
        updateHistory();
        updateFavorite();
        updateDownload();
    }

    public void loadPlaylist() {
        boolean mIsLogin = UserStatus.getLoginStatus();
        if (mIsLogin) {
            OnlinePlaylistUtils.INSTANCE.getOnlinePlaylist(newsLists -> {
                mView.showPlaylist(newsLists);
                return null;
            }, s -> {
                ToastUtils.getInstance().showToast(s);

                if (OnlinePlaylistUtils.INSTANCE.getPlaylists().size() == 0) {
                    mView.showError(s, true);
                }
                return null;
            });
        } else {
            OnlinePlaylistUtils.INSTANCE.getPlaylists().clear();
            mView.showPlaylist(OnlinePlaylistUtils.INSTANCE.getPlaylists());
        }
    }
}
