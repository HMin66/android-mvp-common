package com.mvp.commonlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


import com.mvp.commonlibrary.R;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/4/30.
 */
public class BaseDialog extends Dialog {

    private BaseController mController;

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);

        mController = new BaseController(this, getWindow());
    }

    public View getContentView() {
        return mController.getContentView();
    }

    public void setText(int viewId, CharSequence charSequence) {
        mController.setText(viewId, charSequence);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mController.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return mController.getView(viewId);
    }

    public static class Builder{
        private final BaseController.Params P;
        /**
         * Creates a builder for an alert dialog that uses the default alert
         * dialog theme.
         * <p>
         * The default alert dialog theme is defined by
         * {@link android.R.attr#alertDialogTheme} within the parent
         * {@code context}'s theme.
         *
         * @param context the parent context
         */
        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        /**
         * Creates a builder for an alert dialog that uses an explicit theme
         * resource.
         * <p>
         * The specified theme resource ({@code themeResId}) is applied on top
         * of the parent {@code context}'s theme. It may be specified as a
         * style resource containing a fully-populated theme, such as
         * {@link android.R.style#Theme_Material_Dialog}, to replace all
         * attributes in the parent {@code context}'s theme including primary
         * and accent colors.
         * <p>
         * To preserve attributes such as primary and accent colors, the
         * {@code themeResId} may instead be specified as an overlay theme such
         * as {@link android.R.style#ThemeOverlay_Material_Dialog}. This will
         * override only the window attributes necessary to style the alert
         * window as a dialog.
         * <p>
         * Alternatively, the {@code themeResId} may be specified as {@code 0}
         * to use the parent {@code context}'s resolved value for
         * {@link android.R.attr#alertDialogTheme}.
         *
         * @param context the parent context
         * @param themeResId the resource ID of the theme against which to inflate
         *                   this dialog, or {@code 0} to use the parent
         *                   {@code context}'s default alert dialog theme
         */
        public Builder(Context context, int themeResId) {
            P = new BaseController.Params(context, themeResId);
        }

        /**
         * Creates an {@link BaseDialog} with the arguments supplied to this
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         */
        public BaseDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final BaseDialog dialog = new BaseDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mController);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link BaseDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public BaseDialog show() {
            final BaseDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public Builder setContentView(View view){
            P.mContentView = view;
            P.mContentViewLayoutId = 0;
            return this;
        }

        public Builder setContentView(int layoutId){
            P.mContentView = null;
            P.mContentViewLayoutId = layoutId;
            return this;
        }

        public Builder setText(int viewId, CharSequence text){
            P.mTextArray.put(viewId, text);
            return this;
        }

        public Builder setOnClickListener(int viewId, View.OnClickListener listener){
            P.mClickArray.put(viewId, listener);
            return this;
        }

        public Builder fullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fromBottom(Boolean isAnimation){
            if (isAnimation){
                P.mAnimations = R.style.dialog_from_bottom_anim;
            }

            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public Builder fromRight(Boolean isAnimation){
            if (isAnimation){
                P.mAnimations = R.style.dialog_from_right_anim;
            }

            P.mGravity = Gravity.RIGHT;
            return this;
        }

        public Builder setWidthAndHeight(int width, int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        public Builder addDefaultAnimation(){
            P.mAnimations = R.style.dialog_alpha_anim;
            return this;
        }

        public Builder setAnimations(int styleAnimation){
            P.mAnimations = styleAnimation;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(OnDismissListener) setOnDismissListener}.</p>
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(OnDismissListener)
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }
    }
}
