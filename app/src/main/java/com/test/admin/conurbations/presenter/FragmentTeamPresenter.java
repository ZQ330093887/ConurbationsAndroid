package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.INewList;
import com.test.admin.conurbations.data.response.GankService;
import com.test.admin.conurbations.model.NewsList;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class FragmentTeamPresenter extends BasePresenter {
    private INewList iNewList;
    public FragmentTeamPresenter(INewList iNewList) {
        this.iNewList = iNewList;
    }

    public void getNewListData(int pager,String ordDate) {
        if (pager == 1){
            addSubscription(AppClient.retrofit().create(GankService.class).getLatestNews(), new ApiCallback<NewsList>() {
                @Override
                public void onSuccess(NewsList model) {
                    iNewList.setNewListData(model);
                }

                @Override
                public void onFailure(String msg) {
                }

                @Override
                public void onFinish() {
                }
            });
        }else {
            addSubscription(AppClient.retrofit().create(GankService.class).getBeforeNews(ordDate), new ApiCallback<NewsList>() {
                @Override
                public void onSuccess(NewsList model) {

                    iNewList.setNewListData(model);
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
}
