package com.mvp.commonlibrary.base.mvp;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;

import com.mvp.commonlibrary.base.BaseApplication;
import com.mvp.commonlibrary.constant.Api;
import com.mvp.commonlibrary.dagger2.android.module.GlobalConfigModule;
import com.mvp.commonlibrary.dagger2.module.ApplicationModule;
import com.mvp.commonlibrary.dagger2.module.HttpModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasContentProviderInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/13.
 */

public abstract class BaseMvpApplication extends BaseApplication implements
        HasActivityInjector,
        HasFragmentInjector,
        HasSupportFragmentInjector,
        HasBroadcastReceiverInjector,
        HasServiceInjector,
        HasContentProviderInjector{

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject
    DispatchingAndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector;
    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> broadcastReceiverInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceInjector;
    @Inject
    DispatchingAndroidInjector<ContentProvider> contentProviderInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return broadcastReceiverInjector;
    }

    @Override
    public AndroidInjector<ContentProvider> contentProviderInjector() {
        return contentProviderInjector;
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceInjector;
    }

    @Override
    public AndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    /**
     * 这是类库底层的injectApp代码示例，你应该在你的Module中重写该方法
     */
    protected abstract void injectApplication();

    @Override
    public void onCreate() {
        super.onCreate();
        injectApplication();
    }

    protected ApplicationModule getApplicationModule() {
        return new ApplicationModule(this);
    }

    protected HttpModule getHttpModule(Api api) {
        return new HttpModule(this, api);
    }

    protected GlobalConfigModule getGlobalConfigModule() {
        return new GlobalConfigModule(this);
    }
}
