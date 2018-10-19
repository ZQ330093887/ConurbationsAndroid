package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.INewInfoIndexView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class NewsInfoIndexPresenter extends BasePresenter<INewInfoIndexView> {

    @Inject
    public NewsInfoIndexPresenter() {
    }

    public void getNewListData(final int pager, String ordDate, boolean isRefresh) {

        /**
         * 新数据和旧数据不是一个接口，第一页显示新数据，第二页开始显示旧数据
         * 这里只缓存第一页的新数据
         */
        final String key = "getNewListData" + ordDate;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        if (pager == 1) {
            Object obj = cache.getAsObject(key);
            if (obj != null && !isRefresh) {
                NewsList model = (NewsList) obj;
                mvpView.setNewListData(model);
                return;
            }
            addSubscription(apiStores.getLatestNews(), new ApiCallback<NewsList>() {
                @Override
                public void onSuccess(NewsList model) {
                    if (pager == 1) {
                        cache.put(key, model);
                    }
                    mvpView.setNewListData(model);
                }

                @Override
                public void onFailure(String msg) {
                }

                @Override
                public void onFinish() {
                }
            });
        } else {
            addSubscription(apiStores.getBeforeNews(ordDate), new ApiCallback<NewsList>() {
                @Override
                public void onSuccess(NewsList model) {
                    mvpView.setNewListData(model);
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
