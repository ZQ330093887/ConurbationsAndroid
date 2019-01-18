package com.test.admin.conurbations.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.api.GankApi;
import com.test.admin.conurbations.utils.bigImageView.tool.SingleMediaScanner;
import com.test.admin.conurbations.utils.imageUtils.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhouqiong on 2017/1/23
 */
public class SaveBitmapUtils {


    public static String getCoverUri(Context context, String albumId) {
        if (albumId.equals("-1")) {
            return null;
        }
        String uri = null;
        try {
            Cursor cursor = context.getContentResolver().query(
                    Uri.parse("content://media/external/audio/albums/" + albumId),
                    new String[]{"album_art"}, null, null, null);
            if (cursor != null) {
                cursor.moveToNext();
                uri = cursor.getString(0);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }


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

    public static Observable<Boolean> getSaveBitmapObservable(File resource, String path, String name) {
        Observable<File> observable = Observable.create(e -> e.onNext(resource));
        return observable.map(bitmap1 -> FileUtils.copyFile(resource, path, name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //保存图片后的观察者
    public static Consumer<Boolean> getSaveSubscriber(final Context context, final String path) {
        return s -> {
            if (s) {
                DialogUtils.hideProgressDialog();
                ToastUtils.getInstance().showToast("下载图片成功，已下载到SdCard的MyPictures目录里");
                //发送广播更新相册（目的：相册中能看到下载的图片）
                new SingleMediaScanner(context, path, () -> {
                    // scanning...
                });
            } else {
                ToastUtils.getInstance().showToast("未知错误");
                DialogUtils.hideProgressDialog();
            }
        };
    }

    //保存图片以后分享图片的观察者
    public static Consumer<Boolean> getShareSubscriber(final Context context, String path) {
        return saveSuccess -> {
            if (saveSuccess) {
                FileUtils.startShareImg(path, context);
            } else {
                ToastUtils.getInstance().showToast("未知错误");
            }
        };
    }

    public interface BitmapCallBack {
        void showBitmap(Bitmap bitmap);
    }

    /**
     * 显示播放页大图
     *
     * @param mContext
     */
    public static void loadBigImageView(Context mContext, Music music, BitmapCallBack callBack) {
        if (music == null) return;
        String url = getCoverUriByMusic(music, true);
        Glide.with(mContext)
                .load(url == null ? R.mipmap.music_five : url)
                .asBitmap()
                .error(R.mipmap.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // 下载失败回调
//                        ToastUtils.getInstance().showToast("下载失败回调");
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (callBack != null) {
                            callBack.showBitmap(resource);
                        }
                    }
                });
    }


    public static void loadBigImageView(Context mContext, Music music, ImageView imageView) {
        if (music == null || imageView == null) return;
        String url = getCoverUriByMusic(music, true);
        Glide.with(mContext)
                .load(url == null ? R.mipmap.music_five : url)
                .asBitmap()
                .error(R.mipmap.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 返回bitmap
     *
     * @param mContext
     * @param url
     * @param callBack
     */
    public static void loadBitmap(Context mContext, String url, BitmapCallBack callBack) {
        if (mContext == null) return;
        Glide.with(mContext)
                .load(url == null ? R.mipmap.default_cover : url)
                .asBitmap()
                .error(R.mipmap.default_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, GlideAnimation<? super Bitmap> transition) {
                        if (callBack != null) {
                            callBack.showBitmap(resource);
                        }
                    }
                });
    }

    public static Drawable createBlurredImageFromBitmap(Bitmap bitmap) {
        return ImageUtil.createBlurredImageFromBitmap(bitmap, 4);
    }

    /**
     * 显示图片
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        if (mContext == null) return;
        Glide.with(mContext)
                .load(url)
                .error(R.mipmap.default_cover)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImageView(Context mContext, String url, int defaultUrl, ImageView imageView) {
        if (mContext == null) return;
        Glide.with(mContext)
                .load(url)
                .error(defaultUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 显示小图
     *
     * @param mContext
     * @param music
     * @param callBack
     */
    public static void loadImageViewByMusic(Context mContext, Music music, BitmapCallBack callBack) {
        if (music == null) return;
        String url = getCoverUriByMusic(music, false);
        loadBitmap(mContext, url, callBack);
    }

    /**
     * 获取专辑图url，
     *
     * @param music 音乐
     * @param isBig 是否是大图
     * @return
     */
    private static String getCoverUriByMusic(Music music, boolean isBig) {
        if (music.coverBig != null && isBig) {
            return music.coverBig;
        } else if (music.coverUri != null) {
            return music.coverUri;
        } else {
            return music.coverSmall;
        }
    }


}
