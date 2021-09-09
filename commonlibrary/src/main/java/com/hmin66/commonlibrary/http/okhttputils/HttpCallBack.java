package com.hmin66.commonlibrary.http.okhttputils;

import android.app.Activity;
import android.content.Context;

import com.hmin66.commonlibrary.R;
import com.google.gson.Gson;
import com.hmin66.commonlibrary.dialog.BaseDialog;
import com.hmin66.commonlibrary.http.server.HttpResponse;
import com.hmin66.commonlibrary.http.server.ServerResponseCode;
import com.hmin66.commonlibrary.utils.AccountHelper;
import com.hmin66.commonlibrary.utils.T;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/5/9.
 */

public abstract class HttpCallBack<M> extends StringCallback {

    @Inject
    Gson mGson;

    private WeakReference<Activity> mContext;
    private BaseDialog mLoadingDialog;
    private boolean isShowLoading;

    public HttpCallBack(Activity activity) {
        this.mContext = new WeakReference(activity);
    }

    public HttpCallBack(Context context, boolean isShowLoading) {
        this.mContext = new WeakReference(context);
        this.isShowLoading = isShowLoading;
    }

    @Override
    public void onResponse(String result, int id) {
        if (mGson == null){
            mGson = new Gson();
        }
        //Log.e("HMin66", "HttpCallBack.onResponse Result: " + result);
        HttpResponse<M> response = mGson.fromJson(result, HttpResponse.class);
        //Log.e("HMin66", "HttpCallBack.onResponse Response:" + response.toString());
        if (ServerResponseCode.ERR_INVALID_TOKEN == response.getCode()) {
            // token失效 强制登录
            T.showShort(mContext.get(), "请求失败：" + response.getMsg() + " ,错误码：" + response.getCode());
            AccountHelper.getInstance().setTokenInvalid();
        } else if (ServerResponseCode.SUCCESS != response.getCode()) {
            // 请求失败
            T.showShort(mContext.get(), "请求失败：" + response.getMsg() + " ,错误码：" + response.getCode());
        } else {
            Class<M> objResult = (Class<M>) analysisClazzInfo(this);
            if (objResult != null) {
                //有添加泛型
                M mResult = mGson.fromJson(mGson.toJson(response.getResult()), objResult);
                onSuccess(mResult, id);
            } else {
                //没有添加泛型
            }
        }
    }

    public void onSuccess(M response, int id){

    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        //开启 loadingDialog
        if (isShowLoading) {
            mLoadingDialog = new BaseDialog.Builder(mContext.get())
                    .setContentView(R.layout.dialog_network_request_loading)
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }



    private static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        if (genType != null) {
            ParameterizedType parameterizedType = null;
            try{
                parameterizedType = (ParameterizedType) genType;
                if (parameterizedType != null) {
                    Type[] params = parameterizedType.getActualTypeArguments();
                    return (Class<?>) params[0];
                }
            }catch (Exception e){
                return null;
            }
        }

        // 没传递泛型
        return null;
    }
}
