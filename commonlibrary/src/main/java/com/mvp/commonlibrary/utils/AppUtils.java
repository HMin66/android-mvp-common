package com.mvp.commonlibrary.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by aojiaoqiang on 2018/1/31.
 */

public class AppUtils {


    /**
     * 判断是否是主进程
     * @return
     */
    public static boolean isMainProcess(Context context){
        String processName = AppUtils.getProcessName(android.os.Process.myPid());
        return processName.equals(AppUtils.getPackageName(context));
    }

    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context){
        Context applicationContext = context.getApplicationContext();
        // 获取当前包名
        String packageName = applicationContext.getPackageName();
        return packageName;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 查询sim卡信息
     * 需要READ_PHONE_STATE权限
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getSimId(Context context) {
        int subId1 = -1;
        int subId2 = -1;
        String imsi1 = null;
        String imsi2 = null;
        try {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //大于等于Android 5.0 L版本
                Method getSubscriberId = tManager.getClass().getMethod("getSubscriberId", int.class);

                ContentResolver contentResolver = context.getContentResolver();
                Cursor c = contentResolver.query(Uri.parse("content://telephony/siminfo"),
                        new String[]{"_id"}, "sim_id" + " = ?", new String[]{"0"}, null);
                if (null != c && c.moveToFirst()) {
                    subId1 = c.getInt(c.getColumnIndexOrThrow("_id"));
                    c.close();
                }

                c = contentResolver.query(Uri.parse("content://telephony/siminfo"),
                        new String[]{"_id"}, "sim_id" + " = ?", new String[]{"1"}, null);
                if (null != c && c.moveToFirst()) {
                    subId2 = c.getInt(c.getColumnIndexOrThrow("_id"));
                    c.close();
                }

                if (subId1 > 0) {
                    imsi1 = (String) getSubscriberId.invoke(tManager, subId1);
                }
                if (subId2 > 0) {
                    imsi2 = (String) getSubscriberId.invoke(tManager, subId2);
                }

                if (!TextUtils.isEmpty(imsi1) && !TextUtils.isEmpty(imsi2)) {
                    return imsi1 + "," + imsi2;
                } else {
                    if (!TextUtils.isEmpty(imsi1)) {
                        return imsi1;
                    } else {
                        return imsi2;
                    }
                }
            } else { //Android 5.0以下的api获取ismi方法 sdk < 21
                return tManager.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前系统的语言版本 cn us
     *
     * @return
     */
    public static String getLocal() {
        String locale = Locale.getDefault().toString();
        return locale;
    }

    /**
     * 获取设备的UUID
     * 建议保存下来
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getAppUUid(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, tmPhone, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String uniqueId = deviceUuid.toString();
            return uniqueId;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context){
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // 获取ip地址
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
