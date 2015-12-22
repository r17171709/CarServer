package com.renyu.carserver.activity.customercenter;

import android.graphics.Color;
import android.os.Bundle;
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
import com.renyu.carserver.model.CustomerModel;

import butterknife.Bind;
import butterknife.OnClick;

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
    TextView customercenterinfo_name;
    @Bind(R.id.customercenterinfo_contact)
    TextView customercenterinfo_contact;
    @Bind(R.id.customercenterinfo_company)
    TextView customercenterinfo_company;
    @Bind(R.id.customercenterinfo_area)
    TextView customercenterinfo_area;
    @Bind(R.id.customercenterinfo_address)
    TextView customercenterinfo_address;
    @Bind(R.id.customercenterinfo_phonenum)
    TextView customercenterinfo_phonenum;
    @Bind(R.id.customercenterinfo_legalperson)
    TextView customercenterinfo_legalperson;
    @Bind(R.id.customercenterinfo_businesslicense)
    TextView customercenterinfo_businesslicense;
    @Bind(R.id.customercenterinfo_idphoto_1)
    ImageView customercenterinfo_idphoto_1;
    @Bind(R.id.customercenterinfo_idphoto_2)
    ImageView customercenterinfo_idphoto_2;
    @Bind(R.id.customercenterinfo_businesslicense_photo)
    ImageView customercenterinfo_businesslicense_photo;
    @Bind(R.id.customercenterinfo_shop_photo)
    ImageView customercenterinfo_shop_photo;
    @Bind(R.id.customercenterinfo_legalpersonid)
    TextView customercenterinfo_legalpersonid;
    @Bind(R.id.customercenterinfo_creditline)
    TextView customercenterinfo_creditline;
    @Bind(R.id.customercenterinfo_email)
    TextView customercenterinfo_email;
    @Bind(R.id.customercenterinfo_zipcode)
    TextView customercenterinfo_zipcode;

    CustomerModel model=null;
    String cityIds="";
    String cityNames="";

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
        customercenterinfo_creditline.setText(model.getInit_amount());
        customercenterinfo_email.setText(model.getEmail());
        customercenterinfo_zipcode.setText(model.getPostcode());
        ImageLoader.getInstance().displayImage(model.getBusiness_photo(), customercenterinfo_idphoto_1, getAvatarDisplayImageOptions());
        ImageLoader.getInstance().displayImage(model.getCorporation_codeId_photo(), customercenterinfo_idphoto_2, getAvatarDisplayImageOptions());
        ImageLoader.getInstance().displayImage(model.getShop_photo(), customercenterinfo_businesslicense_photo, getAvatarDisplayImageOptions());
        ImageLoader.getInstance().displayImage(model.getShop_photo_new(), customercenterinfo_shop_photo, getAvatarDisplayImageOptions());
    }

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
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
}
