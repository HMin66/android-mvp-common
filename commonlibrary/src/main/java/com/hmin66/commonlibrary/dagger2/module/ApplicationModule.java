package com.hmin66.commonlibrary.dagger2.module;

import android.content.Context;

import com.hmin66.commonlibrary.base.mvp.BaseMvpApplication;
import com.hmin66.commonlibrary.dagger2.scope.ContextLife;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/10/15 0015
 * github：
 */
@Module
public class ApplicationModule {
    private BaseMvpApplication mApplication;

    public ApplicationModule(BaseMvpApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    @ContextLife("Application")
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    public BaseMvpApplication provideApplication() {
        return mApplication;
    }
}
