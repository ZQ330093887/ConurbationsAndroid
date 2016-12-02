package com.test.admin.conurbations.network;

import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhouqiong on 2016/12/2.
 */

public abstract class RequestCallback extends Callback<Object> {

    public RequestCallback() {
    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    public Object parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }
}
