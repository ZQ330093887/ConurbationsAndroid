package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.IDayAndDayPrettyPictureView;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.model.NetImage360;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class DayAndDayPrettyPicturesPresenter extends BasePresenter {

    private IDayAndDayPrettyPictureView iDayAndDayPrettyPictureView;

    public DayAndDayPrettyPicturesPresenter(IDayAndDayPrettyPictureView iDayAndDayPrettyPictureView) {
        this.iDayAndDayPrettyPictureView = iDayAndDayPrettyPictureView;
    }

    public void getWelfareData() {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .get360ImageList(30),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
                        iDayAndDayPrettyPictureView.setDayPrettyPicture(model);
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
