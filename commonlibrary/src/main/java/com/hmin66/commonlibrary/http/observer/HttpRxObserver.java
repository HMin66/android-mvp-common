package com.hmin66.commonlibrary.http.observer;


import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hmin66.commonlibrary.http.exception.ApiException;
import com.hmin66.commonlibrary.http.exception.ERROR;
import com.hmin66.commonlibrary.http.retrofit.HttpRequestListener;
import com.hmin66.commonlibrary.http.retrofit.RxActionManagerImpl;
import com.hmin66.commonlibrary.http.server.ServerResponseCode;
import com.hmin66.commonlibrary.router.path.ARouterPath;
import com.hmin66.commonlibrary.utils.AccountHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * 适用Retrofit网络请求Observer(监听者)
 * 备注:
 * 1.重写onSubscribe，添加请求标识
 * 2.重写onError，封装错误/异常处理，移除请求
 * 3.重写onNext，移除请求
 * 4.重写cancel，取消请求
 */
public abstract class HttpRxObserver<T> implements Observer<T>, HttpRequestListener {

    private String mTag;//请求标识

    public HttpRxObserver() { }

    public HttpRxObserver(String tag) {
        this.mTag = tag;
    }

    @Override
    public void onError(Throwable e) {
        RxActionManagerImpl.getInstance().remove(mTag);
        // 这里做错误处理，如果是api错误 就是自己和服务器定义好的错误就直接返回，否者就返回统一的错误码
        if (e instanceof ApiException) {
            onError((ApiException) e);
            if (((ApiException) e).getCode() == ServerResponseCode.ERR_INVALID_TOKEN){
                // token失效
                ARouter.getInstance()
                        .build(ARouterPath.LoginActivity)
                        .navigation();
                AccountHelper.getInstance().exitLogin();
            }
        } else {
            onError(new ApiException(e, ERROR.UN_KNOWN_ERROR));
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onNext(@NonNull T t) {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().remove(mTag);
        }
        if ("".equals(t)){
            if ("".getClass().getName().equals(getClassT(this,0).getName())){
                onSuccess(t);
            } else {
                // 数据为null
                onError(new ApiException(ERROR.ANALYTIC_SERVER_DATA_ERROR, "解析(服务器)数据错误"));
            }
        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().add(mTag, d);
        }
        onStart(d);
    }

    @Override
    public void cancel() {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().cancel(mTag);
        }
    }

    /**
     * 是否已经处理
     *
     * @author ZhongDaFeng
     */
    public boolean isDisposed() {
        if (TextUtils.isEmpty(mTag)) {
            return true;
        }
        return RxActionManagerImpl.getInstance().isDisposed(mTag);
    }

    protected abstract void onStart(Disposable d);

    /**
     * 错误/异常回调
     *
     * @author ZhongDaFeng
     */
    protected abstract void onError(ApiException e);

    /**
     * 成功回调
     *
     * @author ZhongDaFeng
     */
    protected abstract void onSuccess(T response);


    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceT(Object o, int index) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);
    }


    /**
     * 获取类上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actType = parameterizedType.getActualTypeArguments()[index];
            return checkType(actType, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

}
