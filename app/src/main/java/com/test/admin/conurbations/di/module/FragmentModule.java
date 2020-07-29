package com.test.admin.conurbations.di.module;

import android.app.Activity;


import androidx.fragment.app.Fragment;

import com.test.admin.conurbations.di.annotation.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ZQiong on 2018/10/10.
 */
@Module
public class FragmentModule {

    private final Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @FragmentScope
    @Provides
    public Activity provideActivity() {
        return mFragment.getActivity();
    }

    @FragmentScope
    @Provides
    public Fragment provideFragment() {
        return mFragment;
    }
}
