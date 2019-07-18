package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.DouyinVideoListData;

/**
 * Created by zhouqiong on 2019/4/2.
 */

public interface IVideoDetailView {

    void setVideoDouYinData(DouyinVideoListData douYinData);

    void showError(String msg);
}
