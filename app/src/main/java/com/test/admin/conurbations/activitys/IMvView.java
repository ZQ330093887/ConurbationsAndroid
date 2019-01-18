package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.model.response.GankData;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IMvView extends BaseViewImpl {
    void setCacheData(List<MvInfo.MvInfoDetail> welfareData);

    void showMvList(List<MvInfo.MvInfoDetail> mvList);
}
