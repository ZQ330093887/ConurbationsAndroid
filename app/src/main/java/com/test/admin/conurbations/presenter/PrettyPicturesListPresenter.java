package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class PrettyPicturesListPresenter extends BasePresenter<IPrettyPictureListView> {
    private final String key = "getPrettyPictureLisData" + "girle";
    private final ACache cache = ACache.get(AppUtils.getAppContext());

    @Inject
    public PrettyPicturesListPresenter() {
    }

    public void getCacheData() {
        Object obj = cache.getAsObject(key);
        NetImage360 gankData = (NetImage360) obj;
        mvpView.setCacheData(gankData);
    }

    public void getPrettyPictureLisData(final int page) {
        addSubscription(apiStores.get360ImageList(30),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
                        if (page == 1) {
                            cache.put(key, model);
                        }
                        mvpView.setPrettyPictureData(model);
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
