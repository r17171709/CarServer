package com.renyu.carserver.imageUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.renyu.carserver.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImagePreviewFragment extends Fragment {
	
	PhotoView photo_view=null;
	
	public static ImagePreviewFragment getInstance(String imageUrl) {
		ImagePreviewFragment fragment=new ImagePreviewFragment();
		Bundle bundle=new Bundle();
		bundle.putString("imageUrl", imageUrl);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			listener=(OnFinishListener) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + " must implement OnFinishListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_imagepreview, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		photo_view=(PhotoView) view.findViewById(R.id.photo_view);
		photo_view.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				listener.onFinish();
			}
		});
		ImageLoader.getInstance().displayImage(getArguments().getString("imageUrl"), photo_view);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(photo_view!=null) {
			photo_view.setScale(1, false);
		}
	}
	
	public interface OnFinishListener {
		public void onFinish();
	}
	
	public OnFinishListener listener;
	

}
