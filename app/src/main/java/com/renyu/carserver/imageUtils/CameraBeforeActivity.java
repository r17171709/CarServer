package com.renyu.carserver.imageUtils;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.ParamUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CameraBeforeActivity extends Activity {
	
	String filePath="";
	File cameraFile=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState!=null&&savedInstanceState.getString("filePath")!=null) {
			filePath=savedInstanceState.getString("filePath");
			returnFilePath(filePath);
			return;
		}
		
		String dirPath= ParamUtils.IMAGECACHE;
		cameraFile=new File(dirPath+"/"+System.currentTimeMillis()+".jpg");
		if(!cameraFile.exists()) {
			try {
				cameraFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Uri cameraUrl=Uri.fromFile(cameraFile);
		filePath=cameraFile.getPath();
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUrl);
		startActivityForResult(intent, ParamUtils.takecamera_result);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("filePath", filePath);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		//拍照
		if(arg1==RESULT_OK&&arg0==ParamUtils.takecamera_result) {
			if(cameraFile!=null) {
				filePath=cameraFile.getPath();
				returnFilePath(filePath);
			}
		}
		else {
			finish();
			return ;
		}
	}
	
	public void returnFilePath(String filePath) {
		Observable.just(filePath).map(new Func1<String, String>() {
			@Override
			public String call(String s) {
				String filePath= CommonUtils.getScalePicturePathName(s);
				//删除拍摄完成的图片
				new File(s).delete();
				return filePath;
			}
		}).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
			@Override
			public void call(String s) {
				//刷新相册
				CommonUtils.refreshAlbum(CameraBeforeActivity.this, s);
				//返回上一级目录
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putString("path", s);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
