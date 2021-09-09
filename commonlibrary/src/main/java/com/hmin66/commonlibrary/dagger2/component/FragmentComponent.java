package com.hmin66.commonlibrary.dagger2.component;

import android.app.Activity;
import android.content.Context;

import com.hmin66.commonlibrary.dagger2.module.FragmentModule;
import com.hmin66.commonlibrary.dagger2.scope.ContextLife;
import com.hmin66.commonlibrary.dagger2.scope.PerFragment;

import dagger.Component;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/10/15 0015
 * github：
 */
@PerFragment
@Component(modules = FragmentModule.class, dependencies = ApplicationComponent.class)
public interface FragmentComponent {

    @ContextLife("Application")
    Context getContext();

    @ContextLife("Activity")
    Context getActivityContext();

    Activity getActivity();
}
