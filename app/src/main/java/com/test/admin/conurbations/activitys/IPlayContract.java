package com.test.admin.conurbations.activitys;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.test.admin.conurbations.model.Music;

/**
 * Created by ZQiong on 2018/12/10.
 */
public interface IPlayContract {

    void setPlayingBitmap(Bitmap albumArt);

    void setPlayingBg(Drawable albumArt, boolean isInit);

    void showLyric(String lyric, boolean init);

    void updatePlayStatus(boolean isPlaying);

    void updatePlayMode();

    void updateProgress(long progress, long max);

    void showNowPlaying(Music music);
}
