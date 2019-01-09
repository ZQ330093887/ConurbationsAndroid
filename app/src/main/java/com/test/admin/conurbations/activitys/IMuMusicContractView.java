package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.model.entity.NewsList;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IMuMusicContractView {

    void showSongs(List<Music> songList);

    void showPlaylist(List<NewsList> playlists);

    void showHistory(List<Music> musicList);

    void showLoveList(List<Music> musicList);

    void showDownloadList(List<Music> musicList);

    //隐藏进度
    void showError(String message, boolean showRetryButton);
}
