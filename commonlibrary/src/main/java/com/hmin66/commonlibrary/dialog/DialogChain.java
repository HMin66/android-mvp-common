package com.hmin66.commonlibrary.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

/**
 *   参考 https://mp.weixin.qq.com/s/i_cnHyqrKRxgL5AaUlmTDg
     DialogChain dialogChain = DialogChain.create(3)
                    .attach(MainActivity.this)
                    .addInterceptor(dialog1)
                    .addInterceptor(dialog2)
                    .addInterceptor(dialog3)
                    .build();
                dialogChain.process();
 *   多个弹窗链方式调用
 */

public class DialogChain {
    //弹窗的时候可能需要Activity/Fragment环境。
    private FragmentActivity activity;
    private Fragment fragment;
    private ArrayList<DialogInterceptor> interceptors;
    private int index = 0;

    public DialogChain(FragmentActivity activity, Fragment fragment, ArrayList<DialogInterceptor> interceptors) {
        this.activity = activity;
        this.fragment = fragment;
        this.interceptors = interceptors;
    }

    public static Builder create(int initialCapacity){
        return new Builder(initialCapacity);
    }

    public static void openLog(boolean isOpen){

    }

    public void process(){
        if (interceptors == null) return;
        if (index == interceptors.size()) {
            // 最后一个弹窗关闭的时候，我们希望释放所有弹窗引用。
            clearAllInterceptors();
        } else if (index >= 0 && index < interceptors.size()) {
            DialogInterceptor interceptor = interceptors.get(index);
            index++;
            interceptor.intercept(this);
        }
    }

    private void clearAllInterceptors() {
        if (interceptors != null) {
            interceptors.clear();
        }
        interceptors = null;
    }

    // 构建者模式。
    public static class Builder {
        private int initialCapacity = 0;
        private ArrayList<DialogInterceptor> interceptors = null;
        private FragmentActivity activity = null;
        private Fragment fragment = null;

        public Builder(int initialCapacity) {
            this.initialCapacity = initialCapacity;
            this.interceptors = new ArrayList<DialogInterceptor>(this.initialCapacity);
        }

        // 添加一个拦截器。
        public Builder addInterceptor(DialogInterceptor interceptor) {
            if (!interceptors.contains(interceptor)) {
                interceptors.add(interceptor);
            }
            return this;
        }

        // 关联Fragment。
        public Builder attach(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        // 关联Activity。
        public Builder attach(FragmentActivity activity) {
            this.activity = activity;
            return this;
        }

        public DialogChain build() {
            return new DialogChain(activity, fragment, interceptors);
        }
    }
}
