package com.renyu.carserver.activity.my;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.LoginModel;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by renyu on 15/11/14.
 */
public class MyDataActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.mydata_avatar)
    CircleImageView mydata_avatar;
    @Bind(R.id.mydata_name)
    TextView mydata_name;
    @Bind(R.id.mydata_contactperson)
    TextView mydata_contactperson;
    @Bind(R.id.mydata_mobile)
    TextView mydata_mobile;
    @Bind(R.id.mydata_area)
    TextView mydata_area;
    @Bind(R.id.mydata_address)
    TextView mydata_address;
    @Bind(R.id.mydata_legal)
    TextView mydata_legal;
    @Bind(R.id.mydata_bussinesscode)
    TextView mydata_bussinesscode;

    String lastAvatar="";
    String cityIds="";
    String cityNames="";

    @Override
    public int initContentView() {
        return R.layout.activity_mydata;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("我的资料");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        ImageLoader.getInstance().displayImage(ParamUtils.getLoginModel(this).getHead_photo(), mydata_avatar, getAvatarDisplayImageOptions());

        getMyData();
    }

    @OnClick({R.id.mydata_avatar, R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mydata_avatar:
                if (!lastAvatar.equals("")) {
                    File file=new File(lastAvatar);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.view_toolbar_center_back:
                finish();
                break;
        }
    }

    private void getMyData() {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.shop.shopinfo", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("MyDataActivity", string);

                Object model= JsonParse.getMyData(string);
                if (model==null) {
                    showToast("未知错误");
                }
                else if (model instanceof String) {
                    showToast(model.toString());
                }
                else {
                    ImageLoader.getInstance().displayImage(((LoginModel) model).getHead_photo(), mydata_avatar, getAvatarDisplayImageOptions());
                    mydata_name.setText(((LoginModel) model).getShop_name());
                    mydata_contactperson.setText(((LoginModel) model).getContact_person());
                    mydata_mobile.setText(((LoginModel) model).getContact_tel());
                    String area="";
                    for (int i=0;i<((LoginModel) model).getAreaframe().split("/").length;i++) {
                        area+=CommonUtils.getCityInfo(((LoginModel) model).getAreaframe().split("/")[i]);
                        if (i!=((LoginModel) model).getAreaframe().split("/").length-1) {
                            cityIds+=((LoginModel) model).getAreaframe().split("/")[i]+",";
                        }
                        else {
                            cityIds+=((LoginModel) model).getAreaframe().split("/")[i];
                        }
                    }
                    mydata_area.setText(area);
                    mydata_address.setText(((LoginModel) model).getShop_addr());
                    mydata_legal.setText(((LoginModel) model).getShopuser_name());
                    mydata_bussinesscode.setText(((LoginModel) model).getBusiness_encoding());
                }
            }

            @Override
            public void onError() {
                showToast(getResources().getString(R.string.network_error));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ParamUtils.takecamera_result&&resultCode==RESULT_OK) {
            String filePath=data.getExtras().getString("path");
            cropImage(filePath);
        }
        else if(requestCode==ParamUtils.choicePic_result&&resultCode==RESULT_OK) {
            Uri uri=data.getData();
            String filePath;
            if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
                filePath= CommonUtils.getPath(uri, this);
            }
            else {
                String[] projection={MediaStore.Images.Media.DATA};
                Cursor cs=managedQuery(uri, projection, null, null, null);
                int columnNumber=cs.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cs.moveToFirst();
                filePath=cs.getString(columnNumber);
                filePath.replaceAll("file:///", "/");
            }
            Observable.just(filePath).map(new Func1<String, String>() {
                @Override
                public String call(String s) {
                    return CommonUtils.getScalePicturePathName(s);
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    if(s.equals("")) {
                        showToast("图片过小，请重新选择");
                    }
                    else {
                        cropImage(s);
                    }
                }
            });
        }
        else if(requestCode==ParamUtils.crop_result&&resultCode==RESULT_OK) {
            String filePath=data.getExtras().getString("path");
            //刷新头像
            ImageLoader.getInstance().displayImage("file://"+filePath, mydata_avatar);
            lastAvatar=filePath;

            updateMyData();
        }
    }

    public void updateMyData() {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.user.update", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("shop_name", mydata_name.getText().toString());
        params.put("contact_tel", mydata_mobile.getText().toString());
        params.put("area", cityIds);
        params.put("shop_addr", mydata_address.getText().toString());
        params.put("contact_person", mydata_contactperson.getText().toString());
        params.put("shopuser_name", mydata_legal.getText().toString());
        params.put("business_encoding", mydata_bussinesscode.getText().toString());
        params.put("shop_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        HashMap<String, File> filesMap=new HashMap<>();
        if (!lastAvatar.equals("")) {
            filesMap.put("head_photo", new File(lastAvatar));
        }
        httpHelper.asyncUpload(filesMap, ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在提交");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("MyDataActivity", string);

                dismissDialog();
                if (JsonParse.getResultValue(string)!=null) {
                    showToast(JsonParse.getResultValue(string));
                    if (JsonParse.getResultInt(string)==0 && !lastAvatar.equals("")) {
                        ParamUtils.setHeadPhoto(MyDataActivity.this, lastAvatar);
                    }
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();
                showToast(getResources().getString(R.string.network_error));
            }
        });
    }

    public DisplayImageOptions getAvatarDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
}
