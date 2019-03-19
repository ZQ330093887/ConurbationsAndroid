package com.test.admin.conurbations.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.MainActivity;
import com.test.admin.conurbations.annotations.ViewNamingRuleXMLParserHandler;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.di.component.AppComponent;
import com.test.admin.conurbations.di.component.DaggerAppComponent;
import com.test.admin.conurbations.di.module.AppModule;
import com.test.admin.conurbations.model.api.BaseApiImpl;
import com.test.admin.conurbations.model.entity.HotSearchBean;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.download.PlaylistLoader;
import com.test.admin.conurbations.utils.download.TasksManager;

import org.litepal.LitePal;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by zhouqiong on 2017/4/1.
 */
public class SolidApplication extends Application {
    private static SolidApplication mInstance;
    public static List<?> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    public static Typeface songTi; // 宋体
    private PlayManager.ServiceToken mToken;
    private AppComponent appComponent;
    public static int count = 0;
    public static int Activitycount = 0;
    public Point screenSize = new Point();
    public static List<HotSearchBean> hotSearchList;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        ToastUtils.init(this);
        AppUtils.init(mInstance);
        LitePal.initialize(this);
        //自定义注入框架
//        loadViewNamingRule();
//        registerListener();
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

    private void loadViewNamingRule() {
        try {
            InputStream inputStream = getResources().getAssets().open("view_naming_rule.xml");
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            ViewNamingRuleXMLParserHandler parserHandler = new ViewNamingRuleXMLParserHandler();
            parser.parse(inputStream, parserHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        Beta.installTinker();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


    /**
     * 注册监听
     */
    private void registerListener() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Activitycount++;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) { //后台切换到前台
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>App切到前台");
                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>App切到后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Activitycount--;
                if (Activitycount == 0) {
                    LogUtil.d(">>>>>>>>>>>>>>>>>>>APP 关闭");
//                    if (socketManager != null) {
//                        socketManager.toggleSocket(false);
//                    }
                }
            }
        });
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
