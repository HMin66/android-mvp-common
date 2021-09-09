package com.hmin66.commonlibrary.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hmin66.commonlibrary.R;
import com.hmin66.commonlibrary.base.BaseActivity;
import com.hmin66.commonlibrary.dialog.BaseDialog;
import com.hmin66.commonlibrary.http.function.HttpResultFunction;
import com.hmin66.commonlibrary.http.function.ServerResultFunction;
import com.hmin66.commonlibrary.http.function.ServerResultListDataFunction;
import com.hmin66.commonlibrary.http.function.ServerResultListFunction;
import com.hmin66.commonlibrary.http.server.HttpListResponse;
import com.hmin66.commonlibrary.http.server.HttpResponse;
import com.hmin66.commonlibrary.utils.T;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/12.
 */

public abstract class BaseMvpActivity<P extends BasePresenter>
        extends BaseActivity implements BaseContract.IView,
        HasFragmentInjector, HasSupportFragmentInjector {

    private BaseDialog mLoadingDialog;

    @Inject
    public P mPresenter;

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @Inject
    DispatchingAndroidInjector<android.app.Fragment> frameworkFragmentInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    @Override
    public AndroidInjector<android.app.Fragment> fragmentInjector() {
        return frameworkFragmentInjector;
    }

    @Override
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(BaseMvpActivity.this.<T>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public <T> ObservableTransformer<HttpResponse<T>, T> applySchedulersHasMap() {
        return new ObservableTransformer<HttpResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<HttpResponse<T>> upstream) {
                return upstream
                        .map(new ServerResultFunction<T>())
                        .onErrorResumeNext(new HttpResultFunction<T>())
                        .compose(BaseMvpActivity.this.<T>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public <T> ObservableTransformer<HttpListResponse<T>, T> applySchedulersList() {
        return new ObservableTransformer<HttpListResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<HttpListResponse<T>> upstream) {
                return upstream
                        .map(new ServerResultListFunction<T>())
                        .onErrorResumeNext(new HttpResultFunction<T>())
                        .compose(BaseMvpActivity.this.<T>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public <T> ObservableTransformer<HttpListResponse<T>, HttpListResponse.DataBean<T>> applySchedulersListData() {
        return new ObservableTransformer<HttpListResponse<T>, HttpListResponse.DataBean<T>>() {

            @Override
            public ObservableSource<HttpListResponse.DataBean<T>> apply(Observable<HttpListResponse<T>> upstream) {
                return upstream
                        .map(new ServerResultListDataFunction<T>())
                        .onErrorResumeNext(new HttpResultFunction<HttpListResponse.DataBean<T>>())
                        .compose(BaseMvpActivity.this.<HttpListResponse.DataBean<T>>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new BaseDialog.Builder(mContext)
                    .setContentView(R.layout.dialog_network_request_loading)
                    .setCancelable(false)
                    .create();
        }
        mLoadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void destroyLoading(){
        if (mLoadingDialog != null){
            mLoadingDialog = null;
        }
    }

    @Override
    public void showToast(String msg) {
        T.showShort(this, msg);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        if(mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
            System.gc();
        }
    }
}
