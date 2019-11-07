package com.xxzhy.baseutils

import android.content.Context
import com.xxzhy.baseutils.http.Http
import com.xxzhy.baseutils.http.RequestHeaderInterceptor
import com.xxzhy.baseutils.http.RequestLogInterceptor
import okhttp3.Interceptor

class BaseUtils {
    companion object {
        private var mBaseUtils: BaseUtils? = null
        private var mContext: Context? = null
        fun init(context: Context): BaseUtils {
            if (mBaseUtils == null) {
                mBaseUtils = BaseUtils()
            }
            this.mContext = context
            return mBaseUtils!!
        }
    }

    /**
     * @param mInterceptor  请求头拦截器
     * @param mlogInterceptor  log拦截器
     */
    fun initHttp(baseUrl: String, mInterceptor: Interceptor?, mlogInterceptor: Interceptor?) {
        if (mContext == null) {
            throw UnsupportedOperationException("mContext不能为null")
        }
        var interceptor = mInterceptor ?: RequestHeaderInterceptor()
        var logInterceptor = mlogInterceptor ?: RequestLogInterceptor()
        Http.initHttp(mContext!!, baseUrl, interceptor, logInterceptor)
    }
}