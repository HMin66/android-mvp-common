package com.hmin66.commonlibrary.base;

import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hmin66.commonlibrary.service.InitializeService;
import com.hmin66.commonlibrary.utils.AccountHelper;
import com.hmin66.commonlibrary.utils.AppUtils;
import com.hmin66.commonlibrary.utils.CrashHandler;
import com.hmin66.commonlibrary.utils.T;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/6/12.
 */

public abstract class BaseApplication extends MultiDexApplication {

//    private ApplicationComponent mApplicationComponent;

//    public ApplicationComponent getApplicationComponent() {
//        return mApplicationComponent;
//    }

//    private void initApplicationComponent() {
//        mApplicationComponent = DaggerApplicationComponent.builder()
//                .applicationModule(new ApplicationModule(this))
//                .httpModule(new HttpModule())
//                .build();
//        mApplicationComponent.injectApplication(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (AppUtils.isMainProcess(this)){
            Log.d("BaseApplication", "onCreate: " + BuildConfig.DEBUG);
            //        initApplicationComponent();
            ARouter.init(this);
            if(BuildConfig.DEBUG){ //如果在debug模式下
                // 打印日志,默认关闭
                ARouter.openLog();
                // 开启调试模式，默认关闭(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                ARouter.openDebug();
                // 打印日志的时候打印线程堆栈
                ARouter.printStackTrace();
            }
            AccountHelper.init(this);
            T.init(this);
            //初始化错误收集
            CrashHandler.init(new CrashHandler(this));
            AutoSizeConfig.getInstance()
                    .setCustomFragment(true);
            //在子线程中完成其他初始化
            InitializeService.start(this);
        }
    }
}
