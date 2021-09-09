package com.hmin66.commonlibrary.http.retrofit;

import android.content.Context;
import android.support.multidex.BuildConfig;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hmin66.commonlibrary.http.gson.IGsonConverterFactory;
import com.hmin66.commonlibrary.http.interceptor.RequestInterceptor;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/14.
 */

public class HttpUtils {

    public static final int CONNECT_TIME_OUT = 30;//连接超时时长x秒
    public static final int READ_TIME_OUT = 30;//读数据超时时长x秒
    public static final int WRITE_TIME_OUT = 30;//写数据接超时时长x秒

    private HttpUtils(){}

    public static OkHttpClient provideOkHttpClient(Context context, OkHttpClient.Builder builder){
        Authenticator authenticator = new Authenticator() {
            //当服务器返回的状态码为401时，会自动执行里面的代码，也就实现了自动刷新token
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                //这里可以进行刷新 token 的操作
                Logger.d("==========>   重新刷新了token");
//                instance.getUploadToken()
                return response.request().newBuilder()
                        .addHeader("token", "")
                        .build();
            }
        };
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // 保存session
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache()
                , new SharedPrefsCookiePersistor(context));

        return builder
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .addInterceptor(new RequestInterceptor())
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        Log.e("MSG", chain.request().toString());
                        return response;
                    }
                })
                .authenticator(authenticator)
                .cookieJar(cookieJar)
                .build();
    }

    public static Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(IGsonConverterFactory.create())
                .build();
    }

    public static Retrofit createRetrofit(Retrofit.Builder builder, String url) {
        return builder
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    public static OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }
}
