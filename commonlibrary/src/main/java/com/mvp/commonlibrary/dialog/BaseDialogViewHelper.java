package com.mvp.commonlibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/4/30.
 */
class BaseDialogViewHelper {

    private View mContentView = null;
    private SparseArray<WeakReference<View>> mViews;

    public BaseDialogViewHelper(Context context, int contentViewLayoutId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(contentViewLayoutId, null);
    }

    public BaseDialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public void setText(int viewId, CharSequence charSequence) {
        TextView textView = getView(viewId);
        if (textView != null) textView.setText(charSequence);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) view.setOnClickListener(listener);
    }

    public View getContentView() {
        return mContentView;
    }

    public <T extends View> T getView(int viewId) {

        View view = null;
        WeakReference<View> viewReference = mViews.get(viewId);

        if (viewReference != null){
            view = viewReference.get();
        }
        if (view == null){
            view = mContentView.findViewById(viewId);
            if (view != null){
                mViews.put(viewId, new WeakReference<View>(view));
            }
        }
        return (T) view;
    }
}
