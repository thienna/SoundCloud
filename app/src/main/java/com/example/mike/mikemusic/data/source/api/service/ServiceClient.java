package com.example.mike.mikemusic.data.source.api.service;

import android.app.Application;

import com.example.mike.mikemusic.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ThienNA on 09/08/2018.
 */

public class ServiceClient {
    private static final int CONNECTION_TIMEOUT = 10;

    static <T> T createService(Application application, String endPoint, Class<T> serviceClass) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        int cacheSize = Constants.DEFAULT_CACHE_SIZE;
        httpClientBuilder.cache(new Cache(application.getCacheDir(), cacheSize));
        httpClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(endPoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(logging);
        Retrofit retrofit = retrofitBuilder.client(httpClientBuilder.build()).build();
        return retrofit.create(serviceClass);
    }
}
