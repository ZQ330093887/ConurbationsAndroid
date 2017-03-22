package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.ISearchView;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class SearchPresenter extends BasePresenter {

    private ISearchView iSearchView;

    public SearchPresenter(ISearchView iSearchView) {
        this.iSearchView = iSearchView;
    }

    public void getSearchQueryInfo(String mSearchQuery, int pages) {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getImageList("ajax", "result", mSearchQuery, pages * 24),
                new ApiCallback<NetImage>() {

                    @Override
                    public void onSuccess(NetImage model) {
                        iSearchView.setSearchData(model);
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
