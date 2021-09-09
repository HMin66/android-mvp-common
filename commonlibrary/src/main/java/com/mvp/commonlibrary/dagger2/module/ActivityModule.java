package com.mvp.commonlibrary.dagger2.module;

import android.content.Context;

import com.mvp.commonlibrary.base.BaseActivity;
import com.mvp.commonlibrary.dagger2.scope.ContextLife;
import com.mvp.commonlibrary.dagger2.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/10/15 0015
 * github：
 */
@Module
public class ActivityModule {
    private BaseActivity mActivity;

    public ActivityModule(BaseActivity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public BaseActivity provideActivity() {
        return mActivity;
    }

}
