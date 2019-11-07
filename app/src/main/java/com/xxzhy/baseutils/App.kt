package com.xxzhy.baseutils


import androidx.multidex.MultiDexApplication

class App  : MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        BaseUtils.init(this).initHttp("http://af.test.17ebank.com:9013/",null,null)
    }
}