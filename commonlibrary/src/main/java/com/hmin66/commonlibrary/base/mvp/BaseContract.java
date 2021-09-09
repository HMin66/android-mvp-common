package com.hmin66.commonlibrary.base.mvp;

import android.support.annotation.NonNull;

import com.hmin66.commonlibrary.http.server.HttpListResponse;
import com.hmin66.commonlibrary.http.server.HttpResponse;

import io.reactivex.ObservableTransformer;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/4/11.
 */
public interface BaseContract {

    interface IPresenter<T extends IView> {

        /**
         * 注入View，使之能够与View相互响应
         *
         * @param iView
         */
        void attachView(@NonNull T iView);

        /**
         * 释放资源
         */
        void detachView();

        T getView();
    }

    interface IView {
        /**
         * 显示loading
         */
        void showLoading();

        /**
         * 关闭loading
         */
        void closeLoading();

        /**
         * 销毁loading
         */
        void destroyLoading();

        /**
         * 显示toast
         *
         * @param msg
         */
        void showToast(String msg);

        /**
         *  切换线程
         * @param <T>
         * @return
         */
        <T> ObservableTransformer<T, T> applySchedulers();

        /**
         * 切换线程 + 解析json
         * @param <T>
         * @return
         */
        <T> ObservableTransformer<HttpResponse<T>, T> applySchedulersHasMap();

        <T> ObservableTransformer<HttpListResponse<T>, T> applySchedulersList();

        <T> ObservableTransformer<HttpListResponse<T>, HttpListResponse.DataBean<T>> applySchedulersListData();
    }

    interface IModel{
        void onDestroy();
    }
}
