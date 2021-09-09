package com.mvp.commonlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mvp.commonlibrary.R;


public class PasswordEditText extends AppCompatEditText
        implements View.OnTouchListener {

    private int mDefaultOpenEyesIconId = R.drawable.open_eyes_icon;
    private int mDefaultCloseEyesIconId = R.drawable.close_eyes_icon;
    private Drawable mOpenEyesIcon;
    private Drawable mCloseEyesIcon;
    private boolean isOpenEyes; //是否可见
    private OnTouchListener mOnTouchListener;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        Drawable openDrawable = a.getDrawable(R.styleable.PasswordEditText_open_eyes_icon_src);
        Drawable closeDrawable = a.getDrawable(R.styleable.PasswordEditText_close_eyes_icon_src);
        a.recycle();
        if (openDrawable == null) {
            openDrawable = ContextCompat.getDrawable(context, mDefaultOpenEyesIconId);
        }

        if (closeDrawable == null) {
            closeDrawable = ContextCompat.getDrawable(context, mDefaultCloseEyesIconId);
        }

        mOpenEyesIcon = DrawableCompat.wrap(openDrawable);
        mCloseEyesIcon = DrawableCompat.wrap(closeDrawable);

        mOpenEyesIcon.setBounds(0, 0, mOpenEyesIcon.getIntrinsicWidth(), mOpenEyesIcon.getIntrinsicHeight());
        mCloseEyesIcon.setBounds(0, 0, mCloseEyesIcon.getIntrinsicWidth(), mCloseEyesIcon.getIntrinsicHeight());
        setEyesIcon();

        setSingleLine();
        //设置密码类型
//        setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        setTransformationMethod(PasswordTransformationMethod.getInstance());


        super.setOnTouchListener(this);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (mCloseEyesIcon.isVisible() && x > getWidth() - getPaddingRight() - mCloseEyesIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                isOpenEyes = !isOpenEyes;
                if (isOpenEyes){
                    //显示文本
//                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //显示密码
//                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                setSelection(getText().toString().trim().length());
                setEyesIcon();
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }

    private void setEyesIcon() {
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                isOpenEyes ? mOpenEyesIcon : mCloseEyesIcon,
                compoundDrawables[3]);
    }
}
