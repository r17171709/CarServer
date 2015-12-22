package com.renyu.carserver.activity.customercenter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.model.CustomerModel;

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
    TextView customercenterprice_taxnum;
    @Bind(R.id.customercenterprice_phonenum2)
    TextView customercenterprice_phonenum2;
    @Bind(R.id.customercenterprice_bank)
    TextView customercenterprice_bank;
    @Bind(R.id.customercenterprice_cardnum)
    TextView customercenterprice_cardnum;
    @Bind(R.id.customercenterprice_reg_address)
    TextView customercenterprice_reg_address;

    CustomerModel model=null;

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

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
        }
    }
}
