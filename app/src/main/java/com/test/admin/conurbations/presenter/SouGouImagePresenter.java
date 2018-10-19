package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.ISouGouImageView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class SouGouImagePresenter extends BasePresenter<ISouGouImageView> {


    @Inject
    public SouGouImagePresenter() {
    }

    public void getSouGouImageData(String imgType, final int pages, boolean isRefresh) {

        final String key = "getSouGouImageData" + imgType;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NetImage model = (NetImage) obj;
            mvpView.setSouGouImageData(model);
            return;
        }
        addSubscription(apiStores.getImageList("ajax", "result", imgType, pages * 24),
                new ApiCallback<NetImage>() {

                    @Override
                    public void onSuccess(NetImage model) {
                        if (pages == 1) {
                            cache.put(key, model);
                        }
                        mvpView.setSouGouImageData(model);
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
