package com.test.admin.conurbations.di.component;


import com.test.admin.conurbations.di.module.AppModule;
import com.test.admin.conurbations.widget.SolidApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ZQiong on 2018/10/10.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    SolidApplication getApplication();

//    LocalDataSourceManager getLocalDataSourceManager();
//
//    RemoteDataSourceManager getRemoteDataSourceManager();
}
