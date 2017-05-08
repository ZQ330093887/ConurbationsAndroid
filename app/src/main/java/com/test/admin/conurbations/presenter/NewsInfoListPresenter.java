package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;
import com.test.admin.conurbations.utils.AppUtils;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class NewsInfoListPresenter extends BasePresenter {

    private INewInformationView iNewInformationView;

    public NewsInfoListPresenter(INewInformationView iNewInformationView) {
        this.iNewInformationView = iNewInformationView;
    }

    public void getNewsInfoData(final String tabId, final int pager, boolean isRefresh) {
        final String key = "getNewsInfo" + tabId;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsList model = (NewsList) obj;
            iNewInformationView.setNewInfoData(model);
            return;
        }

        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getThemeContentList(tabId),
                new ApiCallback<NewsList>() {
                    @Override
                    public void onSuccess(NewsList newsSummaries) {
                        if (pager == 1) {
                            cache.put(key, newsSummaries);
                        }
                        iNewInformationView.setNewInfoData(newsSummaries);
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
