package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class GanHuoPresenter extends BasePresenter {

    private IWelfareView welfareList;

    public GanHuoPresenter(IWelfareView welfareList) {
        this.welfareList = welfareList;
    }

    public void getWelfareData(final String type, final int pager) {

        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getGanHuo(type, pager),
                new ApiCallback<GankData>() {
                    @Override
                    public void onSuccess(GankData model) {
                        welfareList.setWelfareData(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                    }

                    @Override
                    public void onFinish() {
                    }
                });

    }
}
