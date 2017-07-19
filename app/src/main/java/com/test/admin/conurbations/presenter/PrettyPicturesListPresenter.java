package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;
import com.test.admin.conurbations.utils.AppUtils;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class PrettyPicturesListPresenter extends BasePresenter<IPrettyPictureListView> {

    private IPrettyPictureListView iPrettyPictureListView;

    public PrettyPicturesListPresenter(IPrettyPictureListView iPrettyPictureListView) {
        this.iPrettyPictureListView = iPrettyPictureListView;
        attachView(this.iPrettyPictureListView);
    }

    public void getPrettyPictureLisData(final int page, boolean isRefresh) {
        final String key = "getPrettyPictureLisData" + "girle";
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NetImage360 gankData = (NetImage360) obj;
            iPrettyPictureListView.setPrettyPictureData(gankData);
            return;
        }
        addSubscription(apiStores.get360ImageList(30),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
                        if (page == 1) {
                            cache.put(key, model);
                        }
                        iPrettyPictureListView.setPrettyPictureData(model);
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
