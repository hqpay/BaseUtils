package com.xxzhy.baseutils.http

abstract class CallBack<T> {
    fun filter(response: JsonResult<T>?) {
        try {
            if (response == null) {
                fail("网络请求失败", -1)
            } else {
                if (response.code === 200) {
                    success(response.data, response.code)
                } else {
                    fail(response.msg, response.code)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    abstract fun success(response: T?, code: Int)

    abstract fun fail(errorMessage: String?, status: Int)

}
