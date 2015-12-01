package com.renyu.carserver.activity.login;

import android.os.Bundle;
import android.view.View;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by renyu on 15/11/11.
 */
public class ForgetActivity extends BaseActivity {
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
                break;
            case R.id.forget_back:
                finish();
                break;
        }
    }
}
