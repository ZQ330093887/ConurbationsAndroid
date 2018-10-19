package com.test.admin.conurbations.di.module;

import android.app.Activity;


import com.test.admin.conurbations.di.annotation.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ZQiong on 2018/10/10.
 */
@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @ActivityScope
    @Provides
    public Activity provideActivity() {
        return mActivity;
    }
}
