package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.MvInfo;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IMvDetail extends BaseViewImpl{


    void showMvList(List<MvInfo.MvInfoDetail> mvList);

    void showMvDetailInfo(MvInfo.MvInfoDetailInfo mvInfoDetailInfo);

}
