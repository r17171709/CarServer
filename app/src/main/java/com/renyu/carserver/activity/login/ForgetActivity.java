package com.renyu.carserver.activity.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/11.
 */
public class ForgetActivity extends BaseActivity {

    @Bind(R.id.forget_name)
    EditText forget_name;
    @Bind(R.id.forget_phone)
    EditText forget_phone;

    @Override
    public int initContentView() {
        return R.layout.activity_forget;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.forget_sendphone, R.id.forget_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_sendphone:
                forget();
                break;
            case R.id.forget_back:
                finish();
                break;
        }
    }

    private void forget() {
        if (forget_name.getText().toString().equals("")) {
            showToast("请输入账号");
            return;
        }
        if (forget_phone.getText().toString().equals("")) {
            showToast("请输入手机号");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.user.forgotPassword", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("login_phone", forget_phone.getText().toString());
        params.put("login_name", forget_name.getText().toString());
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在提交");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                dismissDialog();
                if (JsonParse.getResultValue(string)!=null) {
                    showToast(JsonParse.getResultValue(string));
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();
                showToast(getResources().getString(R.string.network_error));
            }
        });
    }
}
