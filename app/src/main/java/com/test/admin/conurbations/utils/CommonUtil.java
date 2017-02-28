package com.test.admin.conurbations.utils;

import android.os.Looper;

/**
 * Created by zhouqiong on 2017/2/23.
 */
public class CommonUtil {

    public static void ensureUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()){
            throw new RuntimeException("Invalid access, not in UI thread");
        }
    }

    public static void exit() {
        System.exit(0);
    }
}
