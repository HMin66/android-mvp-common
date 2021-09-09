package com.hmin66.commonlibrary.base.mvp;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/12.
 */

public class BasePresenter<V extends BaseContract.IView>
        implements BaseContract.IPresenter<V> {

    private WeakReference<V> mView;

//    protected M mModel;
//
//    public BasePresenter(M model) {
//        this.mModel = model;
//    }

    @Override
    public void attachView(@NonNull V iView) {
        Logger.e("attachView: ");
        this.mView = new WeakReference(iView);
    }

    /**
     * 改用RxLifecycle处理Presenter内存泄漏，在该方法中在调用mView=null，容易造成presenter内空指针异常
     */
    @Override
    public void detachView() {
        Logger.e("detachView: ");

//        if (this.mView != null){
//            this.mView.clear();
//            this.mView = null;
//            System.gc();
//        }

//        if (mModel != null) {
//            mModel.onDestroy();
//            this.mModel = null;
//        }
    }

    @Override
    public V getView() {
        if(mView == null) throw new IllegalStateException("view not attached");
        else return mView.get();
    }
}
