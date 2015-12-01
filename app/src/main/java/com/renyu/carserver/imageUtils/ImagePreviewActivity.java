package com.renyu.carserver.imageUtils;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;

public class ImagePreviewActivity extends BaseActivity {
	
	ViewPager imagepreview_viewpager=null;
	
	ArrayList<String> images=null;
	ArrayList<Fragment> fragments=null;

	@Override
	public int initContentView() {
		return R.layout.activity_imagepreview;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		images=getIntent().getExtras().getStringArrayList("images");
		fragments=new ArrayList<Fragment>();
		for(int i=0;i<images.size();i++) {
			ImagePreviewFragment fragment=ImagePreviewFragment.getInstance(images.get(i));
			fragments.add(fragment);
		}
		
		init();
	}
	
	private void init() {
		imagepreview_viewpager=(ViewPager) findViewById(R.id.imagepreview_viewpager);
		imagepreview_viewpager.setAdapter(new MyAdapter(getSupportFragmentManager()));
	}
	
	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.size();
		}
	}
}
