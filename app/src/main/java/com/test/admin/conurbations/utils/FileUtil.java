package com.test.admin.conurbations.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.config.AES;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.widget.SolidApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 作者时间：Created by zhouqiong on 2016/3/15.
 * 类的作用：文件缓存调用类
 * 使用方法：调用loadSDCardCache和saveSDCardCache两个方法即可
 * 添加权限：<uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    /**
     * 递归创建文件夹
     *
     * @param file
     * @return 创建失败返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                Log.i("----- 创建文件", file.getAbsolutePath());
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                Log.i("----- 创建文件", file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String imageUriToPath(Context context, Uri uri) {
        String[] strings = {MediaStore.Images.Media.DATA};
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
     */
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 保存网络加载数据缓存到本地
     */
    public static void saveSDCardCache(String content, String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory() + Constants.DATA_CACHE_FOLDER_PATH);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
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
        } catch (Exception e) {
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

    public static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            sdDir = SolidApplication.getInstance().getFilesDir();
        }
        return sdDir.toString();
    }

    //分享图片
    public static void startShareImg(String path, Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, "请选择"));
    }

    public static void sharePage(WebView vWebView, Context context) {
        String title = vWebView.getTitle();
        String url = vWebView.getUrl();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_page, title, url));
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

    public static boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    /**
     * 根据文件路径拷贝文件
     *
     * @param resourceFile 源文件
     * @param targetPath   目标路径（包含文件名和文件格式）
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(File resourceFile, String targetPath, String fileName) {
        boolean result = false;
        if (resourceFile == null || TextUtils.isEmpty(targetPath)) {
            return result;
        }
        File target = new File(targetPath);
        if (target.exists()) {
            target.delete(); // 已存在的话先删除
        } else {
            try {
                target.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File targetFile = new File(targetPath.concat(fileName));
        if (targetFile.exists()) {
            targetFile.delete();
        } else {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileChannel resourceChannel = null;
        FileChannel targetChannel = null;
        try {
            resourceChannel = new FileInputStream(resourceFile).getChannel();
            targetChannel = new FileOutputStream(targetFile).getChannel();
            resourceChannel.transferTo(0, resourceChannel.size(), targetChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            resourceChannel.close();
            targetChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取缓存路径 不存在自动创建
     *
     * @param uniqueName 独一无二的文件夹名(如：img_cache_glide)
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        File file;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                if (context.getExternalCacheDir() != null) {
                    cachePath = context.getExternalCacheDir().toString();
                } else {
                    cachePath = context.getCacheDir().toString();
                }
            } else {
                cachePath = context.getCacheDir().toString();
            }
            file = new File(cachePath + File.separator + uniqueName);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
