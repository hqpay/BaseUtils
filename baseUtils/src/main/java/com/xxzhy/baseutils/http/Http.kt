package com.xxzhy.baseutils.http

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File
import java.util.concurrent.TimeUnit


class Http private constructor(
    context: Context,
    baseUrl: String,
    mInterceptor: Interceptor,
    mlogInterceptor: Interceptor
) {

    private val mRetrofit: Retrofit

    init {
        val httpCacheDirectory = File(
            context.applicationContext.cacheDir, context
                .applicationContext.packageName
        )
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        val client = OkHttpClient.Builder()
            .addInterceptor(mlogInterceptor)
            .addInterceptor(mInterceptor)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)//设置连接超时时间
            .cache(cache)
            .build()

        mRetrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
    }

    fun <T> createApi(tClass: Class<T>): T {
        return mRetrofit.create(tClass)
    }

    companion object {
        var mhttp: Http? = null
        val CONNECT_TIMEOUT = 50
        val READ_TIMEOUT = 50
        val WRITE_TIMEOUT = 50

        fun initHttp(
            context: Context,
            baseUrl: String,
            mInterceptor: Interceptor,
            mlogInterceptor: Interceptor
        ) {
            mhttp = Http(context, baseUrl, mInterceptor, mlogInterceptor)
        }
    }

}
