package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.BannerBean;
import com.test.admin.conurbations.model.entity.NewsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IDiscoverView {

    void showBannerView(List<BannerBean> banners);

    void showEmptyView(String msg);

    void showNeteaseCharts(List<NewsList> charts);

    void showArtistCharts(ArrayList<Artist> musicList);

    void showRadioChannels(ArrayList<NewsList> musicList);
}
