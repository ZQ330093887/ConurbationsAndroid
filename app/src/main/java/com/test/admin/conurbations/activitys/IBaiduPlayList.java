package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.MvInfo;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IBaiduPlayList {
    void showOnlineMusicList(List<Music> musicList);
}
