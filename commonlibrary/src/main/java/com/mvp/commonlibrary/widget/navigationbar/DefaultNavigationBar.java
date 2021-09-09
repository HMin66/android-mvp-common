package com.mvp.commonlibrary.widget.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.mvp.commonlibrary.R;


/**
 * 作者：Administrator on 2017/11/14 10:15
 * 描述：
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationBarParams> {

    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaultNavigationBarParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.layout_default_navigation_bar;
    }

    @Override
    public void applyView() {
        // 绑定效果
        Builder.DefaultNavigationBarParams p = getNavigationParams();
        if (p.mDivideLineGone){
            setGone(R.id.dividing_line);
        }

        if (p.mBackgroundColor != 0){
            setBackgroundColor(p.mBackgroundColor);
        }

        if (!TextUtils.isEmpty(p.mTitle)) {
            setText(R.id.tv_default_navigation_bar_center_title, p.mTitle);
        }

        if (p.mTitleColor != 0){
            setTextColor(R.id.tv_default_navigation_bar_center_title, p.mTitleColor);
        }

        if (p.mTitleTextSize != 0){
            setTextSize(R.id.tv_default_navigation_bar_center_title, p.mTitleTextSize);
        }

        if (p.mTitleTextStyle != null){
            setTextStyle(R.id.tv_default_navigation_bar_center_title, p.mTitleTextStyle);
        }

        if (!TextUtils.isEmpty(p.mLeftText)){
            setText(R.id.tv_default_navigation_bar_left_text, p.mLeftText);
            setOnClickListener(R.id.tv_default_navigation_bar_left_text, p.mLeftClickListener);
        }

        if (p.mLeftTextColor != 0){
            setTextColor(R.id.tv_default_navigation_bar_left_text, p.mLeftTextColor);
        }

        if (p.mLeftTextSize != 0){
            setTextSize(R.id.tv_default_navigation_bar_left_text, p.mLeftTextSize);
        }

        if (p.mLeftIcon != 0) {
            setImageResource(R.id.iv_default_navigation_bar_left_icon, p.mLeftIcon);
        }

        if (!TextUtils.isEmpty(p.mRightText)) {
            setText(R.id.tv_default_navigation_bar_right_text, p.mRightText);
            setOnClickListener(R.id.tv_default_navigation_bar_right_text, p.mRightClickListener);
        }

        if (p.mRightTextColor != 0){
            setTextColor(R.id.tv_default_navigation_bar_right_text, p.mRightTextColor);
        }

        if (p.mRightTextSize != 0){
            setTextSize(R.id.tv_default_navigation_bar_right_text, p.mRightTextSize);
        }

        if (p.mRightIcon != 0) {
            setImageResource(R.id.iv_default_navigation_bar_right_icon, p.mRightIcon);
            setOnClickListener(R.id.iv_default_navigation_bar_right_icon, p.mRightClickListener);
        }

        setOnClickListener(R.id.iv_default_navigation_bar_left_icon, p.mLeftClickListener);
    }

    public static class Builder extends AbsNavigationBar.Builder{

        DefaultNavigationBarParams p;

        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, ViewGroup parent) {
            p = new DefaultNavigationBarParams(context, parent);
        }

        @Override
        public DefaultNavigationBar builder() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(p);
            return defaultNavigationBar;
        }

        public Builder setTitle(String title){
            p.mTitle = title;
            return this;
        }

        public Builder setTitleColor(int titleColor){
            p.mTitleColor = titleColor;
            return this;
        }

        public Builder setTitleSize(float titleTextSize){
            p.mTitleTextSize = titleTextSize;
            return this;
        }

        public Builder setTitleStyle(Typeface tf){
            p.mTitleTextStyle = tf;
            return this;
        }

        public Builder setLeftText(String leftText){
            p.mLeftText = leftText;
            return this;
        }

        public Builder setLeftTextColor(int leftTextColor){
            p.mLeftTextColor = leftTextColor;
            return this;
        }

        public Builder setLeftTextSize(float leftTextSize){
            p.mLeftTextSize = leftTextSize;
            return this;
        }

        public Builder setLeftIcon(int resId){
            p.mLeftIcon = resId;
            return this;
        }

        public Builder setLeftClickListener(View.OnClickListener leftClickListener){
            p.mLeftClickListener = leftClickListener;
            return this;
        }

        public Builder setRightText(String rightText){
            p.mRightText = rightText;
            return this;
        }

        public Builder setRightTextColor(int rightTextColor){
            p.mRightTextColor = rightTextColor;
            return this;
        }

        public Builder setRightTextSize(float rightTextSize){
            p.mRightTextSize = rightTextSize;
            return this;
        }

        public Builder setRightIcon(int resId){
            p.mRightIcon = resId;
            return this;
        }

        public Builder setRightClickListener(View.OnClickListener rightClickListener){
            p.mRightClickListener = rightClickListener;
            return this;
        }

        public Builder setBackgroundColor(int color){
            p.mBackgroundColor = color;
            return this;
        }

        public Builder setDividingLineGone(){
            p.mDivideLineGone = true;
            return this;
        }

        public static class DefaultNavigationBarParams
                extends AbsNavigationBar.Builder.AbsNavigationParams{

            public String mTitle;
            public int mTitleColor;
            public float mTitleTextSize;
            public Typeface mTitleTextStyle;
            public String mLeftText;
            public int mLeftTextColor;
            public float mLeftTextSize;
            public int mLeftIcon;
            public String mRightText;
            public int mRightTextColor;
            public float mRightTextSize;
            public int mRightIcon;
            public int mBackgroundColor;
            public boolean mDivideLineGone = false;

            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 默认关闭Activity
                    ((Activity)mContext).finish();
                }
            };
            public View.OnClickListener mRightClickListener;

            public DefaultNavigationBarParams(Context context) {
                this(context, null);
            }

            public DefaultNavigationBarParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
