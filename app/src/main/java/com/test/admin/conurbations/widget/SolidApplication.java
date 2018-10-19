package com.test.admin.conurbations.widget;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.tencent.bugly.Bugly;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.ViewNamingRuleXMLParserHandler;
import com.test.admin.conurbations.di.component.AppComponent;
import com.test.admin.conurbations.di.component.DaggerAppComponent;
import com.test.admin.conurbations.di.module.AppModule;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.ToastUtils;

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
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ToastUtils.init(this);
        AppUtils.init(mInstance);
        //自定义注入框架
        loadViewNamingRule();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        FeedbackAPI.initAnnoy(this, "23601404");
        Bugly.init(this, "df40649721", true);
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles = new ArrayList(list1);

        songTi = Typeface.createFromAsset(getAssets(), "SongTi.TTF");
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
//        Beta.installTinker();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
