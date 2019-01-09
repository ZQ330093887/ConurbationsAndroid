package com.test.admin.conurbations.model.entity;

/**
 * Created by ZQiong on 2018/12/10.
 */
public class StatusChangedEvent {

    public boolean isPrepared;
    public boolean isPlaying;

    public StatusChangedEvent(boolean isPrepared, boolean isPlaying) {
        this.isPrepared = isPrepared;
        this.isPlaying = isPlaying;
    }
}
