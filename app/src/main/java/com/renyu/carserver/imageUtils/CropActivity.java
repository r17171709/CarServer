package com.renyu.carserver.imageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edmodo.cropper.CropImageView;
import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.ParamUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class CropActivity extends BaseActivity {

	@Bind(R.id.view_toolbar_center_title)
	TextView view_toolbar_center_title;
	@Bind(R.id.view_toolbar_center_back)
	ImageView view_toolbar_center_back;
	@Bind(R.id.view_toolbar_center_next)
	ImageView view_toolbar_center_next;
	@Bind(R.id.cropImg)
	CropImageView mCropImage=null;
	Bitmap bmp=null;
	Bitmap cropBmp=null;

	@Override
	public int initContentView() {
		return R.layout.activity_crop;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		bmp=BitmapFactory.decodeFile(getIntent().getExtras().getString("path"));
		
		init();
	}
	
	private void init() {
		view_toolbar_center_title.setText("剪裁");
		view_toolbar_center_next.setVisibility(View.VISIBLE);
		view_toolbar_center_next.setImageResource(R.mipmap.ic_crop);
		view_toolbar_center_back.setVisibility(View.VISIBLE);
		mCropImage.setFixedAspectRatio(true);
		mCropImage.setImageBitmap(bmp);
	}
	
	public void crop() {
		final Handler handler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				Intent intent=getIntent();
				Bundle bundle=new Bundle();
				bundle.putString("path", msg.obj.toString());
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		};
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				cropBmp=mCropImage.getCroppedImage();
				String path=writeImage(cropBmp, ParamUtils.IMAGECACHE+"/"+System.currentTimeMillis()+".png", 100);
				Message m=new Message();
				m.obj=path;
				handler.sendMessage(m);
			}
		}).start();
	}
	
	public String writeImage(Bitmap bitmap,String destPath,int quality) {
		File file=new File(destPath);
		try {
			if(file.exists()) {
				file.delete();
			}
			if (file.createNewFile()) {
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getPath();
	}

	@OnClick({R.id.view_toolbar_center_next, R.id.view_toolbar_center_back})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.view_toolbar_center_next:
				crop();
				break;
			case R.id.view_toolbar_center_back:
				finish();
				break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bmp!=null&&!bmp.isRecycled()) {
			bmp.recycle();
			bmp=null;
		}
		if(cropBmp!=null&&!cropBmp.isRecycled()) {
			cropBmp.recycle();
			cropBmp=null;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		File file=new File(getIntent().getExtras().getString("path"));
		if (file.exists()) {
			file.delete();
		}
	}
}
