package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.DouyinVideoListData;

/**
 * Created by zhouqiong on 2019/4/2.
 */

public interface IVideoInfoView extends BaseViewImpl {

    void setCacheData(DouyinVideoListData douYinData);

    void setVideoDouYinData(DouyinVideoListData douYinData);
}
