package com.mvp.commonlibrary.utils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * @description: L 日志工具类
 * @data 2018/1/24-16:45
 * @author: AoJiaoQiang
 */
public class L {

    private static boolean isDebug = false;

    private L() {
    }

    /**
     * 初始化
     */
    public static void init() {
        isDebug = true;
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(2)
                .methodOffset(2)
                .tag("GONGHUIPAY_FACE_LOG")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    public static void d(String msg) {
        if (isDebug) {
            Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Logger.i(msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Logger.e(msg);
        }
    }

    public static void json(String json) {
        if (isDebug) {
            Logger.json(json);
        }
    }

}
