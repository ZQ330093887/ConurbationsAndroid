package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IMvView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.api.BaiduApiService;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class MvListPresenter extends BasePresenter<IMvView> {
    private BaiduApiService baiduApiService = ApiManager.getInstance().create(BaiduApiService.class, Constants.BASE_NETEASE_URL);

    @Inject
    public MvListPresenter() {
    }

    public void loadMv(final int offset) {
        ApiManager.request(baiduApiService.getTopMv(offset, 50),
                new RequestCallBack<MvInfo>() {
                    @Override
                    public void success(MvInfo result) {
                        mvpView.showMvList(result.data);
                        mvpView.showFinishState();
                    }

                    @Override
                    public void error(String msg) {
                        mvpView.showError(msg);
                    }
                });
    }


    public void loadRecentMv(int limit) {
        ApiManager.request(baiduApiService.getNewestMv(limit),
                new RequestCallBack<MvInfo>() {
                    @Override
                    public void success(MvInfo result) {
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
