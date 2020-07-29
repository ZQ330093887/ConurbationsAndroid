package com.test.admin.conurbations.utils;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.widget.SolidApplication;


/**
 * Zqiong on 2016/8/12 16:03
 * 内部存储工具
 */
public class SPUtils {
    private static final String MUSIC_ID = "music_id";
    private static final String PLAY_POSITION = "play_position";
    private static final String PLAY_MODE = "play_mode";
    private static final String SPLASH_URL = "splash_url";
    private static final String WIFI_MODE = "wifi_mode";
    private static final String LYRIC_MODE = "lyric_mode";
    private static final String NIGHT_MODE = "night_mode";
    private static final String POSITION = "position";
    private static final String DESKTOP_LYRIC_SIZE = "desktop_lyric_size";
    private static final String DESKTOP_LYRIC_COLOR = "desktop_lyric_color";
    public static final String QQ_OPEN_ID = "qq_open_id";
    public static final String QQ_ACCESS_TOKEN = "qq_access_token";
    public static final String QQ_EXPIRES_IN = "expires_in";

    public static int getPlayPosition() {
        return getAnyByKey(PLAY_POSITION, -1);
    }

    public static void setPlayPosition(int position) {
        putAnyCommit(PLAY_POSITION, position);
    }


    public static String getCurrentSongId() {
        return getAnyByKey(MUSIC_ID, "");
    }

    public static void saveCurrentSongId(String mid) {
        putAnyCommit(MUSIC_ID, mid);
    }

    public static long getPosition() {
        return getAnyByKey(POSITION, 0);
    }

    public static void savePosition(long id) {
        putAnyCommit(POSITION, id);
    }

    public static int getPlayMode() {
        return getAnyByKey(PLAY_MODE, 0);
    }

    public static void savePlayMode(int mode) {
        putAnyCommit(PLAY_MODE, mode);
    }

    public static String getSplashUrl() {
        return getAnyByKey(SPLASH_URL, "");
    }

    public static void saveSplashUrl(String url) {
        putAnyCommit(SPLASH_URL, url);
    }

    public static boolean getWifiMode() {
        return getAnyByKey(SolidApplication.getInstance().getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void saveWifiMode(boolean enable) {
        putAnyCommit(SolidApplication.getInstance().getString(R.string.setting_key_mobile_wifi), enable);
    }

    public static boolean isShowLyricView() {
        return getAnyByKey(SolidApplication.getInstance().getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void showLyricView(boolean enable) {
        putAnyCommit(SolidApplication.getInstance().getString(R.string.setting_key_mobile_wifi), enable);
    }


    public static boolean isNightMode() {
        return getAnyByKey(NIGHT_MODE, false);
    }

    public static void saveNightMode(boolean on) {
        putAnyCommit(NIGHT_MODE, on);
    }


    public static int getFontSize() {
        return getAnyByKey(DESKTOP_LYRIC_SIZE, 30);
    }

    public static void saveFontSize(int size) {
        putAnyCommit(DESKTOP_LYRIC_SIZE, size);
    }


    public static void saveFontColor(int color) {
        putAnyCommit(DESKTOP_LYRIC_COLOR, color);
    }

    public static int getFontColor() {
        return getAnyByKey(DESKTOP_LYRIC_COLOR, Color.RED);
    }

    /**
     * -------------------------------------------------------
     * <p>底层操作
     * -------------------------------------------------------
     */
    public static boolean getAnyByKey(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static void putAnyCommit(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    public static float getAnyByKey(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public static void putAnyCommit(String key, float value) {
        getPreferences().edit().putFloat(key, value).apply();
    }

    public static int getAnyByKey(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static void putAnyCommit(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    public static long getAnyByKey(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static void putAnyCommit(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    public static String getAnyByKey(String key, @Nullable String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static void putAnyCommit(String key, @Nullable String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(SolidApplication.getInstance());
    }
}
