package com.test.admin.conurbations.views.desktop;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.MusicPlayerService;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.SaveBitmapUtils;

/**
 * Created by ZQiong on 2018/12/13.
 */
public class StandardWidget extends BaseWidget {
    private boolean isFirstCreate = true;

    @Override
    void onViewsUpdate(Context context, RemoteViews remoteViews, ComponentName serviceName, Bundle extras) {
        LogUtil.e("BaseWidget", "接收到广播------------- onViewsUpdate");
        if (isFirstCreate) {
            remoteViews.setOnClickPendingIntent(R.id.iv_next, PendingIntent.getService(
                    context,
                    BaseWidget.REQUEST_NEXT,
                    new Intent(context, MusicPlayerService.class)
                            .setAction(MusicPlayerService.ACTION_NEXT)
                            .setComponent(serviceName),
                    0));

            remoteViews.setOnClickPendingIntent(R.id.iv_prev, PendingIntent.getService(
                    context,
                    BaseWidget.REQUEST_PREV,
                    new Intent(context, MusicPlayerService.class)
                            .setAction(MusicPlayerService.ACTION_PREV)
                            .setComponent(serviceName),
                    0
            ));
            remoteViews.setOnClickPendingIntent(R.id.iv_play_pause, PendingIntent.getService(
                    context,
                    BaseWidget.REQUEST_PLAYPAUSE,
                    new Intent(context, MusicPlayerService.class)
                            .setAction(MusicPlayerService.ACTION_PLAY_PAUSE)
                            .setComponent(serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ));
            remoteViews.setOnClickPendingIntent(R.id.iv_cover, PendingIntent.getActivity(
                    context,
                    0,
                    NavigationHelper.getNowPlayingIntent(context)
                            .setComponent(serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ));

            remoteViews.setOnClickPendingIntent(R.id.iv_lyric, PendingIntent.getService(
                    context,
                    0,
                    NavigationHelper.getLyricIntent(context)
                            .setComponent(serviceName),
                    PendingIntent.FLAG_UPDATE_CURRENT
            ));
            isFirstCreate = false;
        }

        if (extras != null) {
            remoteViews.setImageViewResource(R.id.iv_play_pause,
                    extras.getBoolean("play_status", false)
                            ? R.drawable.ic_pause : R.drawable.ic_play);
        }

        if (MusicPlayerService.getInstance() != null) {
            Music music = MusicPlayerService.getInstance().getPlayingMusic();

            if (music == null) return;

            remoteViews.setTextViewText(R.id.tv_title, music.title + " - " + music.artist);
            SaveBitmapUtils.loadImageViewByMusic(context, music, artwork -> {
                if (artwork != null) {
                    remoteViews.setImageViewBitmap(R.id.iv_cover, artwork);
                } else {
                    remoteViews.setImageViewResource(R.id.iv_cover, R.mipmap.default_cover);
                }
            });
        }

    }

    @Override
    int getLayoutRes() {
        return R.layout.widget_standard;
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        LogUtil.e("BaseWidget", "接收到广播------------- 第一次创建");
        isFirstCreate = true;
        Intent intent = new Intent(context, MusicPlayerService.class);
        context.startService(intent);
    }
}
