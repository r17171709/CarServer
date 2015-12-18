package com.renyu.carserver.activity.login;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.renyu.carserver.R;
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
public class JoinActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.join_join_preview)
    LinearLayout join_join_preview;
    @Bind(R.id.join_join_after)
    LinearLayout join_join_after;
    @Bind(R.id.join_layout)
    ScrollView join_layout;
    @Bind(R.id.join_area)
    TextView join_area;
    @Bind(R.id.join_name)
    EditText join_name;
    @Bind(R.id.join_address)
    EditText join_address;
    @Bind(R.id.join_businesslicense)
    EditText join_businesslicense;
    @Bind(R.id.join_contact)
    EditText join_contact;
    @Bind(R.id.join_phonenum)
    EditText join_phonenum;
    @Bind(R.id.legalperson)
    EditText legalperson;
    @Bind(R.id.idnumber)
    EditText idnumber;
    @Bind(R.id.join_businesslicense_upload)
    ImageView join_businesslicense_upload;

    String lastAvatar="";
    String cityIds="";
    String cityNames="";

    @Override
    public int initContentView() {
        return R.layout.activity_join;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        view_toolbar_center_title.setText("加盟申请");
        view_toolbar_center_back.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.join_commit, R.id.join_back, R.id.join_send, R.id.join_backlogin, R.id.join_area, R.id.join_businesslicense_upload, R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_commit:
                join_join_preview.setVisibility(View.GONE);
                break;
            case R.id.join_back:
                finish();
                break;
            case R.id.join_send:
                join();
                break;
            case R.id.join_backlogin:
                finish();
                break;
            case R.id.join_area:
                Intent intent=new Intent(JoinActivity.this, AreaActivity.class);
                startActivityForResult(intent, ParamUtils.RESULT_AREA);
                break;
            case R.id.join_businesslicense_upload:
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
                cityNames+=CommonUtils.getCityInfo(values[i]);
            }
            join_area.setText(cityNames);
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
            ImageLoader.getInstance().displayImage("file://"+filePath, join_businesslicense_upload);
            lastAvatar=filePath;
        }
    }

    private void join() {
        if (join_name.getText().toString().equals("")) {
            showToast("服务商名称不能为空");
            return;
        }
        if (cityIds.equals("")) {
            showToast("所在地区不能为空");
            return;
        }
        if (join_address.getText().toString().equals("")) {
            showToast("详细地址不能为空");
            return;
        }
        if (join_businesslicense.getText().toString().equals("")) {
            showToast("营业执照号不能为空");
            return;
        }
        if (join_contact.getText().toString().equals("")) {
            showToast("联系人不能为空");
            return;
        }
        if (join_phonenum.getText().toString().equals("")) {
            showToast("手机号码不能为空");
            return;
        }
        if (join_phonenum.getText().toString().length()<11) {
            showToast("手机号码必须为11位");
            return;
        }
        if (legalperson.getText().toString().equals("")) {
            showToast("法人姓名不能为空");
            return;
        }
        if (idnumber.getText().toString().equals("")) {
            showToast("法人身份证号不能为空");
            return;
        }
        if (idnumber.getText().toString().length()<18) {
            showToast("法人身份证号必须18位");
            return;
        }
        if (lastAvatar.equals("")) {
            showToast("请添加营业执照照片照片");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.apply", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("shop_name", join_name.getText().toString());
        params.put("area", cityIds);
        params.put("addr", join_address.getText().toString());
        params.put("business_encoding", join_businesslicense.getText().toString());
        params.put("contact_person", join_contact.getText().toString());
        params.put("mobile", join_phonenum.getText().toString());
        params.put("contact", legalperson.getText().toString());
        params.put("shopuser_identity", idnumber.getText().toString());
        params.put("areaframe", cityIds);
        HashMap<String, File> filesMap=new HashMap<>();
        filesMap.put("business_photo", new File(lastAvatar));
        httpHelper.asyncUpload(filesMap, ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在申请加盟");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                dismissDialog();

                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    join_join_after.setVisibility(View.VISIBLE);
                    join_layout.setVisibility(View.GONE);
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
