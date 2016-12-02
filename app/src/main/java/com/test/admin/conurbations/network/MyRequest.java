package com.test.admin.conurbations.network;

import android.os.Bundle;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

/**
 * Created by zhouqiong on 2016/12/2.
 */

public class MyRequest {

    private static MyRequest instance = new MyRequest();
    private MyRequest(){}
    public static MyRequest getInstance(){
        return instance;
    }
    public void requestUrl(String urlString, RequestCallback requestCallback) {
        urlString = RequestUrl.URL_PREFIX + urlString;

        OkHttpUtils.get().url(urlString).build().execute(requestCallback);
    }

    private StringBuffer RequestBundle(Bundle bundle){
        StringBuffer paramsString = new StringBuffer();
        Set<String> keys = bundle.keySet();

        for (String key : keys) {
            paramsString.append(key);
            paramsString.append("/");
        }
        return paramsString;
    }
}
