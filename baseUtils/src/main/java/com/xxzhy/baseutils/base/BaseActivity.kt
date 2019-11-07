package com.xxzhy.baseutils.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.xxzhy.baseutils.R
import com.xxzhy.baseutils.http.CallBack
import com.xxzhy.baseutils.http.JsonResult
import com.xxzhy.baseutils.http.JsonUtil
import com.xxzhy.baseutils.manager.AppManager
import retrofit2.adapter.rxjava.HttpException
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.IOException

abstract class BaseActivity : RxAppCompatActivity() {
    private var pg: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initView())
        AppManager.initAppManager().addActivity(this)
        initData()
    }

    abstract fun initView(): Int

    abstract fun initData()

    /**
     * RxJava线程调度
     * subscribeOn指定观察者代码运行的线程
     * observerOn()指定订阅者运行的线程
     *
     * @param <T>
     * @return
    </T> */
    fun <T> applySchedulers(): Observable.Transformer<T, T> {
        return Observable.Transformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * RxJava对象转换
     * 转换、提取JsonResult包裹的对象
     *
     * @param <T>
     * @return
    </T> */
    protected fun <T> convert(): Func1<JsonResult<T>, T> {
        return Func1 { jsonResult ->
            if (jsonResult.isOk()) {
                jsonResult.data
            } else null
        }
    }

    /**
     * RxJava空对象过滤
     * 返回false，即空对象不传递到订阅者处理
     *
     * @param <T>
     * @return
    </T> */
    protected fun <T> emptyObjectFilter(): Func1<T, Boolean> {
        return Func1 { response -> response != null }
    }

    /**
     * RxJava简化版本网络异步回调
     * 过滤了错误码，直接返回实体对象
     *
     * @param callBack
     * @param <T>
     * @return
    </T> */
    fun <T> newSubscriber(callBack: CallBack<T>?): Subscriber<JsonResult<T>> {
        return object : Subscriber<JsonResult<T>>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                var errorMessage = "请求失败"
                var status = -1
                if (e is HttpException) {
                    val body = e.response()!!.errorBody()
                    try {
                        Log.d("返回数据 : ", body!!.string())
                        val result = JsonUtil.fromJson(body.string(), JsonResult::class.java)
                        if (result != null && result!!.msg != null) {
                            errorMessage = result!!.msg
                            status = result!!.code
                        }
                    } catch (ioe: IOException) {
                        dismissProgress()
                        ioe.printStackTrace()
                    }
                } else {
                    dismissProgress()
                    e.printStackTrace()
                }
                if (callBack == null) {
                    dismissProgress()
                    return
                }
                callBack!!.fail(errorMessage, status)
            }

            override fun onNext(response: JsonResult<T>) {
                if (isUnsubscribed) {
                    return
                }
                if (callBack == null) {
                    return
                }
                callBack!!.filter(response)
            }
        }
    }

    fun showProgress(isCancel: Boolean) {
        try {
            if (pg == null) {
                val view = View.inflate(this, R.layout.dialog_loading, null as ViewGroup?)
                pg = Dialog(this, R.style.myDialogLoading)
                pg!!.setContentView(view)
                pg!!.setCanceledOnTouchOutside(isCancel)
            }
            if (!pg!!.isShowing) {
                pg!!.setCancelable(isCancel)
                pg!!.show()
            }
        } catch (var3: IOException) {
            var3.printStackTrace()
        }
    }

    /*
	 * 进度条消失
	 */
    fun dismissProgress() {
        if (pg != null && pg!!.isShowing) {
            pg!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgress()
    }
}
