package com.xxzhy.baseutils.http


import com.xxzhy.baseutils.logs.Klog

import okhttp3.*
import okio.Buffer

import java.io.IOException
import java.nio.charset.Charset

import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by zhanghongyu on 19/4/25.
 */
class RequestLogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.method()
        Klog.d("请求方式 : $" + method + "$   请求地址 : " + request.url().toString())
        val response = chain.proceed(request)
        val requestBody = request.body()
        val logEnable = Klog.getSettings().getLogEnable()
        if (logEnable) {
            printParams(requestBody)
        }
        val responseBody = response.body()
        val responseBodyString = responseBody!!.string()
        Klog.d("返回数据 : $responseBodyString")
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
        }
        return response.newBuilder().body(
            ResponseBody.create(
                responseBody.contentType(),
                responseBodyString.toByteArray()
            )
        ).build()
    }

    private fun printParams(body: RequestBody?) {
        if (body == null) {
            return
        }
        val buffer = Buffer()
        try {
            body.writeTo(buffer)
            var charset: Charset? = Charset.forName("UTF-8")
            val contentType = body.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF_8)
            }
            val params = buffer.readString(charset!!)
            Klog.d("请求参数 : $params")
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
