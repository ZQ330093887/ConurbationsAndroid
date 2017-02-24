package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.IDayAndDayPrettyPictureMoreView;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.model.NetImage360;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class DayAndDayPrettyPicturesMorePresenter extends BasePresenter {

    private IDayAndDayPrettyPictureMoreView iDayAndDayPrettyPictureMoreView;

    public DayAndDayPrettyPicturesMorePresenter(IDayAndDayPrettyPictureMoreView iTestList) {
        this.iDayAndDayPrettyPictureMoreView = iTestList;
    }

    public void getDayAndDayPrettyPicturesMoreInfo(String cid) {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .get360ImageItemList(cid),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
                        iDayAndDayPrettyPictureMoreView.setDayAndDayPrettyPictureMoreData(model);
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
