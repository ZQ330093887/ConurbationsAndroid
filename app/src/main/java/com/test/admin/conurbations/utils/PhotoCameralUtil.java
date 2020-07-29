package com.test.admin.conurbations.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;

import com.test.admin.conurbations.config.Constants;

import java.io.File;

/**
 * Created by zhouqiong on 2016/9/27.
 */

public class PhotoCameralUtil {
    //调用拍照和从本地图片获取信息
    public static void showHendPhotoDialog(final Activity activity, final String  path) {
        new AlertDialog.Builder(activity)
                .setTitle("头像设置")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
                        activity.startActivityForResult(intent, Constants.PHOTO_REQUEST_TAKEPHOTO);
                    }
                })
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        activity.startActivityForResult(intent, Constants.PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }

    //拍照裁剪图片
    public static void startPhotoZoom(Activity activity, Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);

        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, Constants.PHOTO_REQUEST_CUT);
    }
}
