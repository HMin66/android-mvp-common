package com.hmin66.commonlibrary.http.interceptor;

import com.hmin66.commonlibrary.utils.L;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 结果拦截器,这个类的执行时间是返回结果返回的时候,返回一个json的String,对里面一些特殊字符做处理
 * 主要用来处理一些后台上会出现的bug,比如下面声明的这三种情况下统一替换为:null
 * Created by aojiaoqiang on 2018/1/31.
 */

public class ResponseInterceptor implements Interceptor {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private String emptyString = ":\"\"";
    private String newChars = ":null";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String json = responseBody.string();

            L.json("json = " + json);

            MediaType contentType = responseBody.contentType();
            if (!json.contains(emptyString)) {
                ResponseBody body = ResponseBody.create(MEDIA_TYPE, json);
                return response.newBuilder().body(body).build();
            } else {
                String replace = json.replace(emptyString, newChars);
                ResponseBody body = ResponseBody.create(MEDIA_TYPE, replace);
                return response.newBuilder().body(body).build();
            }
        }
        return response;
    }
}
