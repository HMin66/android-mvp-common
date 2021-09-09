package com.mvp.commonlibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/4/30.
 */
class BaseController {

    private BaseDialog mDialog;
    private Window mWindow;
    private BaseDialogViewHelper mViewHelper;

    public BaseDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public void setViewHelper(BaseDialogViewHelper mViewHelper) {
        this.mViewHelper = mViewHelper;
    }

    public BaseController(BaseDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public View getContentView() {
        return mViewHelper.getContentView();
    }

    public void setText(int viewId, CharSequence charSequence) {
        mViewHelper.setText(viewId, charSequence);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public static class Params{
        public Context mContext;
        public int mThemeResId;
        //点击空白是否可以取消
        public boolean mCancelable = true;
        //dialog cancel监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //dialog 消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog key监听
        public DialogInterface.OnKeyListener mOnKeyListener;

        public View mContentView;
        public int mContentViewLayoutId;
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mGravity = Gravity.CENTER;
        public int mAnimations = 0;

        public Params(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        public void apply(BaseController controller) {
            BaseDialogViewHelper viewHelper = null;
            if (mContentViewLayoutId != 0){
                viewHelper = new BaseDialogViewHelper(mContext, mContentViewLayoutId);
            }

            if (mContentView != null){
                viewHelper = new BaseDialogViewHelper();
                viewHelper.setContentView(mContentView);
            }

            if (viewHelper == null){
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            controller.getDialog().setContentView(viewHelper.getContentView());

            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                viewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            controller.setViewHelper(viewHelper);

            Window window = controller.getWindow();
            window.setGravity(mGravity);
            if(mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;

            window.setAttributes(params);
        }
    }
}
