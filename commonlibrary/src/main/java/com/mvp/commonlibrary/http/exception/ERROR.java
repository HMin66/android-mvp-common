package com.mvp.commonlibrary.http.exception;

/**
 * Created by 12262 on 2016/5/31.
 * 与服务器约定好的异常
 */
public class ERROR {
    /**
     * 未知错误
     */
    public static final int UN_KNOWN_ERROR = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 解析(服务器)数据错误
     */
    public static final int ANALYTIC_SERVER_DATA_ERROR = 1002;

    /**
     * 解析(客户端)数据错误
     */
    public static final int ANALYTIC_CLIENT_DATA_ERROR = 1003;

    /**
     * 网络连接错误
     */
    public static final int CONNECT_ERROR = 1004;

    /**
     * 网络连接超时
     */
    public static final int TIME_OUT_ERROR = 1005;

    /**
     * 网络错误
     */
    public static final int NETWORD_ERROR = 1006;

    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1007;
}
