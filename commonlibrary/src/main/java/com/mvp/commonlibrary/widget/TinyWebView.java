package com.mvp.commonlibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class TinyWebView extends WebView {

    private ProgressBar mProgressbar;
    private int mDefaultProgressColor = Color.GREEN;
    private WebSettings mSettings;

    public TinyWebView(Context context) {
        this(context, null);
    }

    public TinyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TinyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProgressBar(context);
        mSettings = getSettings();
        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
    }

    private void initProgressBar(Context context) {
        mProgressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(context, 3), 0, 0));
        //改变progressbar默认进度条的颜色（深红色）为Color.GREEN
        mProgressbar.setProgressDrawable(new ClipDrawable(new ColorDrawable(mDefaultProgressColor), Gravity.LEFT, ClipDrawable.HORIZONTAL));
        addView(mProgressbar);
    }

    /**
     * 方法描述：是否启用支持javascript
     */
    private TinyWebView openJavaScript() {
        mSettings.setJavaScriptEnabled(true);
        return this;
    }

    /**
     * 方法描述：是否自适应屏幕
     */
    private TinyWebView adaptiveScreen(){
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mSettings.setLoadWithOverviewMode(true);
        return this;
    }

    /**
     * 方法描述：是否可以支持缩放
     * @return
     */
    private TinyWebView setSupportZoom(){
        mSettings.setSupportZoom(true);
        return this;
    }

    /**
     * 方法描述：是否扩大比例的缩放
     * @return
     */
    private TinyWebView setUseWideViewPort(){
        mSettings.setUseWideViewPort(true);
        return this;
    }

    /**
     * 方法描述：是否显示缩放工具
     * @return
     */
    private TinyWebView setBuiltInZoomControls(){
        mSettings.setBuiltInZoomControls(true);
        return this;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 方法描述：根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 类描述：显示WebView加载的进度情况
     */
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressbar.setVisibility(GONE);
            } else {
                if (mProgressbar.getVisibility() == GONE)
                    mProgressbar.setVisibility(VISIBLE);

                mProgressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}

