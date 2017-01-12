package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.ISouGouImageList;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.model.NetImage;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class DayAndDayImagePresenter extends BasePresenter {

    private ISouGouImageList souGouImageList;

    public DayAndDayImagePresenter(ISouGouImageList souGouImageList) {
        this.souGouImageList = souGouImageList;
    }

    public void getWelfareData(String imgType, int pages) {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getImageList("ajax", "result", imgType, pages * 24),
                new ApiCallback<NetImage>() {

                    @Override
                    public void onSuccess(NetImage model) {
                        souGouImageList.setSouGouImage(model);
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
