package com.hmin66.commonlibrary.dagger2.android.module;

import android.app.Application;

import com.google.gson.Gson;
import com.hmin66.commonlibrary.utils.ACache;
import com.hmin66.commonlibrary.utils.AccountHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 描述： 全局配置
 * 作者： 天天童话丶
 * 时间： 2018/12/14.
 */
@Module
public class GlobalConfigModule {

    private Application mApplication;

    public GlobalConfigModule(Application application){
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    public ACache provideACache() {
        return ACache.get(mApplication);
    }

    @Singleton
    @Provides
    public AccountHelper provideAccountHelper() {
        return AccountHelper.init(mApplication);
    }

}
