package com.renyu.carserver.activity.workbench;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.qqapi.QQActivity;
import com.renyu.carserver.sinaweiboapi.WBMainActivity;
import com.renyu.carserver.wxapi.SendWeixin;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/12.
 */
public class ShareActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;

    @Override
    public int initContentView() {
        return R.layout.activity_share;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        switch (getIntent().getExtras().getInt("type")) {
            case ParamUtils.RESULT_CARSERVER:
                view_toolbar_center_title.setText("服务商应用");
                break;
            case ParamUtils.RESULT_CARCLIENT:
                view_toolbar_center_title.setText("修理厂应用");
                break;
            case ParamUtils.RESULT_CARWEIXIN:
                view_toolbar_center_title.setText("微信公众号");
                break;
        }

        view_toolbar_center_back.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.share_qq, R.id.share_qzone, R.id.share_weixin, R.id.share_pyq, R.id.share_sina, R.id.share_sms, R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_qq:
                Intent intent_qq=new Intent(this, QQActivity.class);
                Bundle bundle_qq=new Bundle();
                bundle_qq.putString("text", "123");
                bundle_qq.putString("imageUrl", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1124272042,1748798456&fm=58");
                bundle_qq.putString("title", "title");
                bundle_qq.putString("url", "http://www.baidu.com");
                bundle_qq.putBoolean("isQQZone", false);
                intent_qq.putExtras(bundle_qq);
                startActivity(intent_qq);
                break;
            case R.id.share_qzone:
                Intent intent_qzone=new Intent(this, QQActivity.class);
                Bundle bundle_qzong=new Bundle();
                bundle_qzong.putString("text", "123");
                bundle_qzong.putString("imageUrl", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1124272042,1748798456&fm=58");
                bundle_qzong.putString("title", "title");
                bundle_qzong.putString("url", "http://www.baidu.com");
                bundle_qzong.putBoolean("isQQZone", true);
                intent_qzone.putExtras(bundle_qzong);
                startActivity(intent_qzone);
                break;
            case R.id.share_weixin:
                SendWeixin.sendWeixin(this, "123", "http://www.baidu.com", "title", false);
                break;
            case R.id.share_pyq:
                SendWeixin.sendWeixin(this, "123", "http://www.baidu.com", "title", true);
                break;
            case R.id.share_sina:
                Intent intent_sina=new Intent(this, WBMainActivity.class);
                Bundle bundle_sina=new Bundle();
                bundle_sina.putString("title", "title");
                bundle_sina.putString("description", "description");
                bundle_sina.putString("url", "http://www.baidu.com");
                bundle_sina.putString("defaultText", "defaultText");
                intent_sina.putExtras(bundle_sina);
                startActivity(intent_sina);
                break;
            case R.id.share_sms:
                Uri smsToUri = Uri.parse("smsto:");
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                intent.putExtra("sms_body", "http://www.baidu.com");
                startActivity(intent);
                break;
            case R.id.view_toolbar_center_back:
                finish();
        }
    }
}
