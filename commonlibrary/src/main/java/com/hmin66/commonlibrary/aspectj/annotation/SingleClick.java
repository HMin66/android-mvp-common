package com.hmin66.commonlibrary.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：Administrator on 2018/1/26 16:25
 * 描述：防止View被连续点击
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//@Retention(RetentionPolicy.CLASS)
public @interface SingleClick {}