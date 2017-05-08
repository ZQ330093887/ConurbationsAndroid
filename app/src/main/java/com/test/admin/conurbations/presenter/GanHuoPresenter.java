package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;
import com.test.admin.conurbations.utils.AppUtils;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class GanHuoPresenter extends BasePresenter {

    private IWelfareView welfareList;

    public GanHuoPresenter(IWelfareView welfareList) {
        this.welfareList = welfareList;
    }

    public void getWelfareData(final String type, final int pager, boolean isRefresh) {
        final String key = "getGankItem" + type;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            GankData gankData = (GankData) obj;
            welfareList.setWelfareData(gankData);
            return;
        }
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getGanHuo(type, pager),
                new ApiCallback<GankData>() {
                    @Override
                    public void onSuccess(GankData model) {
                        if (pager == 1) {//只缓存第一页的数据
                            cache.put(key, model);
                        }
                        welfareList.setWelfareData(model);
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
