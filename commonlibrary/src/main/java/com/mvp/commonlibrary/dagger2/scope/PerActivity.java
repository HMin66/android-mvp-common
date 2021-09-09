package com.mvp.commonlibrary.dagger2.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/10/15 0015
 * github：
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
