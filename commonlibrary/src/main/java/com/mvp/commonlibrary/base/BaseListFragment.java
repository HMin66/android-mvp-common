package com.mvp.commonlibrary.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mvp.commonlibrary.R;

import java.util.List;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/26.
 */

public abstract class BaseListFragment extends BaseFragment implements
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    protected int mDefaultSwipeRefreshColor;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter mAdapter;
    protected int mPageIndex = 1;

    protected abstract BaseQuickAdapter getAdapter();
    protected abstract void onLoadMore();

    protected int getSwipeRefreshColor(){
        return mDefaultSwipeRefreshColor = getResources().getColor(R.color.app_default_theme);
    }

    protected RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_list_recyclerview;
    }

    protected int getRecyclerViewId() {
        return R.id.recycler_view;
    }

    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh_layout;
    }

    @Override
    protected void initView(View rootView) {
        mSwipeRefreshLayout = rootView.findViewById(getSwipeRefreshLayoutId());
        mRecyclerView = rootView.findViewById(getRecyclerViewId());
        mSwipeRefreshLayout.setColorSchemeColors(getSwipeRefreshColor());
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mAdapter = getAdapter();
        if (mAdapter != null) {
            mAdapter.bindToRecyclerView(mRecyclerView);
            mAdapter.setOnLoadMoreListener(this);
            mAdapter.setPreLoadNumber(3);
            mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) onLoadMore();
            }
        });
    }

    protected void setAdapterData(List data) {
        final int size = data == null ? 0 : data.size();
        boolean isRefresh = mPageIndex == 1;
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < 10) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        initData();
    }
}
