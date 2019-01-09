package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IGankDayView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.response.GankGirlImageItem;
import com.test.admin.conurbations.model.response.GankHeaderItem;
import com.test.admin.conurbations.model.response.GankItem;
import com.test.admin.conurbations.model.response.GankNormalItem;
import com.test.admin.conurbations.model.response.GankType;
import com.test.admin.conurbations.model.response.TodayData;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class GankDayPresenter extends BasePresenter<IGankDayView> {
    private String key = "getGankDayData" + "zuixin";
    private ACache cache = ACache.get(AppUtils.getAppContext());

    @Inject
    public GankDayPresenter() {
    }


    public void getCacheData() {
        Object obj = cache.getAsObject(key);
        TodayData model = (TodayData) obj;
        mvpView.setCacheData(getGankList(model));
    }

    public void getDayData() {
        addSubscription(apiStores.getDayGank(),
                new ApiCallback<TodayData>() {
                    @Override
                    public void onSuccess(TodayData model) {
                        cache.put(key, model);
                        mvpView.setGankDayData(getGankList(model));
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        mvpView.showError(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.showFinishState();
                    }

                });
    }

    private List<GankItem> getGankList(TodayData dayData) {
        if (null == dayData || null == dayData.results) {
            return null;
        }
        List<GankItem> gankList = new ArrayList<>(10);
        if (null != dayData.results.welfareList && dayData.results.welfareList.size() > 0) {
            gankList.add(GankGirlImageItem.newImageItem(dayData.results.welfareList.get(0)));
        }
        if (null != dayData.results.androidList && dayData.results.androidList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.ANDROID));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.androidList));
        }
        if (null != dayData.results.iosList && dayData.results.iosList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.IOS));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.iosList));
        }
        if (null != dayData.results.frontEndList && dayData.results.frontEndList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.FRONTEND));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.frontEndList));
        }
        if (null != dayData.results.extraList && dayData.results.extraList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.EXTRA));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.extraList));
        }
        if (null != dayData.results.casualList && dayData.results.casualList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.CASUAL));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.casualList));
        }
        if (null != dayData.results.appList && dayData.results.appList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.APP));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.appList));
        }
        if (null != dayData.results.videoList && dayData.results.videoList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.VIDEO));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.videoList));
        }

        return gankList;
    }
}
