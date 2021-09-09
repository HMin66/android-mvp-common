package com.mvp.commonlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @description: BaseFragment
 * @data 2018/1/26-11:11
 * @author: 天天童话丶
 */
public abstract class BaseFragment extends RxFragment {

    private View mRootView;
    private Unbinder mUnbinder;
    private Bundle mBundle;
    protected Bundle mSavedInstanceState;

    // 保存最后一次显示时间，防止onFragmentVisibleChange方法多次调用
    private long mLastFragmentVisibleTime;
    // 当前fragment是否可见
    private boolean isFragmentVisible;
    // 是否第一次可见
    private boolean isFirstVisible;

    protected Activity mContext;

    private boolean mFinished;

    protected boolean isFinishing() {
        return mFinished;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        mBundle = getArguments();
        initBundle(mBundle);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            // 防止布局重复加载
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
        }

        mSavedInstanceState = savedInstanceState;
        initView(mRootView);

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                onFragmentFirstVisible();
                onFragmentVisibleChange(true, isFirstVisible);
                isFirstVisible = false;
            }
            isFragmentVisible = true;
            mLastFragmentVisibleTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && System.currentTimeMillis() - mLastFragmentVisibleTime > 1000){
            onFragmentVisibleChange(true, isFirstVisible);
            isFragmentVisible = !isHidden();
            mLastFragmentVisibleTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onFragmentVisibleChange(false, isFirstVisible);
        isFragmentVisible = false;
    }

    /**
     * fragment单纯的替换 不会调用该方法 只有配合viewpager情况下才会调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (mRootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
            return;
        }
        if (isVisibleToUser) {
            if (System.currentTimeMillis() - mLastFragmentVisibleTime > 1000) {
                onFragmentVisibleChange(true, isFirstVisible);
                isFragmentVisible = true;
                mLastFragmentVisibleTime = System.currentTimeMillis();
            }
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false, isFirstVisible);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁view的时候解除view绑定
        mUnbinder.unbind();
        initVariable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRootView = null;
        mFinished = true;
    }

    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    protected void GoActivity(Context context, Class clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected View getRootView() {
        return mRootView;
    }

    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    protected void initBundle(Bundle arguments){
        if (arguments == null) return;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View rootView);

    protected abstract void initData();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // true 页面隐藏hide() false 是页面show()
        if (hidden) {
            onFragmentVisibleChange(false, isFirstVisible);
            isFragmentVisible = false;
        } else {
            if (System.currentTimeMillis() - mLastFragmentVisibleTime > 1000){
                onFragmentVisibleChange(true, isFirstVisible);
                isFragmentVisible = true;
                mLastFragmentVisibleTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     * <p>
     * 可在该回调方法里进行一些ui显示与隐藏
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible, boolean isFirstVisible) {
    }

    /**
     * 在fragment首次可见时回调，可用于加载数据，防止每次进入都重复加载数据
     */
    protected void onFragmentFirstVisible() {
        initData();
    }
}
