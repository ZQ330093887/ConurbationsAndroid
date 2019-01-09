package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.response.NetImage360;

/**
 * Created by zhouqiong on 2017/1/12.
 */

public interface IPrettyPictureListView extends BaseViewImpl {

    void setCacheData(NetImage360 image360);

    void setPrettyPictureData(NetImage360 image360);
}
