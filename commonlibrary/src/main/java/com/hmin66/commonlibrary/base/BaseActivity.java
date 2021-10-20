package com.hmin66.commonlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import com.hmin66.commonlibrary.AppManager;
import com.jaeger.library.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends RxAppCompatActivity {

    private Bundle mBundle;
    private Unbinder unbinde;
    protected BaseActivity mContext;
    protected ViewGroup mRootView;
    protected Bundle mSavedInstanceState;
    // 这里先写死 将来应该从acache中获取主题色
    protected int mDefaultStatusBarColor = getStatusBarColor();
    // 背景色
    protected int mDefaultBackgroundColor = Color.WHITE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        initTheme();
        setContentView(getLayoutId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setBackgroundColorAndStatusBar();
        AppManager.getAppManager().addActivity(this);

        //this.mDefaultStatusBarColor = getResources().getColor(R.color.app_default_theme);
        this.unbinde = ButterKnife.bind(this);
        this.mBundle = getIntent().getExtras();
        this.mSavedInstanceState = savedInstanceState;

        if (useEventBus()){
            EventBus.getDefault().register(this);
        }

        initBundle(mBundle);
        initView();
        initPermission();
        initData();
    }

    private void setBackgroundColorAndStatusBar() {
        ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
        mRootView = (ViewGroup) contentView.getChildAt(0);
        if (useStatusBarColor()) {
            StatusBarUtil.setColorNoTranslucent(this, mDefaultStatusBarColor);
            // 使用黑色状态栏图标 有个bug 布局使用padding属性会失效，注意下
            StatusBarUtil.setLightMode(this);
        }
        if (useFitsSystemWindows()) mRootView.setFitsSystemWindows(true);
        Drawable drawable = mRootView.getBackground();
        if (drawable == null && useDefaultBackground()){
            mRootView.setBackgroundColor(mDefaultBackgroundColor);
        }
    }

    protected boolean useFitsSystemWindows(){
        return true;
    }

    protected boolean useDefaultBackground(){
        return true;
    }

    protected boolean useStatusBarColor() {
        return true;
    }

    protected boolean useEventBus() {return false;}

    protected abstract int getLayoutId();

    protected  void initTheme(){}
    protected  void initBundle(Bundle bundle){
        if (bundle == null) return;
    }
    protected  void initView(){}
    protected  void initPermission(){}
    protected  void initData(){}

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    protected int getStatusBarColor(){
        return Color.WHITE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除view绑定
        if (unbinde != null) {
            unbinde.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        AppManager.getAppManager().finishActivity(this);
    }

    public void GoActivity(Context context, Class clazz){
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    public void GoActivityForResult(Context context, Class clazz, int requestCode){
        Intent intent = new Intent(context, clazz);
        startActivityForResult(intent, requestCode);
    }

    public void GoActivity(Context context, Class clazz, Bundle bundle){
        if (bundle == null){
            GoActivity(context, clazz);
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoAndKillActivity(Context context, Class clazz){
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode);
        }
    }

    private String mActivityJumpTag;
    private long mActivityJumpTime;

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent          用于跳转的 Intent 对象
     * @return                检查通过返回true, 检查不通过返回false
     */
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        }else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        }else {
            return result;
        }

        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        return result;
    }

    /**
     * Only fullscreen activities can request orientation终极解决方法
     * desc: 安卓8.0版本时为了支持全面屏，增加了一个限制：
     * 如果是透明的Activity，则不能固定它的方向，因为它的方向其实是依赖其父Activity的
     * （因为透明）。然而这个bug只有在8.0中有，8.1中已经修复。
     * @return
     */
    private boolean isTranslucentOrFloating(){
        boolean isTranslucentOrFloating = false;
        try {
            int [] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            Log.e("HMin66", "avoid calling setRequestedOrientation when Oreo.");
            boolean result = fixOrientation();
            Log.e("HMin66", "onCreate fixOrientation when Oreo, result = " + result);
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }
}
