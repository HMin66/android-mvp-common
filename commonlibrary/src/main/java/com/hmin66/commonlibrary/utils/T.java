package com.hmin66.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;


/**
 * @description: T Toast统一管理类
 * @data 2018/1/24-15:31
 * @author: AoJiaoQiang
 */
public class T {

    public static Context mContext;
    private static Toast toast;

    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message) {
        if (mContext == null) {
            throw new RuntimeException("unRegister Context in Application");
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        toast.setText(message);
        toast.show();
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message, View.OnAttachStateChangeListener listener) {
        if (mContext == null) {
            throw new RuntimeException("unRegister Context in Application");
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        toast.setText(message);
        toast.getView().addOnAttachStateChangeListener(listener);
        toast.show();
    }

    public static void showShort(int resId) {
        showShort(mContext.getString(resId));
    }

    public static void showShort(Activity activity, String msg) {
        if (mContext == null) {
            throw new RuntimeException("unRegister Context in Application");
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }
}
