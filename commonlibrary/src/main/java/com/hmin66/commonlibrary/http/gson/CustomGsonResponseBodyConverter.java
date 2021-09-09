package com.hmin66.commonlibrary.http.gson;

import com.hmin66.commonlibrary.http.exception.ERROR;
import com.hmin66.commonlibrary.http.exception.ServerException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by 文强 on 2017/3/2.
 */

public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //把responsebody转为string,因为retrofit2的Response对象只能够读取一次，而我们只需要判断code和获取msg就行了
        if (gson == null || adapter == null){
            throw new ServerException(ERROR.ANALYTIC_SERVER_DATA_ERROR,"ANALYTIC SERVER DATA ERROR");
        }
        String response = value.string();
        // 这里只是为了检测code是否==200 也就是服务器定义的访问正常,所以只解析HttpStatus中的字段,因为只要code和message就可以了
        HttpStatus httpStatus = gson.fromJson(response, HttpStatus.class);
        if (httpStatus.isCodeInvalid()) {
            value.close();
            //抛出一个RuntimeException, 这里抛出的异常会到CallBack的onError()方法中统一处理
            throw new ServerException(httpStatus.getCode(), httpStatus.getMessage());
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}