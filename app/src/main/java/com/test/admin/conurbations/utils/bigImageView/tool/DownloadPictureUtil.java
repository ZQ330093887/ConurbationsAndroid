package com.test.admin.conurbations.utils.bigImageView.tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.test.admin.conurbations.utils.FileUtils;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.bigImageView.glide.engine.SimpleFileTarget;

import java.io.File;


/**
 * Created by zhouqiong on 2017/2/27.
 */
public class DownloadPictureUtil {

    public static void downloadPicture(final Context context, final String url, final String path, final String name) {
        Toast.makeText(context, "开始下载...", Toast.LENGTH_SHORT).show();
        Glide.with(context.getApplicationContext()).load(url).downloadOnly(new SimpleFileTarget() {
            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                boolean result = FileUtils.copyFile(resource, path, name);
                if (result) {
                    Toast.makeText(context, "成功保存", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        new SingleMediaScanner(context, path, () -> {
                            // scanning...
                        });
                    } else {
                        NavigationHelper.scanFileAsync(context, path);
                    }
                } else {
                    ToastUtils.getInstance().showToast("保存失败 ");
                }
            }
        });
    }
}