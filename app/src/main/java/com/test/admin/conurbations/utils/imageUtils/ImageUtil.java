package com.test.admin.conurbations.utils.imageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by zhouqiong on 2016/3/15.
 * 图片缓存类
 * 调用loadImage方法实现图片的缓存和显示
 * 添加权限：<uses-permission android:name="android.permission.INTERNET"/>
 *          <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *          <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 */
public class ImageUtil {

    public static void loadImage(String url, ImageView imageView) {
        if (FileUtil.isEmpty(url)) {
            return;
        }
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setTag(url);
        ImageDownloader.instance().loadDrawable(url, new ImageCallback(imageView) {
            @Override
            public void imageLoaded(Drawable imageDrawable, String url) {
                if (imageView.getTag().toString().equals(url)) {
                    imageView.setImageDrawable(imageDrawable);
                }
            }

            @Override
            void imageLoaded(Bitmap bitmap, String url) {
                if (imageView.getTag().toString().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }

    public static Bitmap zoomImg(Bitmap bitmap, int newWidth ,int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return newBitmap;
    }

    public static Bitmap getBitmapFromPath(String path, int w, int h, boolean lockScale) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

        BitmapFactory.decodeFile(path, opts);

        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth, scaleHeight;
        int newWidth, newHeight;

        if (width > w || height > h) {
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        else {
            return null;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        if (lockScale) {
            newWidth = (int) (width / scale);
            newHeight = (int) (height / scale);
        }
        else {
            newWidth = w;
            newHeight = h;
        }
        WeakReference<Bitmap> weak = new WeakReference<>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), newWidth, newHeight, true);
    }


    /** 保存方法 */
    public static void saveBitmap(Bitmap bm, String filePath) {
        if (bm == null) {
            return;
        }

        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.d("filePath:", "filePath"+filePath);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
