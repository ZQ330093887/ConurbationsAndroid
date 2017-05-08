package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.ISouGouImageView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;
import com.test.admin.conurbations.utils.AppUtils;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class SouGouImagePresenter extends BasePresenter {

    private ISouGouImageView souGouImageList;

    public SouGouImagePresenter(ISouGouImageView souGouImageList) {
        this.souGouImageList = souGouImageList;
    }

    public void getSouGouImageData(String imgType, final int pages, boolean isRefresh) {

        final String key = "getSouGouImageData" + imgType;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NetImage model = (NetImage) obj;
            souGouImageList.setSouGouImageData(model);
            return;
        }

        addSubscription(AppClient.retrofit().create(GankService.class)
                        .getImageList("ajax", "result", imgType, pages * 24),
                new ApiCallback<NetImage>() {

                    @Override
                    public void onSuccess(NetImage model) {
                        if (pages == 1) {
                            cache.put(key, model);
                        }
                        souGouImageList.setSouGouImageData(model);
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
