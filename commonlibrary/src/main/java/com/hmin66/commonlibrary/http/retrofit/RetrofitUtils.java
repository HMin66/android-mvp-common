package com.hmin66.commonlibrary.http.retrofit;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hmin66.commonlibrary.BuildConfig;
import com.hmin66.commonlibrary.http.gson.CustomGsonConverterFactory;
import com.hmin66.commonlibrary.http.interceptor.RequestInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * RetrofitUtils工具类
 */
public final class RetrofitUtils {

    public static final int CONNECT_TIME_OUT = 30;//连接超时时长x秒
    public static final int READ_TIME_OUT = 30;//读数据超时时长x秒
    public static final int WRITE_TIME_OUT = 30;//写数据接超时时长x秒
    private Retrofit mRetrofit;
    private static volatile RetrofitUtils instance;
    private static Context mContext;
    private static String mBaseUrl;

    public static void init(Context context, String baseUrl) {
        mContext = context;
        mBaseUrl = baseUrl;
    }

    public static synchronized RetrofitUtils getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils(mContext, mBaseUrl);
                }
            }
        }
        return instance;
    }

    private RetrofitUtils(Context context, String baseUrl) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient(context))
                    .baseUrl(baseUrl)
                    .addConverterFactory(CustomGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    /**
     * 设置okHttp
     */
    private OkHttpClient okHttpClient(Context context) {
        //开启Log
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                // 失败是否重新请求
                .retryOnConnectionFailure(false);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(logging);
        }

        // 保存session
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        builder.cookieJar(cookieJar);
        // 添加拦截器,塞一些公用的请求头
        builder.addInterceptor(new RequestInterceptor());
        OkHttpClient client = builder.build();
        return client;
    }

    /**
     * 获取Retrofit
     */
    private Retrofit getRetrofit() {
        return mRetrofit;
    }

    public <T> T create(Class<T> cls) {
        return getRetrofit().create(cls);
    }
}
