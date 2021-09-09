package com.hmin66.commonlibrary.dagger2.component;

import android.content.Context;

import com.hmin66.commonlibrary.dagger2.module.ServiceModule;
import com.hmin66.commonlibrary.dagger2.scope.ContextLife;
import com.hmin66.commonlibrary.dagger2.scope.PerService;

import dagger.Component;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/10/15 0015
 * github：
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();

}
