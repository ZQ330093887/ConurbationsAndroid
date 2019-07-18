package com.test.admin.conurbations.presenter;


import android.app.Activity;
import android.text.TextUtils;

import com.test.admin.conurbations.activitys.IVideoInfoView;
import com.test.admin.conurbations.model.entity.DouyinVideoListData;
import com.test.admin.conurbations.retrofit.OkHttpClientManager;
import com.test.admin.conurbations.utils.DouYinUtils;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Request;

/**
 * Created by zhouqiong on 2019/4/2.
 */
public class VideoIndexPresenter extends BasePresenter<IVideoInfoView> {
    @Inject
    public VideoIndexPresenter() {
    }

    public void getDouYinData(Activity activity, final long max_cursor) {
        String url = DouYinUtils.getEncryptUrl(activity, 0, max_cursor);
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                mvpView.showFinishState();
                try {
                    DouyinVideoListData douYinData = null;
                    if (!TextUtils.isEmpty(response)) {
                        douYinData = DouyinVideoListData.fromJSONData(response);
                    }
                    mvpView.setVideoDouYinData(douYinData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                mvpView.showError("网络连接失败");
            }
        });
    }
}
