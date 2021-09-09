package com.mvp.commonlibrary.utils;

/**
 * Created by hmin66 on 2021/8/9.
 */

public class NumberUtils {

    public static int parseInt(String s, int defaultValue) {
        if (s == null || s.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException x) {
            return defaultValue;
        }
    }

    public static double parseDouble(String s, double defaultValue) {
        if (s == null || s.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException x) {
            return defaultValue;
        }
    }
}
