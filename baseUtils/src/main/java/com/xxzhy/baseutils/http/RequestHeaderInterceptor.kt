package com.xxzhy.baseutils.http


import okhttp3.Interceptor
import okhttp3.Request

import java.io.IOException


class RequestHeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("platform", "1")
        return chain.proceed(builder.build())
    }

}