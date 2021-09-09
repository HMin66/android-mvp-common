package com.mvp.commonlibrary.aspectj;

import android.util.Log;
import android.view.View;

import com.mvp.commonlibrary.R;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Calendar;

/**
 * 作者：Administrator on 2018/1/26 17:27
 * 描述：CheckNet切点的切面处理类
 */

@Aspect
public class SingleClickAspect {

    public static final String TAG="SingleClickAspect";
    public static final int MIN_CLICK_DELAY_TIME = 600;
    static int TIME_TAG = R.id.click_time;

    @Pointcut("execution(@SingleClick * *(..))")//方法切入点
    public void methodAnnotated(){}

    @Around("methodAnnotated()")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        View view=null;
        for (Object arg: joinPoint.getArgs()) {
            if (arg instanceof View) view= ((View) arg);
        }
        if (view!=null){
            Object tag=view.getTag(TIME_TAG);
            long lastClickTime= (tag!=null)? (long) tag :0;
            Log.d(TAG, "lastClickTime:" + lastClickTime);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {//过滤掉600毫秒内的连续点击
                view.setTag(TIME_TAG, currentTime);
                Log.d(TAG, "currentTime:" + currentTime);
                joinPoint.proceed();//执行原方法
            }
        }
    }
}
