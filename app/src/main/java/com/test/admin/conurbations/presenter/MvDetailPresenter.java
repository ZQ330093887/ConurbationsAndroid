package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IMvDetail;
import com.test.admin.conurbations.model.api.MusicApiServiceImpl;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.LogUtil;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class MvDetailPresenter extends BasePresenter<IMvDetail> {


    public MvDetailPresenter(IMvDetail mvDetail) {
        attachView(mvDetail);
    }

    public void loadMvDetail(String mvid) {

        ApiManager.request(MusicApiServiceImpl.INSTANCE.getMvDetailInfo(mvid),
                new RequestCallBack<MvInfo.MvDetailInfo>() {
                    @Override
                    public void success(MvInfo.MvDetailInfo result) {
                        mvpView.showMvDetailInfo(result.data);
                        mvpView.showFinishState();
                    }

                    @Override
                    public void error(String msg) {
                        mvpView.showError(msg);
                    }
                });
    }

    public void loadSimilarMv(String mvid) {
        ApiManager.request(MusicApiServiceImpl.INSTANCE.getSimilarMv(mvid),
                new RequestCallBack<MvInfo.SimilarMvInfo>() {
                    @Override
                    public void success(MvInfo.SimilarMvInfo result) {
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
