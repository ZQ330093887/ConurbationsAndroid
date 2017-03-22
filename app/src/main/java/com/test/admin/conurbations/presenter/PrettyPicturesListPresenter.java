package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class PrettyPicturesListPresenter extends BasePresenter {

    private IPrettyPictureListView iPrettyPictureListView;

    public PrettyPicturesListPresenter(IPrettyPictureListView iPrettyPictureListView) {
        this.iPrettyPictureListView = iPrettyPictureListView;
    }

    public void getPrettyPictureLisData() {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .get360ImageList(30),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
                        iPrettyPictureListView.setPrettyPictureData(model);
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
