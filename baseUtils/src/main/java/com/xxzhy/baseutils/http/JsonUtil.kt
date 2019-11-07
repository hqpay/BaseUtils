package com.xxzhy.baseutils.http

import android.text.TextUtils
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.Collections

object JsonUtil {

    private var gson: Gson? = null

    init {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(
            JsonObject::class.java,
            JsonDeserializer<Any> { jsonElement, type, jsonDeserializationContext -> jsonElement.asJsonObject })

        gson = builder.disableHtmlEscaping().create()
    }

    fun toJson(`object`: Any?): String? {
        return if (`object` == null) {
            null
        } else gson!!.toJson(`object`)
    }

    fun <T> fromJson(content: String, clazz: Class<T>?): T? {
        if (TextUtils.isEmpty(content) || clazz == null) {
            return null
        }
        try {
            return gson!!.fromJson(content, clazz)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            return null
        }

    }

    fun <T> fromJson(content: String, token: TypeToken<T>?): T? {
        if (TextUtils.isEmpty(content) || token == null) {
            return null
        }
        try {
            return gson!!.fromJson<T>(content, token.type)
        } catch (e: JsonSyntaxException) {
            return null
        }

    }

    /**
     * 把json转成对应的类型。适合用于自定义数据类型，如ArrayList<Foo>等
     * @param content json
     * @param type 自定义类型的token。使用方法如下
     * Type listType = new TypeToken<ArrayList></ArrayList><Foo>>(){}.getType();
     * @param <T>
     * @return 对应类型的对象
    </T></Foo></Foo> */
    fun <T> fromJson(content: String, type: Type?): T? {
        if (!TextUtils.isEmpty(content) && type != null) {
            try {
                return gson!!.fromJson<T>(content, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun toMap(obj: Any): Map<String, Any> {
        val element = gson!!.toJsonTree(obj)
        return gson!!.fromJson<Map<String, Any>>(element, Map::class.java)
    }

    fun <T> fromObject(obj: Any, clazz: Class<T>): T {
        val element = gson!!.toJsonTree(obj)
        return gson!!.fromJson(element, clazz)
    }

    fun <T> fromObject(obj: Any, token: TypeToken<T>): T? {
        val element = gson!!.toJsonTree(obj)
        return gson!!.fromJson<T>(element, token.type)
    }

    fun getMap(map: Map<String, Any>?, key: String?): Map<*, *>? {
        if (map == null || key == null) {
            return null
        }
        val value = map[key]
        return if (value is Map<*, *>) {
            value
        } else null
    }

    fun getLong(map: Map<String, Any>?, key: String?): Long? {
        if (map == null || key == null) {
            return null
        }
        val value = map[key] ?: return null
        if (value is Number) {
            return value.toLong()
        }
        try {
            return java.lang.Long.parseLong(value.toString())
        } catch (e: NumberFormatException) {
            return null
        }
    }

    /**
     * 从json中搜索，根据键的名字，返回值。
     * @param json
     * @param name json中的键名
     * @return Object
     */
    fun findObject(json: String, name: String): Any? {

        var `object`: Any? = null

        if (TextUtils.isEmpty(json) || TextUtils.isEmpty(name)) {
            return null
        }

        try {
            val jsonobject = JSONObject(json)
            if (!jsonobject.has(name)) {
                return null
            } else {
                `object` = jsonobject.get(name)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return `object`
    }
}
