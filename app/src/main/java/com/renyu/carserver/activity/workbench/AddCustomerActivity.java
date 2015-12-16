package com.renyu.carserver.activity.workbench;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.renyu.carserver.R;
import com.renyu.carserver.activity.login.AreaActivity;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
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
public class AddCustomerActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.addcustomer_layout1)
    ScrollView addcustomer_layout1;
    @Bind(R.id.addcustomer_layout2)
    ScrollView addcustomer_layout2;
    @Bind(R.id.addcustomer_name)
    EditText addcustomer_name;
    @Bind(R.id.addcustomer_area)
    TextView addcustomer_area;
    @Bind(R.id.addcustomer_address)
    EditText addcustomer_address;
    @Bind(R.id.addcustomer_contact)
    EditText addcustomer_contact;
    @Bind(R.id.addcustomer_phonenum)
    EditText addcustomer_phonenum;
    @Bind(R.id.addcustomer_legalperson)
    EditText addcustomer_legalperson;
    @Bind(R.id.addcustomer_id_photo1)
    ImageView addcustomer_id_photo1;
    @Bind(R.id.addcustomer_id_photo2)
    ImageView addcustomer_id_photo2;
    @Bind(R.id.addcustomer_businesslicense)
    EditText addcustomer_businesslicense;
    @Bind(R.id.addcustomer_businesslicense_photo1)
    ImageView addcustomer_businesslicense_photo1;
    @Bind(R.id.addcustomer_businesslicense_photo2)
    ImageView addcustomer_businesslicense_photo2;
    @Bind(R.id.addcustomer_company)
    EditText addcustomer_company;
    @Bind(R.id.addcustomer_taxnum)
    EditText addcustomer_taxnum;
    @Bind(R.id.addcustomer_phonenum2)
    EditText addcustomer_phonenum2;
    @Bind(R.id.addcustomer_bank)
    EditText addcustomer_bank;
    @Bind(R.id.addcustomer_cardnum)
    EditText addcustomer_cardnum;
    @Bind(R.id.addcustomer_legalpersonid)
    EditText addcustomer_legalpersonid;
    @Bind(R.id.addcustomer_reg_address)
    EditText addcustomer_reg_address;
    @Bind(R.id.addcustomer_creditline)
    EditText addcustomer_creditline;
    @Bind(R.id.addcustomer_email)
    EditText addcustomer_email;
    @Bind(R.id.addcustomer_zipcode)
    EditText addcustomer_zipcode;
    @Bind(R.id.view_toolbar_center_text_back)
    TextView view_toolbar_center_text_back;
    @Bind(R.id.view_toolbar_center_text_next)
    TextView view_toolbar_center_text_next;

    String pic1="";
    String pic2="";
    String pic3="";
    String pic4="";
    int imagePos=1;
    String cityIds="";
    String cityNames="";

    @Override
    public int initContentView() {
        return R.layout.activity_addcustomer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("新增会员-基本信息");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        view_toolbar_center_text_next.setText("下一步");
        view_toolbar_center_text_next.setTextColor(Color.parseColor("#a20000"));
        view_toolbar_center_text_next.setVisibility(View.VISIBLE);
        view_toolbar_center_text_back.setTextColor(Color.parseColor("#a20000"));
    }

    @OnClick({R.id.addcustomer_commit, R.id.view_toolbar_center_back,
            R.id.addcustomer_id_photo1, R.id.addcustomer_id_photo2,
            R.id.addcustomer_businesslicense_photo1, R.id.addcustomer_businesslicense_photo2,
            R.id.addcustomer_area, R.id.view_toolbar_center_text_next, R.id.view_toolbar_center_text_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addcustomer_commit:
                addCustomer();
                break;
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.addcustomer_id_photo1:
                imagePos=1;
                if (!pic1.equals("")) {
                    File file=new File(pic1);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.addcustomer_id_photo2:
                imagePos=2;
                if (!pic2.equals("")) {
                    File file=new File(pic2);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.addcustomer_businesslicense_photo1:
                imagePos=3;
                if (!pic3.equals("")) {
                    File file=new File(pic3);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.addcustomer_businesslicense_photo2:
                imagePos=4;
                if (!pic4.equals("")) {
                    File file=new File(pic4);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                openChoiceImage();
                break;
            case R.id.addcustomer_area:
                Intent intent=new Intent(AddCustomerActivity.this, AreaActivity.class);
                startActivityForResult(intent, ParamUtils.RESULT_AREA);
                break;
            case R.id.view_toolbar_center_text_next:
                addcustomer_layout1.setVisibility(View.GONE);
                addcustomer_layout2.setVisibility(View.VISIBLE);
                view_toolbar_center_text_back.setText("上一步");
                view_toolbar_center_text_back.setVisibility(View.VISIBLE);
                view_toolbar_center_text_next.setVisibility(View.GONE);
                view_toolbar_center_title.setText("新增会员-开票信息");
                break;
            case R.id.view_toolbar_center_text_back:
                addcustomer_layout1.setVisibility(View.VISIBLE);
                addcustomer_layout2.setVisibility(View.GONE);
                view_toolbar_center_text_back.setVisibility(View.GONE);
                view_toolbar_center_text_next.setVisibility(View.VISIBLE);
                view_toolbar_center_title.setText("新增会员-基本信息");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (addcustomer_layout2.getVisibility() == View.VISIBLE && addcustomer_layout1.getVisibility() == View.GONE) {
            addcustomer_layout1.setVisibility(View.VISIBLE);
            addcustomer_layout2.setVisibility(View.GONE);
            view_toolbar_center_text_back.setVisibility(View.GONE);
            view_toolbar_center_text_next.setVisibility(View.VISIBLE);
            view_toolbar_center_title.setText("新增会员-基本信息");
        } else {
            super.onBackPressed();
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
            addcustomer_area.setText(cityNames);
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
                ImageLoader.getInstance().displayImage("file://"+filePath, addcustomer_id_photo1);
                pic1=filePath;
            }
            else if (imagePos==2) {
                ImageLoader.getInstance().displayImage("file://"+filePath, addcustomer_id_photo2);
                pic2=filePath;
            }
            else if (imagePos==3) {
                ImageLoader.getInstance().displayImage("file://"+filePath, addcustomer_businesslicense_photo1);
                pic3=filePath;
            }
            else if (imagePos==4) {
                ImageLoader.getInstance().displayImage("file://"+filePath, addcustomer_businesslicense_photo2);
                pic4=filePath;
            }
        }
    }

    private void addCustomer() {
        if (addcustomer_company.getText().toString().equals("")) {
            showToast("请填写单位全称");
            return;
        }
        if (addcustomer_phonenum.getText().toString().equals("")) {
            showToast("请填写联系电话");
            return;
        }
        if (addcustomer_phonenum.getText().toString().length()<11) {
            showToast("手机号码格式错误，请重新填写");
            return;
        }
        if (cityIds.equals("")) {
            showToast("请选择所在地区");
            return;
        }
        if (addcustomer_address.getText().toString().equals("")) {
            showToast("请填写详细地址");
            return;
        }
        if (addcustomer_businesslicense.getText().toString().equals("")) {
            showToast("请填写营业执照号");
            return;
        }
        if (addcustomer_legalperson.getText().toString().equals("")) {
            showToast("请填写法人/经营者姓名");
            return;
        }
        if (addcustomer_legalpersonid.getText().toString().equals("")) {
            showToast("请填写身份证号码");
            return;
        }
        if (addcustomer_name.getText().toString().equals("")) {
            showToast("请填写会员名");
            return;
        }
        if (addcustomer_contact.getText().toString().equals("")) {
            showToast("请填写联系人");
            return;
        }
        if (addcustomer_taxnum.getText().toString().equals("")) {
            showToast("请填写税务登记号");
            return;
        }
        if (addcustomer_bank.getText().toString().equals("")) {
            showToast("请填写开户银行");
            return;
        }
        if (addcustomer_cardnum.getText().toString().equals("")) {
            showToast("请填写开户银行账号");
            return;
        }
        if (addcustomer_phonenum2.getText().toString().equals("")) {
            showToast("请填写联系电话");
            return;
        }
        if (addcustomer_reg_address.getText().toString().equals("")) {
            showToast("请填写企业住所");
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
            showToast("请添加门头照片");
            return;
        }
        if (addcustomer_creditline.getText().toString().equals("")) {
            showToast("初始化授信额度不能为空");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.account.sysservice.xiuliuser.add", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("repairdepot_name", addcustomer_company.getText().toString());
        params.put("mobile", addcustomer_phonenum.getText().toString());
        params.put("area", cityIds);
        params.put("repairdepot_address", addcustomer_address.getText().toString());
        params.put("business_encoding", addcustomer_businesslicense.getText().toString());
        params.put("corporation", addcustomer_legalperson.getText().toString());
        params.put("corporation_codeId", addcustomer_legalpersonid.getText().toString());
        params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("account_name", addcustomer_name.getText().toString());
        params.put("contact_person", addcustomer_contact.getText().toString());
        params.put("revenues_code", addcustomer_taxnum.getText().toString());
        params.put("bank_name", addcustomer_bank.getText().toString());
        params.put("bank_account", addcustomer_cardnum.getText().toString());
        params.put("contact_phone", addcustomer_phonenum2.getText().toString());
        params.put("reg_address", addcustomer_reg_address.getText().toString());
        HashMap<String, File> fileHashMap=new HashMap<>();
        fileHashMap.put("business_photo", new File(pic1));
        fileHashMap.put("corporation_codeId_photo", new File(pic2));
        fileHashMap.put("shop_photo", new File(pic3));
        fileHashMap.put("shop_photo_new", new File(pic4));
        httpHelper.uploadFile(fileHashMap, ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在添加修理厂");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("AddCustomerActivity", string);
                dismissDialog();
                if (JsonParse.getResultValue(string)!=null) {
                    showToast(JsonParse.getResultValue(string));
                }
                else {
                    showToast("未知错误");
                }
                if (JsonParse.getResultCode(string)==0) {
                    finish();
                }
            }

            @Override
            public void onError() {
                dismissDialog();

                showToast(getResources().getString(R.string.network_error));
            }
        });
    }
}
