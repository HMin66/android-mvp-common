package com.hmin66.commonlibrary.base.mvp;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.hmin66.commonlibrary.R;
import com.hmin66.commonlibrary.base.BaseListFragment;
import com.hmin66.commonlibrary.dialog.BaseDialog;
import com.hmin66.commonlibrary.http.function.HttpResultFunction;
import com.hmin66.commonlibrary.http.function.ServerResultFunction;
import com.hmin66.commonlibrary.http.function.ServerResultListDataFunction;
import com.hmin66.commonlibrary.http.function.ServerResultListFunction;
import com.hmin66.commonlibrary.http.server.HttpListResponse;
import com.hmin66.commonlibrary.http.server.HttpResponse;
import com.hmin66.commonlibrary.utils.T;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/14.
 */
public abstract class BaseMvpListFragment<P extends BasePresenter> extends BaseListFragment
        implements BaseContract.IView, HasSupportFragmentInjector {

    private BaseDialog mLoadingDialog;

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Inject
    protected P mPresenter;

    @Override
    public void onAttach(Activity activity) {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
        if (mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null){
            mPresenter.detachView();
            mPresenter = null;
            System.gc();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    @Override
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(BaseMvpListFragment.this.<T>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                        .compose(BaseMvpListFragment.this.<T>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                        .compose(BaseMvpListFragment.this.<T>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                        .compose(BaseMvpListFragment.this.<HttpListResponse.DataBean<T>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
        T.showShort(mContext, msg);
    }
}
