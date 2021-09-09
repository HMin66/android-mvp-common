package com.hmin66.commonlibrary.http.gson;

import com.google.gson.annotations.SerializedName;
import com.hmin66.commonlibrary.http.server.ServerResponseCode;

/**
 * 用于自定义Gson解析器里面 判断服务器定义的错误码
 * Created by 文强 on 2017/3/2.
 */

public class HttpStatus {
    @SerializedName("code")
    private int mCode;
    @SerializedName("msg")
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return mCode != ServerResponseCode.SUCCESS;
    }
}