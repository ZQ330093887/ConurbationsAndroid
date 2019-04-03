package com.test.admin.conurbations.widget;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.ss.android.common.applog.GlobalContext;
import com.ss.android.common.applog.UserInfo;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.MainActivity;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.di.component.AppComponent;
import com.test.admin.conurbations.di.component.DaggerAppComponent;
import com.test.admin.conurbations.di.module.AppModule;
import com.test.admin.conurbations.model.api.BaseApiImpl;
import com.test.admin.conurbations.model.entity.HotSearchBean;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.download.PlaylistLoader;
import com.test.admin.conurbations.utils.download.TasksManager;
import com.test.admin.conurbations.utils.hookpms.ServiceManagerWraper;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by zhouqiong on 2017/4/1.
 */
public class SolidApplication extends Application {
    private static SolidApplication mInstance;
    public static List<?> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    public static Typeface songTi; // 宋体
    private AppComponent appComponent;
    public static int count = 0;
    public Point screenSize = new Point();
    public static List<HotSearchBean> hotSearchList;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ServiceManagerWraper.hookPMS(this.getApplicationContext());
        ToastUtils.init(this);
        AppUtils.init(mInstance);
        LitePal.initialize(this);
        initDB();
        initFileDownload();
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            manager.getDefaultDisplay().getSize(screenSize);
        }
        BaseApiImpl.INSTANCE.initWebView(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        FeedbackAPI.initAnnoy(this, "23601404");

        setBugly();

        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles = new ArrayList(list1);

        songTi = Typeface.createFromAsset(getAssets(), "SongTi.TTF");

        Fresco.initialize(this);
        GlobalContext.setContext(getApplicationContext()); //Hook 抖音
        try {
            System.loadLibrary("userinfo");//抖音&火山
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserInfo.setAppId(2);
        UserInfo.initUser("a3668f0afac72ca3f6c1697d29e0e1bb1fef4ab0285319b95ac39fa42c38d05f");
    }

    private void initDB() {
        PlaylistLoader.createDefaultPlaylist(Constants.PLAYLIST_QUEUE_ID, "播放队列");
        PlaylistLoader.createDefaultPlaylist(Constants.PLAYLIST_HISTORY_ID, "播放历史");
        PlaylistLoader.createDefaultPlaylist(Constants.PLAYLIST_LOVE_ID, "我的收藏");
    }

    /**
     * 初始化文件下载
     */
    private void initFileDownload() {
        FileDownloadLog.NEED_LOG = true;
        FileDownloader.setup(this);
    }

    public static SolidApplication getInstance() {
        return mInstance;
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        Beta.installTinker();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogUtil.d("onTerminate");
        super.onTerminate();
        //结束下载任务
        TasksManager.onDestroy();
        FileDownloader.getImpl().pauseAll();
    }

    /**
     * bugly简单配置
     * app升级千万记得要杀死app进程这个不是热更新
     */
    private void setBugly() {
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(this, "df40649721", true);
    }
}
