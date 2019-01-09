package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class GanHuoPresenter extends BasePresenter<IWelfareView> {

    @Inject
    public GanHuoPresenter() {
    }

    public void getCacheData(final String type) {
        final String key = "getGankItem" + type;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        GankData gankData = (GankData) obj;
        mvpView.setCacheData(gankData);
    }

    public void getWelfareData(final String type, final int pager) {
        final String key = "getGankItem" + type;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        addSubscription(apiStores.getGanHuo(type, pager),
                new ApiCallback<GankData>() {
                    @Override
                    public void onSuccess(GankData model) {
                        if (pager == 1) {//只缓存第一页的数据
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
