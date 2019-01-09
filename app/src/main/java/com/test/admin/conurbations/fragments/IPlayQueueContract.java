package com.test.admin.conurbations.fragments;

import com.test.admin.conurbations.model.Music;

import java.util.List;

/**
 * Created by ZQiong on 2018/12/13.
 */
public interface IPlayQueueContract {
    void showSongs(List<Music> songs);
}
