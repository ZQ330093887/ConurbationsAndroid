package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IGankDayView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.response.GankHeaderItem;
import com.test.admin.conurbations.model.response.GankHotData;
import com.test.admin.conurbations.model.response.GankImageData;
import com.test.admin.conurbations.model.response.GankItem;
import com.test.admin.conurbations.model.response.GankNormalItem;
import com.test.admin.conurbations.model.response.GankType;
import com.test.admin.conurbations.retrofit.ApiCallback;
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
        GankHotData model = (GankHotData) obj;
        mvpView.setCacheData(getGankList(model));
    }

    public void getDayData() {

        addSubscription(apiStores.getBanners(),
                new ApiCallback<GankImageData>() {
                    @Override
                    public void onSuccess(GankImageData imageData) {
                        addSubscription(apiStores.getDayGank(),
                                new ApiCallback<GankHotData>() {
                                    @Override
                                    public void onSuccess(GankHotData model) {
                                        if (imageData != null) {
                                            model.imageData = imageData;
                                        }
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

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        mvpView.showError(msg);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    private List<GankItem> getGankList(GankHotData dayData) {
        if (null == dayData || null == dayData.data) {
            return null;
        }
        List<GankItem> gankList = new ArrayList<>(13);
        //banner数据
        if (null != dayData.imageData) {
            gankList.add(dayData.imageData);
        }
        if (null != dayData.data && dayData.data.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.HOT));
            gankList.addAll(GankNormalItem.newGankList(dayData.data));
        }
        return gankList;
    }
}
