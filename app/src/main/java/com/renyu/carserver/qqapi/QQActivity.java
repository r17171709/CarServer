package com.renyu.carserver.qqapi;

import com.renyu.carserver.commons.ParamUtils;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class QQActivity extends Activity {

    public Tencent mTencent;
    public static String mAppid= ParamUtils.qq_appkey;

    int mExtarFlag = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mTencent=Tencent.createInstance(mAppid, getApplicationContext());
        if(!mTencent.isSupportSSOLogin(QQActivity.this)) {
            Toast.makeText(this, "请先安装QQ手机客户端，再分享", Toast.LENGTH_LONG).show();
            finish();
            return ;
        }

        share(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("imageUrl"), getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("url"), getIntent().getExtras().getBoolean("isQQZone"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // must call mTencent.onActivityResult.
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
        }
    }

    public void share(String text, String imageUrl, String title, String url, boolean isQQZone) {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, text);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "my app");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        if (isQQZone) {
            mExtarFlag |= QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
        }
        else {
            mExtarFlag &= (0xFFFFFFFF - QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                mTencent.shareToQQ(QQActivity.this, params, qqShareListener);
            }
        });
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            Log.d("QQActivity", "onCancel");
            Toast.makeText(QQActivity.this, "已取消", Toast.LENGTH_SHORT).show();
            finish();
        }
        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Log.d("QQActivity", "onComplete: " + response.toString());
            Toast.makeText(QQActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            finish();
        }
        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Log.d("QQActivity", "onError: " + e.errorMessage);
            Toast.makeText(QQActivity.this, "QQ分享失败"+e.errorMessage+"，请稍后再试", Toast.LENGTH_SHORT).show();
            finish();
        }
    };
}