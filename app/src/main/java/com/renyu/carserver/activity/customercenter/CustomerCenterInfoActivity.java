package com.renyu.carserver.activity.customercenter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.renyu.carserver.R;
import com.renyu.carserver.activity.login.AreaActivity;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.CustomerModel;
import com.renyu.carserver.model.JsonParse;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by renyu on 15/11/12.
 */
public class CustomerCenterInfoActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.customercenterinfo_id)
    TextView customercenterinfo_id;
    @Bind(R.id.customercenterinfo_name)
    EditText customercenterinfo_name;
    @Bind(R.id.customercenterinfo_contact)
    EditText customercenterinfo_contact;
    @Bind(R.id.customercenterinfo_company)
    EditText customercenterinfo_company;
    @Bind(R.id.customercenterinfo_area)
    TextView customercenterinfo_area;
    @Bind(R.id.customercenterinfo_address)
    EditText customercenterinfo_address;
    @Bind(R.id.customercenterinfo_phonenum)
    EditText customercenterinfo_phonenum;
    @Bind(R.id.customercenterinfo_legalperson)
    EditText customercenterinfo_legalperson;
    @Bind(R.id.customercenterinfo_businesslicense)
    EditText customercenterinfo_businesslicense;
    @Bind(R.id.customercenterinfo_idphoto_1)
    ImageView customercenterinfo_idphoto_1;
    @Bind(R.id.customercenterinfo_idphoto_2)
    ImageView customercenterinfo_idphoto_2;
    @Bind(R.id.customercenterinfo_businesslicense_photo)
    ImageView customercenterinfo_businesslicense_photo;
    @Bind(R.id.customercenterinfo_shop_photo)
    ImageView customercenterinfo_shop_photo;
    @Bind(R.id.customercenterinfo_legalpersonid)
    EditText customercenterinfo_legalpersonid;
    @Bind(R.id.customercenterinfo_creditline)
    EditText customercenterinfo_creditline;
    @Bind(R.id.customercenterinfo_email)
    EditText customercenterinfo_email;
    @Bind(R.id.customercenterinfo_zipcode)
    EditText customercenterinfo_zipcode;

    CustomerModel model=null;
    String cityIds="";
    String cityNames="";
    String pic1="";
    String pic2="";
    String pic3="";
    String pic4="";
    int imagePos=1;

    boolean isChange;

    @Override
    public int initContentView() {
        return R.layout.activity_customercenterinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model= (CustomerModel) getIntent().getExtras().getSerializable("model");

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("会员基本信息");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        customercenterinfo_id.setText(model.getLogin_account());
        customercenterinfo_name.setText(model.getAccount_name());
        customercenterinfo_contact.setText(model.getContact_person());
        customercenterinfo_company.setText(model.getRepairdepot_name());
        for (int i=0;i<model.getAreaframe().split("/").length;i++) {
            cityNames+= CommonUtils.getCityInfo(model.getAreaframe().split("/")[i]);
            if (i!=model.getAreaframe().split("/").length-1) {
                cityIds+=model.getAreaframe().split("/")[i]+",";
            }
            else {
                cityIds+=model.getAreaframe().split("/")[i];
            }
        }
        customercenterinfo_area.setText(cityNames);
        customercenterinfo_address.setText(model.getRepairdepot_address());
        customercenterinfo_phonenum.setText(model.getMobile());
        customercenterinfo_legalperson.setText(model.getCorporation());
        customercenterinfo_legalpersonid.setText(model.getCorporation_codeId());
        customercenterinfo_businesslicense.setText(model.getBusiness_encoding());
        ImageLoader.getInstance().displayImage(model.getBusiness_photo(), customercenterinfo_idphoto_1, getAvatarDisplayImageOptions());
        ImageLoader.getInstance().displayImage(model.getCorporation_codeId_photo(), customercenterinfo_idphoto_2, getAvatarDisplayImageOptions());
        ImageLoader.getInstance().displayImage(model.getShop_photo(), customercenterinfo_businesslicense_photo, getAvatarDisplayImageOptions());
        ImageLoader.getInstance().displayImage(model.getShop_photo_new(), customercenterinfo_shop_photo, getAvatarDisplayImageOptions());
    }

    @OnClick({R.id.view_toolbar_center_back, R.id.customercenterinfo_area, R.id.customercenterinfo_next,
            R.id.customercenterinfo_idphoto_1, R.id.customercenterinfo_idphoto_2,
            R.id.customercenterinfo_businesslicense_photo, R.id.customercenterinfo_shop_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.customercenterinfo_area:
                Intent intent=new Intent(CustomerCenterInfoActivity.this, AreaActivity.class);
                startActivityForResult(intent, ParamUtils.RESULT_AREA);
                break;
            case R.id.customercenterinfo_idphoto_1:
                imagePos=1;
                if (!pic1.equals("")) {
                    File file=new File(pic1);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.customercenterinfo_idphoto_2:
                imagePos=2;
                if (!pic2.equals("")) {
                    File file=new File(pic2);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.customercenterinfo_businesslicense_photo:
                imagePos=3;
                if (!pic3.equals("")) {
                    File file=new File(pic3);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.customercenterinfo_shop_photo:
                imagePos=4;
                if (!pic4.equals("")) {
                    File file=new File(pic4);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.customercenterinfo_next:
                updateCustomer();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ParamUtils.RESULT_AREA&&resultCode==RESULT_OK) {
            cityIds=data.getExtras().getString("value");
            cityNames="";
            String[] values=data.getExtras().getString("value").split(",");
            for (int i=0;i<values.length;i++) {
                cityNames+= CommonUtils.getCityInfo(values[i]);
            }
            customercenterinfo_area.setText(cityNames);
        }
        else if(requestCode==ParamUtils.takecamera_result&&resultCode==RESULT_OK) {
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
            if (imagePos==1) {
                ImageLoader.getInstance().displayImage("file://"+filePath, customercenterinfo_idphoto_1);
                pic1=filePath;
            }
            else if (imagePos==2) {
                ImageLoader.getInstance().displayImage("file://"+filePath, customercenterinfo_idphoto_2);
                pic2=filePath;
            }
            else if (imagePos==3) {
                ImageLoader.getInstance().displayImage("file://"+filePath, customercenterinfo_businesslicense_photo);
                pic3=filePath;
            }
            else if (imagePos==4) {
                ImageLoader.getInstance().displayImage("file://"+filePath, customercenterinfo_shop_photo);
                pic4=filePath;
            }
        }
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

    private void updateCustomer() {
        if (customercenterinfo_name.getText().toString().equals("")) {
            showToast("请填写会员名");
            return;
        }
        if (cityIds.equals("")) {
            showToast("请选择所在地区");
            return;
        }
        if (customercenterinfo_address.getText().toString().equals("")) {
            showToast("请填写详细地址");
            return;
        }
        if (customercenterinfo_phonenum.getText().toString().equals("")) {
            showToast("请填写联系电话");
            return;
        }
        if (customercenterinfo_phonenum.getText().toString().length()<11) {
            showToast("手机号码格式错误，请重新填写");
            return;
        }
        if (customercenterinfo_legalperson.getText().toString().equals("")) {
            showToast("请填写法人/经营者姓名");
            return;
        }
        if (customercenterinfo_legalpersonid.getText().toString().equals("")) {
            showToast("请填写身份证号码");
            return;
        }
        if (customercenterinfo_businesslicense.getText().toString().equals("")) {
            showToast("请填写营业执照号");
            return;
        }
        if (customercenterinfo_contact.getText().toString().equals("")) {
            showToast("请填写联系人");
            return;
        }
        if (customercenterinfo_company.getText().toString().equals("")) {
            showToast("请填写单位全称");
            return;
        }
        if (customercenterinfo_creditline.getText().toString().equals("")) {
            showToast("初始化授信额度不能为空");
            return;
        }
        if (pic1.equals("")) {
            showToast("请添加身份证正面照片");
            return;
        }
        if (pic2.equals("")) {
            showToast("请添加身份证背面照片");
            return;
        }
        if (pic3.equals("")) {
            showToast("请添加营业执照照片");
            return;
        }
        if (pic4.equals("")) {
            showToast("请添加门头照片照片");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.xiuliuser.update", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("account_name", customercenterinfo_name.getText().toString());
        params.put("area", cityIds);
        params.put("repairdepot_address", customercenterinfo_address.getText().toString());
        params.put("mobile", customercenterinfo_phonenum.getText().toString());
        params.put("corporation", customercenterinfo_legalperson.getText().toString());
        params.put("corporation_codeId", customercenterinfo_legalpersonid.getText().toString());
        params.put("business_encoding", customercenterinfo_businesslicense.getText().toString());
        params.put("contact_person", customercenterinfo_contact.getText().toString());
        params.put("repairdepot_name", customercenterinfo_company.getText().toString());
        params.put("user_id", ""+model.getUser_id());
        HashMap<String, File> fileHashMap=new HashMap<>();
        if (!pic1.equals("")) {
            fileHashMap.put("business_photo", new File(pic1));
        }
        if (!pic2.equals("")) {
            fileHashMap.put("corporation_codeId_photo", new File(pic2));
        }
        if (!pic3.equals("")) {
            fileHashMap.put("shop_photo", new File(pic3));
        }
        if (!pic4.equals("")) {
            fileHashMap.put("shop_photo_new", new File(pic4));
        }
        httpHelper.uploadFile(fileHashMap, ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在更新");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("CustomerCenterInfoActiv", string);
                dismissDialog();
                if (JsonParse.getResultValue(string)!=null) {
                    showToast(JsonParse.getResultValue(string));

                    isChange=true;
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
    @Override
    public void onBackPressed() {
        if (isChange) {
            Intent intent=new Intent();
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        super.onBackPressed();
    }

}
