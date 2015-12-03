package com.renyu.carserver.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.imageUtils.CameraActivity;
import com.renyu.carserver.imageUtils.CameraBeforeActivity;
import com.renyu.carserver.imageUtils.CropActivity;

import butterknife.ButterKnife;

/**
 * Created by renyu on 15/10/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public OKHttpHelper httpHelper=null;
    ProgressDialog pd=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initContentView()!=0) {
            setContentView(initContentView());
            ButterKnife.bind(this);
        }
        httpHelper=new OKHttpHelper();
    }

    public abstract int initContentView();

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(String title, String message) {
        pd=ProgressDialog.show(this, title, message);
    }

    public void dismissDialog() {
        if (pd!=null) {
            pd.dismiss();
        }
    }

    public void openChoiceImage() {
        String[] choice={"相册", "拍照"};
        new AlertDialog.Builder(this).setTitle("请选择").setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        chooseImage();
                        break;
                    case 1:
                        takePicture();
                        break;
                }
            }
        }).show();
    }

    /**
     * 选择相册
     */
    private void chooseImage() {
        //调用系统相册
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, ParamUtils.choicePic_result);
    }

    /**
     * 照相
     */
    private void takePicture() {
        Intent intent=new Intent(this, CameraActivity.class);
        startActivityForResult(intent, ParamUtils.takecamera_result);
    }

    /**
     * 剪裁头像
     * @param path
     */
    public void cropImage(String path) {
        Intent intent=new Intent(this, CropActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("path", path);
        intent.putExtras(bundle);
        startActivityForResult(intent, ParamUtils.crop_result);
    }

    /**
     * 关闭键盘
     * @param view
     */
    public void hide(View view) {
        InputMethodManager inputmethodmanager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputmethodmanager != null)
            inputmethodmanager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 打开键盘
     * @param view
     */
    public void show(View view) {
        InputMethodManager inputmethodmanager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputmethodmanager != null)
            inputmethodmanager.showSoftInput(view, 0);
    }
}
