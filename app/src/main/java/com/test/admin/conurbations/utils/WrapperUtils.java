package com.test.admin.conurbations.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.test.admin.conurbations.model.api.GankApi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhouqiong on 2017/1/23
 */
public class WrapperUtils {
    public static Observable<Integer> getSetWallWrapperObservable(Bitmap bitmap, final Context context) {

        return Observable.just(bitmap).map(bitmap1 -> {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                wallpaperManager.setBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
                return GankApi.status.error;
            }
            return GankApi.status.success;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Integer> getSetLockWrapperObservable(Bitmap bitmap, final Context context) {
        return Observable.just(bitmap).map(bitmap1 -> {
            WallpaperManager mWallManager = WallpaperManager.getInstance(context);
            Class class1 = mWallManager.getClass();
            Method setWallPaperMethod;
            try {
                setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return GankApi.status.error;
            }
            try {
                setWallPaperMethod.invoke(mWallManager, bitmap1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return GankApi.status.error;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return GankApi.status.error;
            }
            return GankApi.status.success;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //设置事件发生后的消费该事件的观察者
    public static Consumer<Integer> callbackSubscriber = integer -> {
        if (integer.intValue() == GankApi.status.success) {
            DialogUtils.hideProgressDialog();
            ToastUtils.getInstance().showToast("设置成功！");
        } else {
            ToastUtils.getInstance().showToast("设置失败...");
            DialogUtils.hideProgressDialog();
        }
    };
}
