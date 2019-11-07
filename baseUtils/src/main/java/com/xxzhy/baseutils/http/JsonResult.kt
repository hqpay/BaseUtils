package com.xxzhy.baseutils.http


class JsonResult<T> {
    var data: T? = null
    var code: Int = 0
    var msg: String = ""

    fun isOk(): Boolean{
            if (code == 0) {
                return true
            }
            return if (code == 401) {
                false
            } else false
        }

    override fun toString(): String {
        return JsonUtil.toJson(this).toString()
    }

}
