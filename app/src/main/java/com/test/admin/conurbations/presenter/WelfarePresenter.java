package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.IWelfareList;
import com.test.admin.conurbations.data.GankType;
import com.test.admin.conurbations.data.response.GankData;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class WelfarePresenter extends BasePresenter {

    private IWelfareList welfareList;
    private static final int DEFAULT_PAGE_COUNT = 20;
    protected int getPageCount() {
        return DEFAULT_PAGE_COUNT;
    }

    public WelfarePresenter(IWelfareList welfareList) {
        this.welfareList = welfareList;
    }

    public void getWelfareData(final int pager) {

        addSubscription(AppClient.retrofit().create(GankService.class)
                .getGank(GankType.WELFARE, getPageCount(), pager),
                new ApiCallback<GankData>() {
                    @Override
                    public void onSuccess(GankData model) {
                        welfareList.setWelfareData(model);
                    }

                    @Override
                    public void onFailure(String msg) {}
                    @Override
                    public void onFinish() {}

                });

    }
}
