package com.test.admin.conurbations.config;

import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouqiong on 2016/3/15.
 */
public class Constants {

    // 创建一个以当前时间为名称的文件
    public static final String pathFileName = Environment.getExternalStorageDirectory() + "/" + getPhotoFileName();

    public static final String DATA_CACHE_FOLDER_PATH = "/wenjianhuancun/data";

    public static final String IMAGE_CACHE_FOLDER_PATH = "/tupianhuancun/imgs";

    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照

    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    public static final int[] testColors = {0xFF5D4037, 0xFF009688, 0xFF607D8B, 0xFF7B1FA2};

    public static final int[] toolBarColors = {0xFF431D12, 0xFF00796B, 0xFF455A64, 0xFF3F0F53};


    // 使用系统当前日期加以调整作为照片的名称
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd");
        return dateFormat.format(date) + ".jpg";
    }
}
