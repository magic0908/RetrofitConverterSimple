package com.xing.retrofitconverter.api;

import com.xing.retrofitconverter.api.http.MyConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creation Time: 2018/10/26 13:01.
 * Author: King.
 * Description: Retrofit请求封装
 */
public class RetrofitClient {

    private static RetrofitClient mInstance;

    private Retrofit mRetrofit;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitClient.class) {
                if (mInstance == null) mInstance = new RetrofitClient();
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象与参数
     */
    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //链接超时
                .connectTimeout(90, TimeUnit.SECONDS)
                //读取超时
                .readTimeout(90, TimeUnit.SECONDS)
                //失败自动重连
                .retryOnConnectionFailure(true);
        //初始化Retrofit并添加配置
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(ApiService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                //这里是自定义的GsonConverterFactory
                .addConverterFactory(MyConverterFactory.create())
                .build();
    }

    public <T> T getApi(Class<T> clz) {
        return mRetrofit.create(clz);
    }
}
