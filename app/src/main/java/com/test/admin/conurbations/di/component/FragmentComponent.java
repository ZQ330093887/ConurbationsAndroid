package com.test.admin.conurbations.di.component;

import android.app.Activity;

import com.test.admin.conurbations.di.annotation.FragmentScope;
import com.test.admin.conurbations.di.module.FragmentModule;
import com.test.admin.conurbations.fragments.GanHuoFragment;
import com.test.admin.conurbations.fragments.GankDayFragment;
import com.test.admin.conurbations.fragments.NbaIndexFragment;
import com.test.admin.conurbations.fragments.NewsInfoIndexFragment;
import com.test.admin.conurbations.fragments.NewsInfoListFragment;
import com.test.admin.conurbations.fragments.PrettyPicturesFragment;
import com.test.admin.conurbations.fragments.PrettyPicturesListFragmentList;
import com.test.admin.conurbations.fragments.SearchFragment;
import com.test.admin.conurbations.fragments.SouGouImageFragment;
import com.test.admin.conurbations.fragments.WelfareFragment;

import dagger.Component;

/**
 * Created by ZQiong on 2018/10/10.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(GankDayFragment fragment);

    void inject(GanHuoFragment fragment);

    void inject(NbaIndexFragment fragment);

    void inject(NewsInfoIndexFragment fragment);

    void inject(NewsInfoListFragment fragment);

    void inject(PrettyPicturesListFragmentList fragment);

    void inject(SouGouImageFragment fragment);

    void inject(WelfareFragment fragment);

    void inject(SearchFragment fragment);

    void inject(PrettyPicturesFragment fragment);

}
