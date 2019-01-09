package com.test.admin.conurbations.views.desktop;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.widget.RemoteViews;

import com.test.admin.conurbations.player.MusicPlayerService;
import com.test.admin.conurbations.utils.LogUtil;


/**
 * Created Zqiong on 2018.12.13.
 */

public abstract class BaseWidget extends AppWidgetProvider {

    protected static final int REQUEST_NEXT = 1;
    protected static final int REQUEST_PREV = 2;
    protected static final int REQUEST_PLAYPAUSE = 3;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        onUpdate(context, appWidgetManager, appWidgetIds, null);
    }

    private void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Bundle extras) {
        ComponentName serviceName = new ComponentName(context, MusicPlayerService.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getLayoutRes());
        try {
            onViewsUpdate(context, remoteViews, serviceName, extras);
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.e("BaseWidget", "接收到广播-------------" + action);
        if (action != null && action.startsWith("com.cyl.music_lake.")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), this.getClass().getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds, intent.getExtras());
        } else {
            super.onReceive(context, intent);
        }
    }

    abstract void onViewsUpdate(Context context, RemoteViews remoteViews, ComponentName serviceName, Bundle extras);

    abstract @LayoutRes
    int getLayoutRes();
}
