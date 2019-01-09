package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.MvInfo;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IPlayListDetailView {
    void showPlaylistSongs(List<Music> songList);

    void removeMusic(int position);

    void success(int type);
}
