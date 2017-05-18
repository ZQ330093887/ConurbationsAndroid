package com.test.admin.conurbations.utils;

import android.content.Context;
import android.content.res.Resources;

import com.test.admin.conurbations.R;

public class ADFilterUtils {
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.ad);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }
}