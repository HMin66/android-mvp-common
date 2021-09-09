package com.mvp.commonlibrary.dagger2.android.component;

import android.app.Application;
import android.content.Context;

import com.mvp.commonlibrary.constant.Api;
import com.mvp.commonlibrary.dagger2.android.module.GlobalConfigModule;
import com.mvp.commonlibrary.dagger2.module.ApplicationModule;
import com.mvp.commonlibrary.dagger2.module.HttpModule;
import com.mvp.commonlibrary.dagger2.scope.ContextLife;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/13.
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        HttpModule.class,
        GlobalConfigModule.class})
public interface BaseApplicationComponent {

    @ContextLife("Application")
    Context getContext();

    Application injectApplication(Application application);

    Api getApi();
}
