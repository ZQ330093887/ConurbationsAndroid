package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class WelfarePresenter extends BasePresenter<IWelfareView> {

    @Inject
    public WelfarePresenter() {
    }


    public void getCacheData() {
        final String key = "getGankItem" + "fuli";
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        GankData model = (GankData) obj;
        mvpView.setCacheData(model);
    }

    public void getWelfareData(int pager) {
        addSubscription(apiStores.getGanHuo("Girl", "Girl", pager),
                new ApiCallback<GankData>() {
                    @Override
                    public void onSuccess(GankData model) {
                        if (pager == 1) {
                            final String key = "getGankItem" + "fuli";
                            final ACache cache = ACache.get(AppUtils.getAppContext());
                            cache.put(key, model);
                        }
                        mvpView.setWelfareData(model);
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
