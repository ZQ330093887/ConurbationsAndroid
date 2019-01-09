package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.NewsItem;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface INBAInfoView extends BaseViewImpl {

    void setCacheData(NewsItem nbaInfoData);

    void setNBAInfoData(NewsItem nbaInfoData);
}
