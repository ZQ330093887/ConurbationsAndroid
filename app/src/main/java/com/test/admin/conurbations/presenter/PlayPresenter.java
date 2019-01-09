package com.test.admin.conurbations.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.test.admin.conurbations.activitys.IPlayContract;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.MusicPlayerService;
import com.test.admin.conurbations.player.playback.PlayProgressListener;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.imageUtils.ImageUtil;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class PlayPresenter extends BasePresenter<IPlayContract> implements PlayProgressListener {

    @Override
    public void onProgressUpdate(long position, long duration) {
        mvpView.updateProgress(position, duration);
    }

    @Inject
    public PlayPresenter() {
    }

    @Override
    public void attachView(IPlayContract mvpView) {
        super.attachView(mvpView);
        MusicPlayerService.addProgressListener(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        MusicPlayerService.removeProgressListener(this);
    }


    public void updateNowPlaying(Music music, boolean isInit, Activity context) {
        mvpView.showNowPlaying(music);
        SaveBitmapUtils.loadBigImageView(context, music, bitmap -> {
            new Thread(() -> {
                Drawable blur = ImageUtil.createBlurredImageFromBitmap(bitmap, 12);
                context.runOnUiThread(() -> mvpView.setPlayingBg(blur, isInit));
            }).start();
        });
    }
}
