package com.mvp.commonlibrary.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.util.Random;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/5/9.
 */

public class Base64Utils {

    private static final String TAG = Base64Utils.class.getSimpleName();

    /**
     * 编码
     * @param content
     * @return
     */
    public static String encode(String content){
        String source = "UNO" + content + "HACHA";
        String target = Base64.encodeToString(source.getBytes(), Base64.DEFAULT);
        Log.e(TAG, "source:" + source + " ,target:" + target);
        return target;
    }

    /**
     * 解码
     * @param content
     * @return
     */
    public static String decode(String content){
        String source = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
        String target = source.substring(3, source.length() - 5);
        Log.e(TAG, "source:" + source + " ,target:" + target);
        return target;
    }

    /**
     * 防止短信刷验证码
     * Prevent text message verification code.
     */
    public static String preventMessageCode() {
        int v1 = (int) (Math.random() * 7);
        String[] encrypt = new String[]{"un", "no", "oh", "ha", "ac", "ch", "ha"};
        Log.e("产生的随机数", v1 + encrypt[v1]);
        Log.e("=打乱后=加密前=", "" + randString(v1 + encrypt[v1]));
        String encodedString = "UNO" + randString(v1 + encrypt[v1]) + "HACHA";
        Log.e(TAG, "[decodedString], str == " + encodedString);
        if (TextUtils.isEmpty(encodedString)) {
            return null;
        }
        return Base64.encodeToString(encodedString.getBytes(), Base64.DEFAULT);
    }

    public static String randString(String str) {//把生成的随机数打乱顺序
        StringBuffer result = new StringBuffer();
        int length = str.length();
        char[] chars = str.toCharArray();
        int index = -1; // 数组下标
        while (true) {
            index = new Random().nextInt(length); // 随机 生成 下标
            if (chars[index] != ' ') { //是否为空
                result.append(chars[index]);
                if (result.length() == length) { //全部获取完毕
                    break;
                }
                chars[index] = ' '; //置空
            } else
                continue;
        }
        return result.toString();
    }
}

