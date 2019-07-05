package com.test.admin.conurbations.presenter;


import com.google.gson.Gson;
import com.test.admin.conurbations.activitys.lNewsListView;
import com.test.admin.conurbations.model.entity.NewsData;
import com.test.admin.conurbations.model.entity.NewsResponse;
import com.test.admin.conurbations.model.entity.TTNews;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.PrefUtils;
import com.test.admin.conurbations.widget.SolidApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2019/7/4.
 */

public class NewsListPresenter extends BasePresenter<lNewsListView> {

    private long lastTime;

    @Inject
    public NewsListPresenter() {
    }

    public void getNewsList(String channelCode) {
        lastTime = PrefUtils.getLong(SolidApplication.getInstance(), channelCode, 0);//读取对应频道下最后一次刷新的时间戳
        if (lastTime == 0) {
            //如果为空，则是从来没有刷新过，使用当前时间戳
            lastTime = System.currentTimeMillis() / 1000;
        }

        addSubscription(apiStores.getNewsList(channelCode, lastTime, System.currentTimeMillis() / 1000),
                new ApiCallback<NewsResponse>() {
                    @Override
                    public void onSuccess(NewsResponse response) {
                        lastTime = System.currentTimeMillis() / 1000;
                        PrefUtils.putLong(SolidApplication.getInstance(), channelCode, lastTime);//保存刷新的时间戳

                        List<NewsData> data = response.data;
                        List<TTNews> newsList = new ArrayList<>();
                        if (data != null && data.size() > 0) {
                            for (NewsData newsData : data) {
                                TTNews news = new Gson().fromJson(newsData.content, TTNews.class);
                                newsList.add(news);
                            }
                        }
                        mvpView.onGetNewsListSuccess(newsList);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.showError(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.showFinishState();
                    }
                });
    }
}
