package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.INudePhotosView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.entity.MenuModel;
import com.test.admin.conurbations.model.entity.MenuSoup;
import com.test.admin.conurbations.utils.SoupFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class NudePhotosPresenter {

    private INudePhotosView iNudePhotosView;

    public NudePhotosPresenter(INudePhotosView view) {
        this.iNudePhotosView = view;
    }

    public void getNodeTitle() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.HOST_MOBILE_URL)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iNudePhotosView.showError("服务器异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<MenuModel> localList = new ArrayList<>();
                if (response != null && response.body() != null) {
                    Map<String, Object> values = SoupFactory.parseHtml(MenuSoup.class, response.body().string());
                    if (values != null) {
                        if (values.get(MenuSoup.class.getSimpleName()) != null) {
                            Object o = values.get(MenuSoup.class.getSimpleName());
                            localList.addAll((Collection<? extends MenuModel>) o);
                        }
                    }
                }

                iNudePhotosView.setNodePhotoData(localList);
            }
        });

    }
}
