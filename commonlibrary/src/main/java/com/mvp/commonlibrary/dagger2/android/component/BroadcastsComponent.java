package com.mvp.commonlibrary.dagger2.android.component;

import android.content.BroadcastReceiver;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;


@Subcomponent(modules = {
        AndroidInjectionModule.class,
})
public interface BroadcastsComponent extends AndroidInjector<BroadcastReceiver> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BroadcastReceiver> {
    }
}
