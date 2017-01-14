package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.ISearchView;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.model.NetImage360;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class SearchPresenter extends BasePresenter {

    private ISearchView searchView;

    public SearchPresenter(ISearchView iTestList) {
        this.searchView = iTestList;
    }

    public void getSearchInfo(String cid) {
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .get360ImageItemList(cid),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
                        searchView.setSearchData(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.d("msd", msg);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }
}
