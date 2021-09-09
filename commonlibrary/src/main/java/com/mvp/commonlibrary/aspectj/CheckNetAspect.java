package com.mvp.commonlibrary.aspectj;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.mvp.commonlibrary.aspectj.annotation.CheckNet;
import com.mvp.commonlibrary.base.BaseActivity;
import com.mvp.commonlibrary.base.BaseFragment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 作者：Administrator on 2018/1/26 17:27
 * 描述：CheckNet切点的切面处理类
 */

@Aspect
public class CheckNetAspect {
    private final String TAG = "CheckNetAspect";
    //@Aspect注解 使该类成为处理某个切点的切面处理类

    /**
     * 找到处理的切点 语法结构：execution([修饰符]　返回值类型　方法名(参数)　［异常模式］)　　括号可选部分。
     */
    @Pointcut("execution(@CheckNet * *(..))")
    public void methodAnnotationforCheckNet(){}

    /**
     * 处理切面 方法定义：方法名可变 其他都不可变。 @Around 在切点前后都要执行 @Before 在切点前要执行 @After 在切点后要执行
     * @param joinPoint
     * @return
     * @throws Throwable
     */
//    @Before("methodAnnotationforCheckNet()")
//    public void methodBefore(JoinPoint joinPoint) throws Throwable {
//        Log.e(TAG, "methodBefore");
//    }

    /**
     * 处理切面 方法定义：方法名可变 其他都不可变。 @Around 在切点前后都要执行 @Before 在切点前要执行 @After 在切点后要执行
     * @param joinPoint
     * @return
     * @throws Throwable
     */
//    @After("methodAnnotationforCheckNet()")
//    public void methodAfter(JoinPoint joinPoint) throws Throwable {
//
//        return null;
//    }

    /**
     * 处理切面 方法定义：方法名可变 其他都不可变。 @Around 在切点前后都要执行 @Before 在切点前要执行 @After 在切点后要执行
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("methodAnnotationforCheckNet()")
    public Object methodAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        Log.e(TAG, "methodAround");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
//        Log.e(TAG, "checkNet:" + checkNet + ", name" + signature.getMethod().getName());
        if(checkNet != null){
            Object object = joinPoint.getThis(); //获取当前切点所在的类
            Context context = getContext(object);
//            Log.e(TAG, "this:" + object + ", target:" + joinPoint.getTarget());
//            Log.e(TAG, "context:" + context + ", target:" + getContext(joinPoint.getTarget()));
            if (context != null){
                if (!isNetworkAvailable(context)){
                    //没有网络就不要往下执行
                    Toast.makeText(context, "请连接网络！", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }
        return joinPoint.proceed();
    }

    public Context getContext(Object object) {
        if (object instanceof BaseActivity){
            return (BaseActivity)object;
        } else if (object instanceof BaseFragment){
            return ((BaseFragment)object).getActivity();
        } else if (object instanceof View){
            return ((View)object).getContext();
        }
        return null;
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
