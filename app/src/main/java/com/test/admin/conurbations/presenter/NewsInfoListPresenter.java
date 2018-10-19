package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class NewsInfoListPresenter extends BasePresenter<INewInformationView> {

    @Inject
    public NewsInfoListPresenter() {
    }

    public void getNewsInfoData(final String tabId, final int pager, boolean isRefresh) {
        final String key = "getNewsInfo" + tabId;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsList model = (NewsList) obj;
            mvpView.setNewInfoData(model);
            return;
        }

        addSubscription(apiStores.getThemeContentList(tabId),
                new ApiCallback<NewsList>() {
                    @Override
                    public void onSuccess(NewsList newsSummaries) {
                        if (pager == 1) {
                            cache.put(key, newsSummaries);
                        }
                        mvpView.setNewInfoData(newsSummaries);
                    }

                    @Override
                    public void onFailure(String msg) {
                        System.out.println("***********" + msg);
                    }

                    @Override
                    public void onFinish() {
                    }
                });

    }
}
