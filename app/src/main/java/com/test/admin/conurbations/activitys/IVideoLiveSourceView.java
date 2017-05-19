package com.test.admin.conurbations.activitys;

import android.content.Context;

import com.test.admin.conurbations.model.entity.VideoLiveSource;

import java.util.List;

/**
 * Created by zhouqiong on 2017/1/12.
 */

public interface IVideoLiveSourceView {
    void setVideoLiveData(List<VideoLiveSource> videoLiveData, Context context);
}
