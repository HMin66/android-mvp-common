package com.mvp.commonlibrary.widget.numberview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mvp.commonlibrary.R;

/**
 * 描述： 加减控件
 * 作者： 天天童话丶
 * 时间： 2018/5/12.
 */

public class NumberView extends LinearLayout {

    private ImageView mReduce;
    private ImageView mAdd;
    private TextView mNumber;
    private Drawable mDrawableLeft;
    private Drawable mDrawableRight;
    private CallBack mCallBack;

    public NumberView(Context context) {
        this(context, null);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        // 直接加载布局
        inflate(context, R.layout.layout_number_view, this);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberView);
        mDrawableLeft = array.getDrawable(R.styleable.NumberView_drawableLeft);
        mDrawableRight = array.getDrawable(R.styleable.NumberView_drawableRight);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mReduce = findViewById(R.id.reduce);
        mAdd = findViewById(R.id.add);
        mNumber = findViewById(R.id.number);

        if (mDrawableLeft != null){
            mReduce.setImageDrawable(mDrawableLeft);
        }
        if (mDrawableRight != null){
            mAdd.setImageDrawable(mDrawableRight);
        }
        mAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt(mNumber.getText().toString().trim());
                mNumber.setText(String.valueOf(number + 1));

                if (mCallBack != null){
                    mCallBack.add();
                }
            }
        });

        mReduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt(mNumber.getText().toString().trim());
                if (number > 1){
                    mNumber.setText(String.valueOf(number - 1));
                }

                if (mCallBack != null){
                    mCallBack.reduce();
                }
            }
        });
    }

    public void setDrawableLeft(Drawable drawableLeft){
        mReduce.setImageDrawable(drawableLeft);
    }

    public void setDrawableRight(Drawable drawableRight){
        mAdd.setImageDrawable(drawableRight);
    }

    public String getNumber(){
        return mNumber.getText().toString().trim();
    }

    public interface CallBack{
        void add();
        void reduce();
    }

    public void setCallBack(CallBack callBack){
        this.mCallBack = callBack;
    }
}
