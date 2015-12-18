package com.renyu.carserver.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by renyu on 15/11/14.
 */
public class MySaveActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.mysave_avatar)
    CircleImageView mysave_avatar;
    @Bind(R.id.mysave_name)
    TextView mysave_name;
    @Bind(R.id.mysave_tel)
    EditText mysave_tel;
    @Bind(R.id.mysave_password)
    TextView mysave_password;
    @Bind(R.id.mysave_newpass)
    EditText mysave_newpass;
    @Bind(R.id.mysave_repeatnewpass)
    EditText mysave_repeatnewpass;

    @Override
    public int initContentView() {
        return R.layout.activity_mysave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        view_toolbar_center_title.setText("账户安全");
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(ParamUtils.getLoginModel(this).getHead_photo(), mysave_avatar, getAvatarDisplayImageOptions());
        mysave_name.setText(ParamUtils.getLoginModel(this).getAccount_name());
        mysave_password.setText(ParamUtils.getLogin(this).get("password"));
        mysave_tel.setText(ParamUtils.getLoginModel(this).getLogin_phone());
    }

    @OnClick({R.id.view_toolbar_center_back, R.id.mysave_tel_change, R.id.mysave_password_change, R.id.mysave_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.mysave_tel_change:
                mysave_tel.setEnabled(true);
                break;
            case R.id.mysave_password_change:
                mysave_newpass.setEnabled(true);
                mysave_repeatnewpass.setEnabled(true);
                break;
            case R.id.mysave_save:
                passportSave();
                break;
        }
    }

    public DisplayImageOptions getAvatarDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    private void passportSave() {
        if (mysave_password.getText().toString().equals("")) {
            showToast("请输入原始密码");
            return;
        }
        if (mysave_newpass.getText().toString().equals("")) {
            showToast("请输入修改后的密码");
            return;
        }
        if (mysave_repeatnewpass.getText().toString().equals("")) {
            showToast("请再次输入修改后的密码");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.passport", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("old_pwd", mysave_password.getText().toString());
        params.put("new_pwd", mysave_newpass.getText().toString());
        params.put("confirm_pwd", mysave_repeatnewpass.getText().toString());
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在修改");
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
