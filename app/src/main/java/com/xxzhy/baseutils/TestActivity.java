package com.xxzhy.baseutils;


import com.xxzhy.baseutils.base.BaseActivity;
import com.xxzhy.baseutils.fragment.AuthApi;
import com.xxzhy.baseutils.fragment.DataMoudle;
import com.xxzhy.baseutils.http.CallBack;
import com.xxzhy.baseutils.http.Http;
import com.xxzhy.baseutils.http.JsonResult;


public class TestActivity extends BaseActivity {
    @Override
    public int initView() {
        return 0;
    }

    @Override
    public void initData() {
        login();
    }
    private void login() {

        Http.Companion.getMhttp().createApi(AuthApi.class).version()
                .compose(this.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(this.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response,int code) {

                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                    }
                }));
    }
}
