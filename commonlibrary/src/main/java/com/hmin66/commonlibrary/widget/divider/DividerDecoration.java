package com.hmin66.commonlibrary.widget.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @description: DividerDecoration 用在LinearLayoutManager的情况下。在item之间添加分割线。
 * DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,72),0);//颜色 & 高度 & 左边距 & 右边距
 * itemDecoration.setDrawLastItem(true);//有时候你不想让最后一个item有分割线,默认true.
 * itemDecoration.setDrawHeaderFooter(false);//是否对Header于Footer有效,默认false.
 * recyclerView.addItemDecoration(itemDecoration);
 * @data 2017/12/29-15:49
 * @author: AoJiaoQiang
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {
    private ColorDrawable mColorDrawable;
    private int mHeight;
    private int mPaddingLeft;
    private int mPaddingRight;
    private boolean mDrawLastItem = true;
    private boolean mDrawHeaderFooter = false;

    public DividerDecoration(int color, int height) {
        this.mColorDrawable = new ColorDrawable(color);
        this.mHeight = height;
    }

    public DividerDecoration(int color, int height, int paddingLeft, int paddingRight) {
        this.mColorDrawable = new ColorDrawable(color);
        this.mHeight = height;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
    }

    /**
     * 是否绘制最后一个item的分割线
     * @param mDrawLastItem
     */
    public void setDrawLastItem(boolean mDrawLastItem) {
        this.mDrawLastItem = mDrawLastItem;
    }

    /**
     * 是否绘制头和脚的分割线
     * @param mDrawHeaderFooter
     */
    public void setDrawHeaderFooter(boolean mDrawHeaderFooter) {
        this.mDrawHeaderFooter = mDrawHeaderFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int orientation = 0;
        int headerCount = 0, footerCount = 0;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }

        if (position >= headerCount && position < parent.getAdapter().getItemCount() - footerCount || mDrawHeaderFooter) {
            if (orientation == OrientationHelper.VERTICAL) {
                outRect.bottom = mHeight;
            } else {
                outRect.right = mHeight;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (parent.getAdapter() == null) {
            return;
        }

        int orientation = 0;
        int headerCount = 0, footerCount = 0, dataCount;

        dataCount = parent.getAdapter().getItemCount();

        int dataStartPosition = headerCount;
        int dataEndPosition = headerCount + dataCount;


        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }
        int start, end;
        if (orientation == OrientationHelper.VERTICAL) {
            start = parent.getPaddingLeft() + mPaddingLeft;
            end = parent.getWidth() - parent.getPaddingRight() - mPaddingRight;
        } else {
            start = parent.getPaddingTop() + mPaddingLeft;
            end = parent.getHeight() - parent.getPaddingBottom() - mPaddingRight;
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position >= dataStartPosition && position < dataEndPosition - 1//数据项除了最后一项
                    || (position == dataEndPosition - 1 && mDrawLastItem)//数据项最后一项
                    || (!(position >= dataStartPosition && position < dataEndPosition) && mDrawHeaderFooter)//header&footer且可绘制
                    ) {

                if (orientation == OrientationHelper.VERTICAL) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + mHeight;
                    mColorDrawable.setBounds(start, top, end, bottom);
                    mColorDrawable.draw(c);
                } else {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int left = child.getRight() + params.rightMargin;
                    int right = left + mHeight;
                    mColorDrawable.setBounds(left, start, right, end);
                    mColorDrawable.draw(c);
                }
            }
        }
    }
}
