package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class NewsInfoListPresenter extends BasePresenter {

    private INewInformationView iNewInformationView;

    public NewsInfoListPresenter(INewInformationView iNewInformationView) {
        this.iNewInformationView = iNewInformationView;
    }

    public void getNewsInfoData(final String tabId) {

        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getThemeContentList(tabId),
                new ApiCallback<NewsList>() {
                    @Override
                    public void onSuccess(NewsList newsSummaries) {
                        iNewInformationView.setNewInfoData(newsSummaries);
                    }

                    @Override
                    public void onFailure(String msg) {
                        System.out.println("***********"+msg);
                    }

                    @Override
                    public void onFinish() {
                    }
                });

    }
}
