package com.hmin66.commonlibrary.http.interceptor;


import com.hmin66.commonlibrary.utils.AccountHelper;
import com.hmin66.commonlibrary.utils.AppUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 发送请求拦截器 设置一些公用的请求头
 * Created by aojiaoqiang on 2018/1/31.
 */

public class RequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();//获取请求
        Request tokenRequest = null;
        if (!AccountHelper.getInstance().isLogin()) {
            //对 token 进行判空，如果为空，则不进行修改
            return chain.proceed(originalRequest);
        }
        tokenRequest = originalRequest.newBuilder()//往请求头中添加 token 字段
                // 插入语言版本
                .header("Accept-Language", AppUtils.getLocal())
                .header("Authorization","Bearer "+ AccountHelper.getInstance().getToken())
                .build();

        return chain.proceed(tokenRequest);
    }

}
