package com.test.admin.conurbations.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.widget.SolidApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouqiong on 2017/2/23.
 */
public class CommonUtil {

    public static void ensureUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("Invalid access, not in UI thread");
        }
    }

    public static void exit() {
        System.exit(0);
    }


    /**
     * 格式化播放次数
     */
    public static String formatPlayCount(int count) {
        if (count < 10000) {
            return String.valueOf(count);
        } else {
            return count / 10000 + "." + count / 1000 % 10 + "万";
        }
    }


    /**
     * 音乐名格式化
     *
     * @param title
     * @return
     */
    public static String getTitle(String title) {
        title = stringFilter(title);
        if (TextUtils.isEmpty(title)) {
            title = SolidApplication.getInstance().getString(R.string.unknown);
        }
        return title;
    }

    /**
     * 歌手名格式化
     *
     * @param artist
     * @return
     */
    public static String getArtist(String artist) {
        artist = stringFilter(artist);
        if (TextUtils.isEmpty(artist)) {
            artist = SolidApplication.getInstance().getString(R.string.unknown);
        }
        return artist;
    }


    /**
     * 歌手专辑格式化
     *
     * @param artist
     * @param album
     * @return
     */
    public static String getArtistAndAlbum(String artist, String album) {
        artist = stringFilter(artist);
        album = stringFilter(album);
        if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return "";
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return artist;
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            return album;
        } else {
            return artist + " - " + album;
        }
    }


    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "<[^>]+>";
//        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 强制隐藏输入法
     */
    public static void hideInputView(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) SolidApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
