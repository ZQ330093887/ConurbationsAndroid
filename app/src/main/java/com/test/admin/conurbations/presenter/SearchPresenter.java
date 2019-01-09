package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.ISearchView;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.retrofit.ApiCallback;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class SearchPresenter extends BasePresenter<ISearchView> {

    @Inject
    public SearchPresenter() {
    }

    public void getSearchQueryInfo(String mSearchQuery, int pages) {
        addSubscription(apiStores.getImageList("ajax", "result", mSearchQuery, pages * 24),
                new ApiCallback<NetImage>() {

                    @Override
                    public void onSuccess(NetImage model) {
                        mvpView.setSearchData(model);
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
