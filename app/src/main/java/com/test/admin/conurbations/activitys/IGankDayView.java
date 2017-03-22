package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.response.GankItem;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IGankDayView {
    void setGankDayData(List<GankItem> todayData);
}
