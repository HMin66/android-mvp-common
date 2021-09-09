package com.hmin66.commonlibrary.utils;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * 作者：Administrator on 2017/12/15 15:56
 * 描述：
 */

public class FragmentManagerHelper {

    private FragmentManager mFragmentManager;
    private int mContainerViewId;

    public FragmentManagerHelper(@Nullable FragmentManager fm, @IdRes int containerViewId){
        mFragmentManager = fm;
        mContainerViewId = containerViewId;
    }

    public void add(Fragment fragment){
        mFragmentManager.beginTransaction().add(mContainerViewId, fragment).commit();
    }

    public void replace(Fragment fragment){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        // 隐藏所有Fragment
        List<Fragment> childFragments = mFragmentManager.getFragments();
        for (Fragment childFragment : childFragments) {
            ft.hide(childFragment);
        }
        // 容器里没有就添加，否则显示
        if (!childFragments.contains(fragment)){
            ft.add(mContainerViewId, fragment);
        } else {
            ft.show(fragment);
        }

        ft.commit();
    }

    public void replaceAllowingStateLoss(Fragment fragment){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        // 隐藏所有Fragment
        List<Fragment> childFragments = mFragmentManager.getFragments();
        for (Fragment childFragment : childFragments) {
            ft.hide(childFragment);
        }
        // 容器里没有就添加，否则显示
        if (!childFragments.contains(fragment)){
            ft.add(mContainerViewId, fragment);
        } else {
            ft.show(fragment);
        }

        ft.commitAllowingStateLoss();
    }

    public void detach(Fragment fragment){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        List<Fragment> childFragments = mFragmentManager.getFragments();
        if (childFragments.contains(fragment)) {
            ft.detach(fragment);
        }
        ft.commit();
    }
}
