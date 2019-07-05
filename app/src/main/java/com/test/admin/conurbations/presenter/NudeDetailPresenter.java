package com.test.admin.conurbations.presenter;


import com.test.admin.conurbations.activitys.INudePhotosView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.entity.PageModel;
import com.test.admin.conurbations.model.entity.PageSoup;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.SoupFactory;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class NudeDetailPresenter extends BasePresenter<INudePhotosView> {

    @Inject
    public NudeDetailPresenter() {
    }

    public void getCacheData(final String url) {
        final String key = "getNudeDetail" + url;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        PageModel pageModel = (PageModel) obj;
        mvpView.setCacheNudePhotos(pageModel);
    }

    public void getNudeDetail(String url, int pager) {
        final String key = "getNudeDetail" + url;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mvpView.showError("服务器异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                PageModel soups = null;
                if (response != null && response.body() != null) {
                    Map<String, Object> values = SoupFactory.parseHtml(PageSoup.class, response.body().string());
                    if (values != null) {
                        PageModel pageModel = (PageModel) values.get(PageSoup.class.getSimpleName());
                        if (pageModel != null) {
                            soups = pageModel;
                            if (pager == 1) {
                                cache.put(key, pageModel);
                            }
                        }
                    }
                }
                mvpView.setNodeDetailData(soups);
            }
        });
    }
}
