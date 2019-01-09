package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.response.GankData;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IWelfareView extends BaseViewImpl {

    void setCacheData(GankData welfareData);

    void setWelfareData(GankData welfareData);
}
