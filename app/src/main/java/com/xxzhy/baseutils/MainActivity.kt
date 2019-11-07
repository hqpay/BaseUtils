package com.xxzhy.baseutils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import com.xxzhy.baseutils.base.BaseActivity
import com.xxzhy.baseutils.fragment.AuthApi
import com.xxzhy.baseutils.fragment.DataMoudle
import com.xxzhy.baseutils.fragment.DataMoudle.DataBean
import com.xxzhy.baseutils.http.CallBack
import com.xxzhy.baseutils.http.Http
import com.yqjr.superviseapp.utils.ext.click
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Result.Companion.success

class MainActivity : BaseActivity() {
    override fun initData() {
        version()
//        var intent = Intent(this,TestActivity::class.java)
//        startActivity(intent)
    }

    override fun initView(): Int {
        return R.layout.activity_main
    }


    private fun version() {
        Http.mhttp!!.createApi(AuthApi::class.java).version()
            .compose(bindToLifecycle())
            .compose(applySchedulers())
            .subscribe(newSubscriber(object : CallBack<DataBean>(){
                override fun fail(errorMessage: String?, status: Int) {
                }

                override fun success(response: DataBean?, code: Int) {
                }
            }))
    }
}
