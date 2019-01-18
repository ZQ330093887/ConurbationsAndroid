package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IMvView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.BaiduApiService;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.AppUtils;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class MvListPresenter extends BasePresenter<IMvView> {

    @Inject
    public MvListPresenter() {
    }

    public void getCacheData(final String type) {
        final String key = "mv" + type;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null) {
            MvInfo mvInfo = (MvInfo) obj;
            mvpView.setCacheData(mvInfo.data);
        } else {
            mvpView.setCacheData(null);
        }
    }

    public void loadMv(final int offset, String type) {
        ApiManager.request(baiduNetService.getTopMv(offset, 50),
                new RequestCallBack<MvInfo>() {
                    @Override
                    public void success(MvInfo result) {
                        final String key = "mv" + type;
                        final ACache cache = ACache.get(AppUtils.getAppContext());
                        cache.put(key, result);
                        mvpView.showMvList(result.data);
                        mvpView.showFinishState();
                    }

                    @Override
                    public void error(String msg) {
                        mvpView.showError(msg);
                    }
                });
    }


    public void loadRecentMv(int limit, String type) {
        ApiManager.request(baiduNetService.getNewestMv(limit),
                new RequestCallBack<MvInfo>() {
                    @Override
                    public void success(MvInfo result) {
                        final String key = "mv" + type;
                        final ACache cache = ACache.get(AppUtils.getAppContext());
                        cache.put(key, result);
                        mvpView.showMvList(result.data);
                        mvpView.showFinishState();
                    }

                    @Override
                    public void error(String msg) {
                        mvpView.showError(msg);
                    }
                });
    }
}
