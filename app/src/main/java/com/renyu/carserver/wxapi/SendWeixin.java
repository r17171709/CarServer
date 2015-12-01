package com.renyu.carserver.wxapi;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.renyu.carserver.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class SendWeixin {

	public static String sendWeixin(Context context, String text, String url, String title, boolean isFriend) {
		IWXAPI api=WXAPIFactory.createWXAPI(context, "wx437d0c8be3e42d4f");
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion >= 0x21020001) {
			api.registerApp("wx437d0c8be3e42d4f");
			WXWebpageObject webpage=new WXWebpageObject();
			webpage.webpageUrl=url;
			WXMediaMessage msg=new WXMediaMessage(webpage);
			msg.title=title;
			msg.description=text;
			msg.thumbData=Bitmap2Bytes(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
			SendMessageToWX.Req req=new SendMessageToWX.Req();
			req.transaction=buildTransaction("webpage");
			req.message=msg;
			req.scene=isFriend?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
			api.sendReq(req);
			return "";
		} else {
			return "您当前使用的微信版本过低或未安装，分享失败";
		}
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	/**
	 * Bitmap转换成byte[]  
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
