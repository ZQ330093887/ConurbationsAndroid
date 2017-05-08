package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.model.response.GankType;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;
import com.test.admin.conurbations.utils.AppUtils;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class WelfarePresenter extends BasePresenter {

    private IWelfareView welfareList;
    private static final int DEFAULT_PAGE_COUNT = 20;

    protected int getPageCount() {
        return DEFAULT_PAGE_COUNT;
    }

    public WelfarePresenter(IWelfareView welfareList) {
        this.welfareList = welfareList;
    }

    public void getWelfareData(final boolean isRefresh, final int pager) {

        final String key = "getGankItem" + "fuli";
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            GankData model = (GankData) obj;
            welfareList.setWelfareData(model);
            return;
        }

        addSubscription(AppClient.retrofit().create(GankService.class).getGank(GankType.WELFARE, getPageCount(), pager),
                new ApiCallback<GankData>() {
                    @Override
                    public void onSuccess(GankData model) {
                        if (pager == 1) {
                            cache.put(key, model);
                        }
                        welfareList.setWelfareData(model);
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
