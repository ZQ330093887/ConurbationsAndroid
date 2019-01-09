package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.response.NetImage;

/**
 * Created by zhouqiong on 2017/1/12.
 */

public interface ISouGouImageView extends BaseViewImpl {

    void setCacheData(NetImage imageData);

    void setSouGouImageData(NetImage imageData);
}
