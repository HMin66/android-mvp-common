package com.hmin66.commonlibrary.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;


/**
 * 作者：天天童话丶
 * 描述：
 */

public class VerticalDragView extends FrameLayout {

    //拖动控件
    private View mDragView;
    private View mListView;
    //固定控件 高度
    private int mFixedViewHeight;

    public void setListView(View view){
        this.mListView = view;
    }

    private ViewDragHelper mViewDragHelper;

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //指定子view 是否可以拖动
            return child == mDragView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //垂直拖动移动的位置
//            Log.e("VerticalDragView", "clampViewPositionVertical→→[child, top, dy]:" + "[" + child + "," +  top + ","+ dy + "]");
            //设置拖动范围在 0 - mFixedViewHeight 之间
            if (top <= 0) top = 0;
            if (top >= mFixedViewHeight) top = mFixedViewHeight;
            return top;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //水平拖动移动的位置
//            Log.e("VerticalDragView", "clampViewPositionHorizontal→→[child, left, dx]:" + "[" + child + "," +  left + ","+ dx + "]");
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //手指松开
            //Log.e("VerticalDragView", "onViewReleased→→[releasedChild, xvel, yvel]:" + "[" + releasedChild + "," +  xvel + ","+ yvel + "]");
            if (releasedChild == mDragView) {
                //Log.e("HMin66", "mDragView.getTop():" + mDragView.getTop() + " mFixedViewHeight / 2:" + mFixedViewHeight / 2);
                if (mDragView.getTop() > mFixedViewHeight / 2) {
                    //Log.e("HMin66", "显示固定控件");
                    mFixedViewIsOpen = true;
                    //显示固定控件
                    mViewDragHelper.settleCapturedViewAt(0, mFixedViewHeight);
                } else {
                    //Log.e("HMin66", "隐藏固定控件");
                    mFixedViewIsOpen = false;
                    //隐藏固定控件
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                }
                invalidate();
            }
        }


    };

    private float mDownY;

    private boolean mFixedViewIsOpen = true;

    public VerticalDragView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewDragHelper = ViewDragHelper.create(this, 0.5f, mDragHelperCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2){
            throw new RuntimeException("VerticalDragView 只能包含两个子布局！");
        }
        mDragView = mListView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //Log.e("HMin66", "onLayout: " + changed + " mDragView.getTop():" + mDragView.getTop());
        if (changed){
            mFixedViewHeight = getChildAt(0).getMeasuredHeight();
//            ViewCompat.offsetTopAndBottom(mDragView, mFixedViewHeight);
        }

        if (mFixedViewIsOpen){
            mDragView.layout(left, mFixedViewHeight, right, mDragView.getMeasuredHeight() + mFixedViewHeight);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    //处理 列表的事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 固定控件 显示时 事件全部拦截
//        if (mFixedViewIsOpen){
//            return true;
//        }
        //在顶部向下滑动 本来应该是让自己执行onTouch事件，但却被 listView 消费了，所以在这里进行拦截
        //listview 通过 requestDisallowInterceptTouchEvent() 方法 请求父类不拦截
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.e("HMin66", "onInterceptTouchEvent: ACTION_DOWN");
                mDownY = event.getY();
                //第一次按下 被 listview ontouch 事件消费 ，必须把按下事件传递给mViewDragHelper
                //because ACTION_DOWN was not received for this pointer before ACTION_MOVE.
                mViewDragHelper.processTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("HMin66", "onInterceptTouchEvent: ACTION_MOVE：" + mDragView.getTop());
                float moveY = event.getY();
                if ((moveY - mDownY) > 0 && !canChildScrollUp() && !mFixedViewIsOpen){
                    //固定控件是隐藏时 列表已经滑动到顶部并且是向下滑动 拦截事件 不然listview处理
                    //Log.e("HMin66", "onInterceptTouchEvent: 固定控件是隐藏时 列表已经滑动到顶部并且是向下滑动 拦截事件");
                    return true;
                }

                if ((moveY - mDownY) > 0 && mFixedViewIsOpen){
                    //Log.e("HMin66", "onInterceptTouchEvent: 固定控件是显示时 拖动控件向下滑动 不拦截事件");
                    // 固定控件是显示时 拖动控件向下滑动 不拦截
                    return false;
                }

                if ((moveY - mDownY) < 0 && !mFixedViewIsOpen){
                    //Log.e("HMin66", "onInterceptTouchEvent: 固定控件是隐藏时 拖动控件向上滑动 不拦截事件");
                    // 固定控件是隐藏时 拖动控件向上滑动 不拦截
                    return false;
                }

                if ((moveY - mDownY) < 0 && mFixedViewIsOpen){
                    //Log.e("HMin66", "onInterceptTouchEvent: 固定控件是显示时 拖动控件向上滑动 拦截事件");
                    // 固定控件是显示时 拖动控件向上滑动 拦截
                    return true;
                }

                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.e("HMin66", "onTouchEvent:"  + event.getAction() + " top:"+ mDragView.getTop());
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll up. Override this if the child view is a custom view.
     *         判断View 是否滚动到最顶部，还能不能向上滚动
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mListView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mListView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mListView, -1) || mListView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mListView, -1);
        }
    }
}
