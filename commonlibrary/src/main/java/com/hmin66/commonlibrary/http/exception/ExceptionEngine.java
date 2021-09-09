package com.hmin66.commonlibrary.http.exception;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;


/**
 * 错误/异常处理工具
 *
 */
public class ExceptionEngine {

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpExc = (HttpException) e;
            ex = new ApiException(e, ERROR.UN_KNOWN_ERROR);
            ex.setMsg("网络错误");  //均视为网络错误
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException serverExc = (ServerException) e;
            ex = new ApiException(serverExc, serverExc.getCode());
            ex.setMsg(serverExc.getMsg());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof MalformedJsonException) {  //解析数据错误
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.setMsg("解析数据错误");
            return ex;
        } else if (e instanceof ConnectException) {//连接网络错误
            ex = new ApiException(e, ERROR.CONNECT_ERROR);
            ex.setMsg("网络连接失败");
            return ex;
        } else if (e instanceof SocketTimeoutException) {//网络超时
            ex = new ApiException(e, ERROR.TIME_OUT_ERROR);
            ex.setMsg("网络请求超时");
            return ex;
        } else {  //未知错误
            ex = new ApiException(e, ERROR.UN_KNOWN_ERROR);
            ex.setMsg("未知错误");
            return ex;
        }
    }

}
