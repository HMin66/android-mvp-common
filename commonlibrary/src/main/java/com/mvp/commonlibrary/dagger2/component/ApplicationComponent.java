package com.mvp.commonlibrary.dagger2.component;

import android.app.Application;
import android.content.Context;

import com.mvp.commonlibrary.constant.Api;
import com.mvp.commonlibrary.dagger2.module.ApplicationModule;
import com.mvp.commonlibrary.dagger2.module.HttpModule;
import com.mvp.commonlibrary.dagger2.scope.ContextLife;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/10/15 0015
 * github：
 */
@Singleton
@Component(modules = {ApplicationModule.class, HttpModule.class})
public interface ApplicationComponent {

    @ContextLife("Application")
    Context getContext();

    Application injectApplication(Application application);

    Api getApi();
}
