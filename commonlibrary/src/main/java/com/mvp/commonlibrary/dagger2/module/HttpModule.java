package com.mvp.commonlibrary.dagger2.module;

import android.content.Context;
import com.mvp.commonlibrary.constant.Api;
import com.mvp.commonlibrary.dagger2.scope.ApiUrl;
import com.mvp.commonlibrary.http.retrofit.HttpUtils;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 描述： 可以将HttpModule写成抽象类，放在framelibrary层。
 * 作者： 天天童话丶
 * 时间： 2018/12/9.
 */
@Module
public class HttpModule {
    private Context mContext;
    private Api mApi;

    public HttpModule(Context context, Api api){
        mContext = context;
        mApi = api;
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return HttpUtils.provideRetrofitBuilder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return HttpUtils.provideOkHttpBuilder();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder){
        return HttpUtils.provideOkHttpClient(mContext, builder);
    }

    @Provides
    @Singleton
    @ApiUrl
    Retrofit provideApiRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return HttpUtils.createRetrofit(builder, client, mApi.getBaseUrl());
    }

    @Singleton
    @Provides
    Api provideApi(@ApiUrl Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}