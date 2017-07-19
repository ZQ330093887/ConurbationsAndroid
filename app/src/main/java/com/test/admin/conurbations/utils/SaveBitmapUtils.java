package com.test.admin.conurbations.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.test.admin.conurbations.model.api.GankApi;
import com.test.admin.conurbations.widget.SolidApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhouqiong on 2017/1/23
 */
public class SaveBitmapUtils {

    public static Observable<String> getSaveBitmapObservable(final Bitmap bitmap) {

        Observable<Bitmap> observable = Observable.create(e -> e.onNext(bitmap));
        return observable.map(bitmap1 -> {
            String name = "/" + System.currentTimeMillis() + ".png";
            File file = new File(GankApi.imgPath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(GankApi.imgPath + name);
            try {
                //创建需要保存的图片成功，若未成功则已经有该文件
                if (file.createNewFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return GankApi.status.error + "";
            }
            return file.toString();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //保存图片后的观察者
    public static Consumer<String> saveSubscriber = s -> {
        if (!s.equals(GankApi.status.error)) {
            DialogUtils.hideProgressDialog();
            ToastUtils.getInstance().showToast("下载图片成功，已下载到SdCard的MyPictures目录里");
            //发送广播更新相册（目的：相册中能看到下载的图片）
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(s);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            SolidApplication.getInstance().sendBroadcast(intent);
        } else {
            ToastUtils.getInstance().showToast("未知错误");
            DialogUtils.hideProgressDialog();
        }
    };

    //保存图片以后分享图片的观察者
    public static Consumer<String> getShareSubscriber(final Context context) {
        return path -> {
            if (!path.equals(GankApi.status.error)) {
                FileUtil.startShareImg(path, context);
            } else {
                ToastUtils.getInstance().showToast("未知错误");
            }
        };
    }
}
