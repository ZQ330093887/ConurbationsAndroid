package com.test.admin.conurbations.presenter;


import android.text.TextUtils;
import android.util.Log;

import com.test.admin.conurbations.activitys.INBAInfoView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.NewsIndex;
import com.test.admin.conurbations.model.entity.NewsItem;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.JsonParserUtil;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zhouqiong on 2016/11/18.
 */
public class NBAIndexPresenter extends BasePresenter<INBAInfoView> {

    private List<String> indexs = new ArrayList<>();
    private int num = 10;
    private ACache cache;
    private String key;

    @Inject
    public NBAIndexPresenter() {
    }

    public void getCacheData(final String type) {
        key = "getNBAItem" + type;
        cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        NewsItem model = (NewsItem) obj;
        mvpView.setCacheData(model);
    }

    public void getNBAData(final int pager, final String type) {

        LogUtil.e(pager);
        addSubscription(apiStores.getNewsIndex(type),
                new ApiCallback<NewsIndex>() {
                    @Override
                    public void onSuccess(NewsIndex newsIndex) {
                        indexs.clear();
                        for (NewsIndex.IndexBean bean : newsIndex.data) {
                            indexs.add(bean.id);
                        }
                        String arcIds = parseIds(pager);
                        requestNews(arcIds, type, pager);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.showError(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.showFinishState();
                    }
                });

    }

    private void requestNews(String arcIds, String type, final int pager) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://sportsnba.qq.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new OkHttpClient.Builder().build()).build();
        GankService api = retrofit.create(GankService.class);
        Call<String> call = api.getNewsItem(type, arcIds);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsItem newsItem = JsonParserUtil.parseNewsItem(jsonStr);
                    if (pager == 0) {
                        cache.put(key, newsItem);
                    }
                    mvpView.setNBAInfoData(newsItem);
                    Log.d("resp:", jsonStr);
                } else {
                    ToastUtils.getInstance().showToast("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtils.getInstance().showToast(t.getMessage());
            }
        });
    }

    private String parseIds(int pager) {
        int size = indexs.size();
        StringBuilder articleIds = new StringBuilder();
        for (int i = pager, j = 0; i < size && j < num; i++, j++, pager++) {
            articleIds.append(indexs.get(i)).append(",");
        }
        if (!TextUtils.isEmpty(articleIds.toString()))
            articleIds = new StringBuilder(articleIds.substring(0, articleIds.length() - 1));
        Log.d("articleIds = ", articleIds.toString());
        return articleIds.toString();
    }
}
