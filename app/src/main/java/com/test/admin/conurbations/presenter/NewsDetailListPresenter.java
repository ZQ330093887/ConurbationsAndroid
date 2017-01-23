package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.INewsDetaliListView;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.model.NewsDetail;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class NewsDetailListPresenter extends BasePresenter {

    private INewsDetaliListView iNewsDetaliListView;

    public NewsDetailListPresenter(INewsDetaliListView iNewsDetaliListView) {
        this.iNewsDetaliListView = iNewsDetaliListView;
    }

    public void getNewsDetail(int cid) {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getNewsDetail(cid),
                new ApiCallback<NewsDetail>() {
                    @Override
                    public void onSuccess(NewsDetail model) {
                        iNewsDetaliListView.setSearchData(model);
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
