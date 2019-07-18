package com.test.admin.conurbations.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.IDiscoverView;
import com.test.admin.conurbations.activitys.IVideoDetailView;
import com.test.admin.conurbations.model.api.MusicApiServiceImpl;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.BannerResult;
import com.test.admin.conurbations.model.entity.DouyinVideoListData;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.OkHttpClientManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.DouYinUtils;
import com.test.admin.conurbations.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


/**
 * Created by zhouqiong on 2016/12/12.
 */

public class VideoDetailPresenter extends BasePresenter<IVideoDetailView> {


    public VideoDetailPresenter(IVideoDetailView iDiscoverView) {
        attachView(iDiscoverView);
    }

    /**
     * 下拉数据规律：min_cursor=max_cursor=0
     * 上拉数据规律：
     * 第二次请求取第一次请求返回的json数据中的min_cursor字段，max_cursor不需要携带。
     * 第三次以及后面所有的请求都只带max_cursor字段，值为第一次请求返回的json数据中的max_cursor字段
     */
    public void getDouyinListData(Activity activity, final long max_cursor) {
        String url = DouYinUtils.getEncryptUrl(activity, 0, max_cursor);
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
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
