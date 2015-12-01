package com.renyu.carserver.imageUtils;

import android.content.Intent;
import android.os.Bundle;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.CommonUtils;

public class CameraActivity extends BaseActivity {

    @Override
    public int initContentView() {
        return R.layout.activity_camera;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setBackgroundDrawable(null);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new CameraFragment())
                    .commit();
        }
    }

    public void backTo(String filePath) {
        //刷新相册
        CommonUtils.refreshAlbum(this, filePath);
        //返回上一级目录
        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle.putString("path", filePath);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

}
