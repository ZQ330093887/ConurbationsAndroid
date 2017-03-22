package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.INewsInfoDetailListView;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class NewsInfoListDetailPresenter extends BasePresenter {

    private INewsInfoDetailListView iNewsInfoDetailListView;

    public NewsInfoListDetailPresenter(INewsInfoDetailListView iNewsInfoDetailListView) {
        this.iNewsInfoDetailListView = iNewsInfoDetailListView;
    }

    public void getNewsInfoDetailData(int cid) {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getNewsDetail(cid),
                new ApiCallback<NewsDetail>() {
                    @Override
                    public void onSuccess(NewsDetail model) {
                        iNewsInfoDetailListView.setNewsInfoDetailData(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.d("msd", msg);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }
}
