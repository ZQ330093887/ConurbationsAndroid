package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.ISouGouImageView;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class SouGouImagePresenter extends BasePresenter {

    private ISouGouImageView souGouImageList;

    public SouGouImagePresenter(ISouGouImageView souGouImageList) {
        this.souGouImageList = souGouImageList;
    }

    public void getSouGouImageData(String imgType, int pages) {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getImageList("ajax", "result", imgType, pages * 24),
                new ApiCallback<NetImage>() {

                    @Override
                    public void onSuccess(NetImage model) {
                        souGouImageList.setSouGouImageData(model);
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
