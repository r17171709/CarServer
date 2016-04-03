package com.renyu.carserver.activity.workbench;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 16/3/29.
 */
public class SendMessageActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.sendmessage_name)
    TextView sendmessage_name;
    @Bind(R.id.sendmessage_title)
    EditText sendmessage_title;
    @Bind(R.id.sendmessage_content)
    EditText sendmessage_content;

    int id=-2;

    @Override
    public int initContentView() {
        return R.layout.activity_sendmessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_title.setText("站内信");
    }

    @OnClick({R.id.sendmessage_commit, R.id.view_toolbar_center_back, R.id.sendmessage_choice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendmessage_commit:
                if (id==-2) {
                    showToast("请选择收件人");
                    return;
                }
                else if (sendmessage_title.getText().toString().equals("")) {
                    showToast("请填写标题");
                    return;
                }
                else if (sendmessage_content.getText().toString().equals("")) {
                    showToast("请填写内容");
                    return;
                }
                sendMessage();
                break;
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.sendmessage_choice:
                startActivityForResult(new Intent(SendMessageActivity.this, MessageChoiceActivity.class), ParamUtils.RESULT_MESSAGECHOICE);
                break;
        }
    }

    private void sendMessage() {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.message.notice", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("from_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("to_id", ""+id);
        params.put("type", id==-1?"5":"2");
        params.put("title", sendmessage_title.getText().toString());
        params.put("content", sendmessage_content.getText().toString());
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在发送");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                dismissDialog();
                showToast("发送成功");
                finish();
            }

            @Override
            public void onError() {
                dismissDialog();
                showToast("发送失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ParamUtils.RESULT_MESSAGECHOICE && resultCode==RESULT_OK) {
            id=data.getExtras().getInt("id");
            sendmessage_name.setText("收件人  "+data.getExtras().getString("name"));
        }
    }
}
