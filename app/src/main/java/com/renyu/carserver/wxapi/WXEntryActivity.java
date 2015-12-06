package com.renyu.carserver.wxapi;

import android.os.Bundle;

import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.ParamUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api=WXAPIFactory.createWXAPI(this, ParamUtils.weixin_appkey, false);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public int initContentView() {
		return 0;
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp resp) {

		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				showToast("分享成功");
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				showToast("分享取消");
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				showToast("您没有微信分享权限");
				break;
			default:
				showToast("未知错误，分享失败");
				break;
		}

		// TODO 微信分享 成功之后调用接口
		this.finish();
	}

}
