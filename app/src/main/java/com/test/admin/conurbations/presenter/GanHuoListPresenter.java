package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IWelfareList;
import com.test.admin.conurbations.data.entity.Moment;
import com.test.admin.conurbations.data.response.GankData;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class GanHuoListPresenter extends BasePresenter {

    private IWelfareList welfareList;

    public GanHuoListPresenter(IWelfareList welfareList) {
        this.welfareList = welfareList;
    }

    public void getWelfareData(final Moment.Type type, final int pager) {

        addSubscription(AppClient.retrofit().create(GankService.class)
                .getGanHuo(String.valueOf(type), pager),
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
