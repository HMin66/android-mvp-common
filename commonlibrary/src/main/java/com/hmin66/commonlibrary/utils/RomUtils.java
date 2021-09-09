package com.hmin66.commonlibrary.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Method;

/**
 * 兼容部分 小米/vivo/oppo 手机 程序安装后因为后台弹出界面权限默认拒绝而打不开singleTask页面
 * Created by hmin66 on 2021/5/11.
 */
public class RomUtils {

    public static RomUtils mInstance;
    public static RomUtils getInstance(){
        if (mInstance == null){
            synchronized (RomUtils.class){
                if (mInstance == null){
                    mInstance = new RomUtils();
                }
            }
        }
        return mInstance;
    }

    private final String TAG = RomUtils.class.getSimpleName();

    private boolean isXiaoMi() {
        return checkManufacturer("xiaomi");
    }

    private boolean isOppo() {
        return checkManufacturer("oppo");
    }

    private boolean isVivo() {
        return checkManufacturer("vivo");
    }

    private boolean checkManufacturer(String manufacturer) {
        return manufacturer.equals(Build.MANUFACTURER.toLowerCase());
    }

    public boolean isBackgroundStartAllowed(Context context) {
        if (isXiaoMi()) {
            return isXiaomiBgStartPermissionAllowed(context);
        }

        if (isVivo()) {
            return isVivoBgStartPermissionAllowed(context);
        }

        if (isOppo() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }


    private boolean isXiaomiBgStartPermissionAllowed(Context context) {
        AppOpsManager ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021;
            // ops.checkOpNoThrow(op, uid, packageName)
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isVivoBgStartPermissionAllowed(Context context) {
        return getVivoBgStartPermissionStatus(context) == 0;
    }

    /**
     * 判断Vivo后台弹出界面状态， 1无权限，0有权限
     * @param context context
     */
    private int getVivoBgStartPermissionStatus(Context context) {
        Uri uri = Uri.parse("content://com.vivo.permissionmanager.provider.permission/start_bg_activity");
        String selection = "pkgname = ?";
        String selectionArgs[] = {context.getPackageName()};
        int state = 1;
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);
            if (cursor.moveToFirst()) {
                state = cursor.getInt(cursor.getColumnIndex("currentstate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }
}
