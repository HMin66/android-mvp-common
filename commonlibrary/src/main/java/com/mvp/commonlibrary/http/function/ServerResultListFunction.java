package com.mvp.commonlibrary.http.function;

import com.mvp.commonlibrary.http.exception.ServerException;
import com.mvp.commonlibrary.http.server.HttpListResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 服务器结果处理函数
 */
public class ServerResultListFunction<T> implements Function<HttpListResponse<T>, T> {

    @Override
    public T apply(@NonNull HttpListResponse<T> response) throws Exception {
        // 这里处理服务器返回的是不是错误
        // code == 1就是处理成功了，否者就是处理失败 或者登录过期
        if (!response.isSuccess()) {
            throw new ServerException(response.getCode(), response.getMsg());
        }
        return response.getResult().getRecords() == null ? (T)"" : response.getResult().getRecords();
    }
}
