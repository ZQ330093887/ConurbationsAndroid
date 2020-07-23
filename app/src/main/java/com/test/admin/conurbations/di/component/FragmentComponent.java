package com.test.admin.conurbations.di.component;

import android.app.Activity;

import com.test.admin.conurbations.di.annotation.FragmentScope;
import com.test.admin.conurbations.di.module.FragmentModule;
import com.test.admin.conurbations.fragments.DownloadManagerFragment;
import com.test.admin.conurbations.fragments.DownloadedFragment;
import com.test.admin.conurbations.fragments.GanHuoFragment;
import com.test.admin.conurbations.fragments.GankHotFragment;
import com.test.admin.conurbations.fragments.MusicIndexFragment;
import com.test.admin.conurbations.fragments.MvListFragment;
import com.test.admin.conurbations.fragments.NbaIndexFragment;
import com.test.admin.conurbations.fragments.NetPlayListFragment;
import com.test.admin.conurbations.fragments.NudePhotosFragment;
import com.test.admin.conurbations.fragments.PrettyPicturesFragment;
import com.test.admin.conurbations.fragments.PrettyPicturesListFragmentList;
import com.test.admin.conurbations.fragments.SearchFragment;
import com.test.admin.conurbations.fragments.SouGouImageFragment;
import com.test.admin.conurbations.fragments.VideoIndexFragment;
import com.test.admin.conurbations.fragments.VideoListFragment;
import com.test.admin.conurbations.fragments.WelfareFragment;

import dagger.Component;

/**
 * Created by ZQiong on 2018/10/10.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(GankHotFragment fragment);

    void inject(GanHuoFragment fragment);

    void inject(DownloadedFragment fragment);

    void inject(DownloadManagerFragment fragment);

    void inject(NbaIndexFragment fragment);

    void inject(MusicIndexFragment fragment);

    void inject(NetPlayListFragment fragment);

    void inject(PrettyPicturesListFragmentList fragment);

    void inject(SouGouImageFragment fragment);

    void inject(WelfareFragment fragment);

    void inject(SearchFragment fragment);

    void inject(PrettyPicturesFragment fragment);

    void inject(MvListFragment fragment);

    void inject(VideoIndexFragment fragment);

    void inject(NudePhotosFragment fragment);

    void inject(VideoListFragment fragment);
}
