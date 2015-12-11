package com.renyu.carserver.activity.customercenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.CustomerModel;
import com.renyu.carserver.model.JsonParse;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/12.
 */
public class CustomerCenterPriceActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.customercenterprice_taxnum)
    EditText customercenterprice_taxnum;
    @Bind(R.id.customercenterprice_phonenum2)
    EditText customercenterprice_phonenum2;
    @Bind(R.id.customercenterprice_bank)
    EditText customercenterprice_bank;
    @Bind(R.id.customercenterprice_cardnum)
    EditText customercenterprice_cardnum;
    @Bind(R.id.customercenterprice_reg_address)
    EditText customercenterprice_reg_address;

    CustomerModel model=null;

    boolean isChange=false;

    @Override
    public int initContentView() {
        return R.layout.activity_customercenterprice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model= (CustomerModel) getIntent().getExtras().getSerializable("model");

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("会员开票资料");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        customercenterprice_taxnum.setText(model.getRevenues_code());
        customercenterprice_phonenum2.setText(model.getContact_phone());
        customercenterprice_bank.setText(model.getBank_name());
        customercenterprice_cardnum.setText(""+model.getBank_account());
        customercenterprice_reg_address.setText(""+model.getReg_address());
    }

    @OnClick({R.id.customercenterprice_commit, R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customercenterprice_commit:
                updateCustomer();
                break;
            case R.id.view_toolbar_center_back:
                finish();
        }
    }

    private void updateCustomer() {
        if (customercenterprice_taxnum.getText().toString().equals("")) {
            showToast("请填写税务登记号");
            return;
        }
        if (customercenterprice_bank.getText().toString().equals("")) {
            showToast("请填写开户银行");
            return;
        }
        if (customercenterprice_cardnum.getText().toString().equals("")) {
            showToast("请填写开户银行账号");
            return;
        }
        if (customercenterprice_phonenum2.getText().toString().equals("")) {
            showToast("请填写联系电话");
            return;
        }
        if (customercenterprice_reg_address.getText().toString().equals("")) {
            showToast("请填写企业住所");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.xiuliuser.update", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("revenues_code", customercenterprice_taxnum.getText().toString());
        params.put("bank_name", customercenterprice_bank.getText().toString());
        params.put("bank_account", customercenterprice_cardnum.getText().toString());
        params.put("contact_phone", customercenterprice_phonenum2.getText().toString());
        params.put("reg_address", customercenterprice_reg_address.getText().toString());
        params.put("user_id", ""+model.getUser_id());
        HashMap<String, File> fileHashMap=new HashMap<>();
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
