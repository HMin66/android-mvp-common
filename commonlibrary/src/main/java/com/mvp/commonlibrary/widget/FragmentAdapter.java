package com.mvp.commonlibrary.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by hmin66 on 2019/12/6.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList;
    private String[] mTitleList;

    public void setTitleList(String[] titleList) {
        this.mTitleList = titleList;
    }

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, String[] titleList) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return (mFragmentList == null) ? 0 : mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null && mTitleList.length != 0){
            return mTitleList[position];
        }

        return "";
    }
}

