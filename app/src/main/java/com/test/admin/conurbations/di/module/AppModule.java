package com.test.admin.conurbations.di.module;


import com.test.admin.conurbations.widget.SolidApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ZQiong on 2018/10/10.
 */
@Module
public class AppModule {

    private final SolidApplication application;

    public AppModule(SolidApplication application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public SolidApplication provideApplication() {
        return application;
    }

//    @Singleton
//    @Provides
//    public LocalDataSourceManager providerLocalDataSourceManager(
//            XmlCacheHelper xmlCacheHelper, RealmDBHelper realmDBHelper) {
//        return new LocalDataSourceManager(xmlCacheHelper, realmDBHelper);
//    }
//
//    @Singleton
//    @Provides
//    public RemoteDataSourceManager providerRemoteDataSourceManager(
//            RetrofitHelper retrofitHelper) {
//        return new RemoteDataSourceManager(retrofitHelper, retrofitHelper, retrofitHelper);
//    }
}
