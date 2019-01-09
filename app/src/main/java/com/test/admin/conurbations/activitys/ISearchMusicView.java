package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.HotSearchBean;
import com.test.admin.conurbations.model.entity.SearchHistoryBean;
import com.test.admin.conurbations.utils.download.TasksManagerModel;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface ISearchMusicView {

    void showSearchResult(List<Music> list);

    void showHotSearchInfo(List<HotSearchBean> modelList);

    void showSearchHistory(List<SearchHistoryBean> modelList);
}
