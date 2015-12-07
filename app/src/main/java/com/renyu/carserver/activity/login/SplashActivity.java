package com.renyu.carserver.activity.login;

import android.content.Intent;
import android.os.Bundle;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.MainActivity;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.LoginModel;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by renyu on 15/11/11.
 */
public class SplashActivity extends BaseActivity {

    long loginTime=-1;

    @Override
    public int initContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ParamUtils.getLogin(SplashActivity.this).get("name").equals("")&&!ParamUtils.getLogin(SplashActivity.this).get("password").equals("")) {
            login(ParamUtils.getLogin(SplashActivity.this).get("name"), ParamUtils.getLogin(SplashActivity.this).get("password"));
        }
        else {
            Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    jumpToLogin(false);
                }
            });
        }
    }

    private void login(final String name, final String password) {
        loginTime=System.currentTimeMillis();
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.user.login", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("user_name", name);
        params.put("password", password);
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                if (JsonParse.getResultInt(string)==1) {
                    jumpToLogin(false);
                }
                else {
                    final Object model=JsonParse.getLoginModel(string);
                    if (model==null) {
                        jumpToLogin(false);
                    }
                    else if (model instanceof LoginModel) {
                        long timeExtra=System.currentTimeMillis()-loginTime;

                        Observable.timer(timeExtra>2000?0:2000-timeExtra, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                ParamUtils.setLoginModel(SplashActivity.this, (LoginModel) model);
                                ParamUtils.saveLogin(SplashActivity.this, name, password);

                                jumpToLogin(true);
                            }
                        });

                    }
                    else if (model instanceof String) {
                        jumpToLogin(false);
                    }
                }

            }

            @Override
            public void onError() {
                jumpToLogin(false);
            }
        });
    }

    private void jumpToLogin(boolean flag) {
        Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("needJump", flag);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
