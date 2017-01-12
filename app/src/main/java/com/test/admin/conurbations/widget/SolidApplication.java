package com.test.admin.conurbations.widget;

import android.app.Application;
import android.os.Environment;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.test.admin.conurbations.utils.ToastUtils;

import java.io.File;


/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:20:59
 */
public class SolidApplication extends Application {
    private static SolidApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ToastUtils.init(this);
        FeedbackAPI.initAnnoy(this, "23601404");
    }

    public static SolidApplication getInstance() {
        return mInstance;
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }
}
