package com.test.admin.conurbations.model.entity;

/**
 * Created by ZQiong on 2018/12/10.
 */
public class PlaylistEvent {
    public String type;
    public NewsList playlist;

    public PlaylistEvent(String type, NewsList playlist) {
        this.type = type;
        this.playlist = playlist;
    }
}
