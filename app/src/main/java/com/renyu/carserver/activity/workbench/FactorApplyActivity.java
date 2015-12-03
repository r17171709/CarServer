package com.renyu.carserver.activity.workbench;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.adapter.FactorApplyAdapter;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.FactoryApplyModel;
import com.renyu.carserver.model.JsonParse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/12.
 */
public class FactorApplyActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.factorapply_swipy)
    SwipyRefreshLayout factorapply_swipy;
    @Bind(R.id.factorapply_rv)
    RecyclerView factorapply_rv;
    FactorApplyAdapter adapter=null;
    @Bind(R.id.factorapply_edit)
    EditText factorapply_edit;

    ArrayList<FactoryApplyModel> models=null;

    int page_no=1;

    int applytime_=0;

    @Override
    public int initContentView() {
        return R.layout.activity_factorapply;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("修理厂申请");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        factorapply_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        factorapply_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction==SwipyRefreshLayoutDirection.TOP) {
                    page_no=1;
                }
                else if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

                }
                getAllFactoryApply(factorapply_edit.getText().toString(), applytime_);
            }
        });
        factorapply_rv.setHasFixedSize(true);
        factorapply_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new FactorApplyAdapter(this, models);
        factorapply_rv.setAdapter(adapter);
        factorapply_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (factorapply_edit.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    factorapply_swipy.setRefreshing(true);
                    page_no=1;
                    applytime_=0;
                    getAllFactoryApply(factorapply_edit.getText().toString(), 0);
                }
                return false;
            }
        });
        factorapply_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                page_no=1;
                applytime_=0;
                getAllFactoryApply(s.toString(), 0);
            }
        });

        getAllFactoryApply(null, 0);
    }

    @OnClick({R.id.view_toolbar_center_back, R.id.factorapply_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.factorapply_time:
                Calendar calendar=Calendar.getInstance();
                new DatePickerDialog(FactorApplyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        // TODO Auto-generated method stub
                        page_no=1;
                        String time=year+"-"+((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+"-"+((day < 10) ? "0" + day : day);
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date=format.parse(time);
                            getAllFactoryApply(factorapply_edit.getText().toString(), (int) date.getTime());
                            applytime_=(int) date.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    public void getAllFactoryApply(String repairdepot_name, long applytime) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.joinapplylist", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("page_no", ""+page_no);
        params.put("page_size", "20");
        params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        if (repairdepot_name!=null&&!repairdepot_name.equals("")) {
            params.put("repairdepot_name", repairdepot_name);
        }
        if (applytime!=0) {
            params.put("applytime", ""+applytime);
        }
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                factorapply_swipy.setRefreshing(false);
                Log.d("FactorApplyActivity", string);

                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model=JsonParse.getFactoryApplyModel(string);
                    if (model==null) {
                        showToast("未知错误");
                    }
                    else if (model instanceof String) {
                        showToast((String) model);
                    }
                    else {
                        if (page_no==1) {
                            models.clear();
                        }
                        models.addAll((ArrayList<FactoryApplyModel>) model);
                        adapter.notifyDataSetChanged();
                        page_no++;
                    }
                }
            }

            @Override
            public void onError() {
                factorapply_swipy.setRefreshing(false);
            }
        });
    }
}
