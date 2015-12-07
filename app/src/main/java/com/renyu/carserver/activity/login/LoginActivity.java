package com.renyu.carserver.activity.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.MainActivity;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.LoginModel;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by renyu on 15/11/11.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_name)
    EditText login_name;
    @Bind(R.id.login_password)
    EditText login_password;
    @Bind(R.id.login_image)
    ImageView login_image;

    long loginTime=-1;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ParamUtils.isOpen) {
            finish();
            return;
        }
        if (!getIntent().getExtras().getBoolean("needJump")) {
            splashAnimation();
            initViews();
        }
        else {
            Intent intent_main=new Intent(LoginActivity.this, MainActivity.class);
            Bundle bundle=new Bundle();
            bundle.putBoolean("isNeedAnimation", true);
            intent_main.putExtras(bundle);
            startActivity(intent_main);
        }
    }

    private void splashAnimation() {
        ValueAnimator valueAnimator=ValueAnimator.ofInt(1000, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                login_image.setScaleX(1+animation.getAnimatedFraction()/2);
                login_image.setScaleY(1+animation.getAnimatedFraction()/2);
                login_image.setAlpha(1-animation.getAnimatedFraction());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                login_image.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                login_image.setImageResource(R.mipmap.splash_bg);
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    private void initViews() {
        login_name.setText(ParamUtils.getLogin(this).get("name"));
        login_password.setText(ParamUtils.getLogin(this).get("password"));
    }

    @OnClick({R.id.login_forget, R.id.login_joinserver, R.id.login_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_forget:
                Intent intent_forget=new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent_forget);
                break;
            case R.id.login_joinserver:
                Intent intent_join=new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent_join);
                break;
            case R.id.login_commit:
                login(login_name.getText().toString(), login_password.getText().toString());
                break;
        }
    }

    private void login(final String name, final String password) {
        if (name.equals("")) {
            showToast("请输入手机号/会员名/会员账号");
            return;
        }
        if (password.equals("")) {
            showToast("请输入密码");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.user.login", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("user_name", name);
        params.put("password", password);
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在登陆");
                loginTime=System.currentTimeMillis();
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                    dismissDialog();
                }
                else {
                    final Object model=JsonParse.getLoginModel(string);
                    if (model==null) {
                        showToast("未知错误");
                        dismissDialog();
                    }
                    else if (model instanceof LoginModel) {
                        long timeExtra=System.currentTimeMillis()-loginTime;
                        Observable.timer(timeExtra>2000?0:2000-timeExtra, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                dismissDialog();

                                ParamUtils.setLoginModel(LoginActivity.this, (LoginModel) model);
                                ParamUtils.saveLogin(LoginActivity.this, name, password);

                                Intent intent_main=new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putBoolean("isNeedAnimation", false);
                                intent_main.putExtras(bundle);
                                startActivity(intent_main);
                                login_name.setText("");
                                login_password.setText("");
                            }
                        });
                    }
                    else if (model instanceof String) {
                        showToast((String) model);
                        dismissDialog();
                    }
                }

            }

            @Override
            public void onError() {
                dismissDialog();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle=intent.getExtras();
        if (bundle.getInt("type")==ParamUtils.FINISH) {
            finish();
        }
        else if (bundle.getInt("type")==ParamUtils.NONEEDFINISH) {

        }
    }
}
