package com.renyu.carserver.qqapi;

import com.renyu.carserver.base.BaseActivity;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class QQActivity extends BaseActivity {

    public Tencent mTencent;
    public static String mAppid="1104982206";

    int mExtarFlag = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mTencent=Tencent.createInstance(mAppid, getApplicationContext());
        if(!mTencent.isSupportSSOLogin(QQActivity.this)) {
            showToast("请先安装QQ手机客户端，再分享");
            finish();
            return ;
        }

        share(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("imageUrl"), getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("url"), getIntent().getExtras().getBoolean("isQQZone"));
    }

    @Override
    public int initContentView() {
        return 0;
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
        }
        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Log.d("QQActivity", "onComplete: " + response.toString());
            finish();
        }
        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Log.d("QQActivity", "onError: " + e.errorMessage);
            finish();
        }
    };
}