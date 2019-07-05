package com.test.admin.conurbations.activitys;


import com.test.admin.conurbations.model.entity.TTNews;

import java.util.List;

public interface lNewsListView extends BaseViewImpl {

    void setCacheData(List<TTNews> newList);

    void onGetNewsListSuccess(List<TTNews> newList);
}
