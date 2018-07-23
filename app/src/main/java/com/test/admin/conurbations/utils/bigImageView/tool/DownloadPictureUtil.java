package com.test.admin.conurbations.utils.bigImageView.tool;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.test.admin.conurbations.utils.FileUtil;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.bigImageView.glide.engine.SimpleFileTarget;

import java.io.File;


/**
 * Created by zhouqiong on 2017/2/27.
 */
public class DownloadPictureUtil {

    public static void downloadPicture(final Context context, final String url, final String path, final String name) {
        ToastUtils.getInstance().showToast("开始下载...");
        Glide.with(context.getApplicationContext()).load(url).downloadOnly(new SimpleFileTarget() {
            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                ToastUtils.getInstance().showToast("保存失败");
            }

            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                boolean result = FileUtil.copyFile(resource, path, name);
                if (result) {
                    ToastUtils.getInstance().showToast("成功保存到 ".concat(path).concat(name));
                    new SingleMediaScanner(context, path, new SingleMediaScanner.ScanListener() {
                        @Override
                        public void onScanFinish() {
                            // scanning...
                        }
                    });
                } else {
                    ToastUtils.getInstance().showToast("保存失败 ");
                }
            }
        });
    }
}