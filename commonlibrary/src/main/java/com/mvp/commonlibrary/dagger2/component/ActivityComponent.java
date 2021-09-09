package com.mvp.commonlibrary.dagger2.component;

import android.content.Context;

import com.mvp.commonlibrary.base.BaseActivity;
import com.mvp.commonlibrary.dagger2.module.ActivityModule;
import com.mvp.commonlibrary.dagger2.scope.ContextLife;
import com.mvp.commonlibrary.dagger2.scope.PerActivity;

import dagger.Component;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/10/15 0015
 * github：
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    BaseActivity getActivity();

//    void inject(Activity activity);
}
