package com.test.admin.conurbations.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.test.admin.conurbations.config.AES;
import com.test.admin.conurbations.config.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 作者时间：Created by zhouqiong on 2016/3/15.
 * 类的作用：文件缓存调用类
 * 使用方法：调用loadSDCardCache和saveSDCardCache两个方法即可
 * 添加权限：<uses-permission android:name="android.permission.INTERNET"/>
 *          <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *          <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static String imageUriToPath(Context context, Uri uri) {
        String[] strings = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, strings, null, null, null);

        String imagePath = "";
        if (cursor != null && cursor.moveToFirst()) {
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        if (cursor != null) {
            cursor.close();
        }
        return imagePath;
    }

    /**
    * 获取本地缓存数据
    * */
    public static String loadSDCardCache(String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory() + Constants.DATA_CACHE_FOLDER_PATH);

        if (!dir.exists()) {
            return null;
        }

        File file = new File(dir, fileName.hashCode() + ".yqb");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            String string = "";
            int len;

            while ((len = fileInputStream.read(bytes)) != -1) {
                string += new String(bytes, 0, len, "utf-8");
            }

            fileInputStream.close();

            Log.d(TAG, "Load cache successfully: " + fileName);

            return AES.decode(string, fileName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 保存网络加载数据缓存到本地
     * */
    public static void saveSDCardCache(String content, String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory() + Constants.DATA_CACHE_FOLDER_PATH);

        if (!dir.exists()) {
            if (!dir.mkdirs()){
                return;
            }
        }

        File file = new File(dir, fileName.hashCode() + ".yqb");
        try {
            content = AES.encode(content, fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();

            Log.d(TAG, "Save cache successfully: " + fileName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean createDir(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return false;
            }
        }
        return true;
    }

    public static boolean deleteDir(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            return true;
        }

        return dir.delete();
    }

    public static boolean isEmpty(String string) {
        return string == null || string.equals("");
    }
}
