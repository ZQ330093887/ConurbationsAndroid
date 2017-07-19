package com.test.admin.conurbations.presenter;


import android.text.TextUtils;
import android.util.Log;

import com.test.admin.conurbations.activitys.INewsInfoDetailListView;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.utils.JsonParserUtil;
import com.test.admin.conurbations.utils.ToastUtils;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.test.admin.conurbations.retrofit.AppClient.retrofit;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class NewsInfoListDetailPresenter extends BasePresenter<INewsInfoDetailListView> {

    private INewsInfoDetailListView iNewsInfoDetailListView;

    public NewsInfoListDetailPresenter(INewsInfoDetailListView iNewsInfoDetailListView) {
        this.iNewsInfoDetailListView = iNewsInfoDetailListView;
        attachView(this.iNewsInfoDetailListView);
    }

    public void getNewsInfoDetailData(int cid) {
        addSubscription(apiStores.getNewsDetail(cid),
                new ApiCallback<NewsDetail>() {
                    @Override
                    public void onSuccess(NewsDetail model) {
                        iNewsInfoDetailListView.setNewsInfoDetailData(model);
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

    public void getNBANewsInfoDetailData(String cid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://sportsnba.qq.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(new OkHttpClient.Builder().build()).build();
        GankService api = retrofit.create(GankService.class);
        Call<String> call = api.getNewsDetail("banner", cid);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsDetail detail = JsonParserUtil.parseNewsDetail(jsonStr);
                    iNewsInfoDetailListView.setNewsInfoDetailData(detail);
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
}
