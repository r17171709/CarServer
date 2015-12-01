package com.renyu.carserver.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.MainActivity;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.LoginModel;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/11.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_name)
    EditText login_name;
    @Bind(R.id.login_password)
    EditText login_password;

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
        initViews();
    }

    private void initViews() {
        login_name.setText(ParamUtils.getLogin(this).get("name"));
        login_password.setText(ParamUtils.getLogin(this).get("password"));
        if (!(ParamUtils.getLogin(this).get("name").equals("")||ParamUtils.getLogin(this).get("password").equals(""))) {
            login(ParamUtils.getLogin(this).get("name"), ParamUtils.getLogin(this).get("password"));
        }
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
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.user.login", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("user_name", name);
        params.put("password", password);
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在登陆");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("LoginActivity", string);
                dismissDialog();

                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model=JsonParse.getLoginModel(string);
                    if (model==null) {
                        showToast("未知错误");
                    }
                    else if (model instanceof LoginModel) {
                        ParamUtils.setLoginModel(LoginActivity.this, (LoginModel) model);
                        ParamUtils.saveLogin(LoginActivity.this, name, password);

                        Intent intent_main=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent_main);
                        login_name.setText("");
                        login_password.setText("");
                    }
                    else if (model instanceof String) {
                        showToast((String) model);
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
