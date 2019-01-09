package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.NewsList;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface INewInformationView extends BaseViewImpl {
    void setNewInfoData(List<NewsList> result);
}
