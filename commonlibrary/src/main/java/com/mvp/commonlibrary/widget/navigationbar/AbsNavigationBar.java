package com.mvp.commonlibrary.widget.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 作者：Administrator on 2017/11/14 09:38
 * 描述：标题栏基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    private View mNavigationBar;
    private final SparseArray<View> views = new SparseArray();

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }

    public P getNavigationParams() {
        return mParams;
    }

    /**
     * 创建 和 绑定 View
     */
    private void createAndBindView() {
        // 1、创建View
        mNavigationBar = LayoutInflater.from(mParams.mContext)
                .inflate(bindLayoutId(), mParams.mParent, false);

        // 2、添加View
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 5.0 以下 4.4以上时 处理标题栏和状态栏的重叠
            FrameLayout layout = new FrameLayout(mParams.mContext);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(mNavigationBar);
            layout.setFitsSystemWindows(true);

            mParams.mParent.addView(layout, 0);
        } else {
            // 其他直接添加
            mParams.mParent.addView(mNavigationBar, 0);
        }
        // 3、绑定参数
        applyView();
    }

    public void show(){
        mNavigationBar.setVisibility(View.VISIBLE);
    }

    public void hide(){
        mNavigationBar.setVisibility(View.GONE);
    }

    public void setGone(int viewId){
        this.getView(viewId).setVisibility(View.GONE);
    }

    public void setBackgroundColor(int color) {
        mNavigationBar.setBackgroundColor(color);
    }

    public void setText(int viewId, String text) {
        TextView view = (TextView) this.getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setText(text);
    }

    public void setTextColor(int viewId, int textColor) {
        TextView view = (TextView)this.getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setTextColor(textColor);
    }

    public void setTextSize(int viewId, float size) {
        TextView view = (TextView)this.getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setTextSize(size);
    }

    public void setTextStyle(int viewId, Typeface tf) {
        TextView view = (TextView)this.getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setTypeface(tf);
    }

    public void setImageResource(int viewId, int resId) {
        ImageView view = (ImageView) this.getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setImageResource(resId);
    }

    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = (ImageView) this.getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setImageDrawable(drawable);
    }

    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = (ImageView) this.getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setImageBitmap(bitmap);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(listener);
    }

    public <T extends View> T getView(int viewId) {
        View view = (View) this.views.get(viewId);
        if (view == null) {
            view = mNavigationBar.findViewById(viewId);
            this.views.put(viewId, view);
        }
        return (T) view;
    }

    public abstract static class Builder {

        public abstract AbsNavigationBar builder();


        public static class AbsNavigationParams {

            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                mContext = context;
                if (parent != null) {
                    mParent = parent;
                } else {
                    mParent = (ViewGroup) ((ViewGroup) ((Activity) context).getWindow().getDecorView()).getChildAt(0);
                }
            }
        }
    }
}
