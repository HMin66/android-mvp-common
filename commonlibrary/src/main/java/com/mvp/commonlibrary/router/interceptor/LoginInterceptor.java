package com.mvp.commonlibrary.router.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/5/16.
 */

@Interceptor(priority = 10)
public class LoginInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
//        if (postcard.getPath().equals(ARouterPath.MainActivity)
//                || postcard.getPath().equals(ARouterPath.LoginActivity)) {
//            // 跳转首页不拦截 跳转登录不拦截
//            callback.onContinue(postcard);
//            return;
//        }
//
//        Log.e("HMin66", "LoginInterceptor.process isLogin：" + AccountHelper.getInstance().isLogin());
//        if (!AccountHelper.getInstance().isLogin()
//                && !postcard.getPath().equals(ARouterPath.LoginActivity)){
//                callback.onInterrupt(new RuntimeException("账号未登录"));
//                ARouter.getInstance().build(ARouterPath.LoginActivity).navigation();
//        } else {
            callback.onContinue(postcard);
//        }
    }

    @Override
    public void init(Context context) {

    }
}
