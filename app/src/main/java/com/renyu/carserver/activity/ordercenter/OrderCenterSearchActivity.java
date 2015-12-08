package com.renyu.carserver.activity.ordercenter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.ParentOrderModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/14.
 */
public class OrderCenterSearchActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.senior_custom)
    EditText senior_custom;
    @Bind(R.id.senior_product)
    EditText senior_product;
    @Bind(R.id.senior_productid)
    EditText senior_productid;
    @Bind(R.id.senior_time1)
    TextView senior_time1;
    @Bind(R.id.senior_time2)
    TextView senior_time2;
    @Bind(R.id.senior_price1)
    EditText senior_price1;
    @Bind(R.id.senior_price2)
    EditText senior_price2;

    @Override
    public int initContentView() {
        return R.layout.activity_ordercenter_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("筛选");
        view_toolbar_center_title.setTextColor(Color.parseColor("#a20000"));
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);

        senior_time1.setTag("0");
        senior_time2.setTag("0");
    }

    @OnClick({R.id.view_toolbar_center_back, R.id.senior_reset, R.id.senior_commit, R.id.senior_time1, R.id.senior_time2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.senior_reset:
                senior_custom.setText("");
                senior_product.setText("");
                senior_productid.setText("");
                senior_time1.setText("");
                senior_time2.setText("");
                senior_price1.setText("");
                senior_price2.setText("");
                break;
            case R.id.senior_commit:
                if ((senior_time1.getText().toString().equals("") && !senior_time2.getText().toString().equals("")) ||
                        (senior_time2.getText().toString().equals("") && !senior_time1.getText().toString().equals("")) ) {
                    showToast("请完整输入下单时间");
                    return;
                }
                if (Integer.parseInt(senior_time1.getTag().toString())>Integer.parseInt(senior_time2.getTag().toString())) {
                    showToast("请正确输入下单时间");
                    return;
                }
                if ((senior_price1.getText().toString().equals("") && !senior_price2.getText().toString().equals("")) ||
                        (senior_price1.getText().toString().equals("") && !senior_price2.getText().toString().equals("")) ) {
                    showToast("请完整输入价格区间");
                    return;
                }
                if (!senior_price1.getText().toString().equals("")&&
                        !senior_price2.getText().toString().equals("")&&
                        Integer.parseInt(senior_price1.getText().toString())>Integer.parseInt(senior_price2.getText().toString())) {
                    showToast("请正确输入价格区间");
                    return;
                }
                search();
                break;
            case R.id.senior_time1:
                Calendar calendarTime1=Calendar.getInstance();
                new DatePickerDialog(OrderCenterSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        // TODO Auto-generated method stub
                        String time=year+"-"+((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+"-"+((day < 10) ? "0" + day : day);
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        senior_time1.setText(time);
                        try {
                            Date date=format.parse(time);
                            senior_time1.setTag(date.getTime()/1000);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, calendarTime1.get(Calendar.YEAR), calendarTime1.get(Calendar.MONTH), calendarTime1.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.senior_time2:
                Calendar calendarTime2=Calendar.getInstance();
                new DatePickerDialog(OrderCenterSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        // TODO Auto-generated method stub
                        String time=year+"-"+((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+"-"+((day < 10) ? "0" + day : day);
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        senior_time2.setText(time);
                        try {
                            Date date=format.parse(time);
                            senior_time2.setTag(date.getTime()/1000);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, calendarTime2.get(Calendar.YEAR), calendarTime2.get(Calendar.MONTH), calendarTime2.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void search() {
        Intent intent=new Intent(OrderCenterSearchActivity.this, OrderCenterSearchResultActivity.class);
        Bundle bundle=new Bundle();
        if (!senior_custom.getText().toString().equals("")) {
            bundle.putString("senior_custom", senior_custom.getText().toString());
        }
        if (!senior_product.getText().toString().equals("")) {
            bundle.putString("senior_product", senior_product.getText().toString());
        }
        if (!senior_productid.getText().toString().equals("")) {
            bundle.putString("senior_productid", senior_productid.getText().toString());
        }
        if (!senior_time1.getText().toString().equals("")&&!senior_time2.getText().toString().equals("")) {
            bundle.putString("senior_time1", senior_time1.getTag().toString());
            bundle.putString("senior_time2", senior_time2.getTag().toString());
        }
        if (!senior_price1.getText().toString().equals("")&&!senior_price2.getText().toString().equals("")) {
            bundle.putString("senior_price1", senior_price1.getText().toString());
            bundle.putString("senior_price2", senior_price2.getText().toString());
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
