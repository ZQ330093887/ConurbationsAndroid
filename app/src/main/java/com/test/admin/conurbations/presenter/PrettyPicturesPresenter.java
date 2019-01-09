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
public class PrettyPicturesPresenter extends BasePresenter<IPrettyPictureListView> {
    private final String key = "getPrettyPictureLisData" + "cid";
    private final ACache cache = ACache.get(AppUtils.getAppContext());

    @Inject
    public PrettyPicturesPresenter() {
    }

    public void getCacheData() {
        Object obj = cache.getAsObject(key);
        NetImage360 model = (NetImage360) obj;
        mvpView.setCacheData(model);
    }

    public void getPrettyPictureLisData(String cid) {
        addSubscription(apiStores.get360ImageItemList(cid),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
//                        if (pager == 1) {
//                            cache.put(key, model);
//                        }
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
