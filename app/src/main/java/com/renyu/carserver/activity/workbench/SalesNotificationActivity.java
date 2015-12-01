package com.renyu.carserver.activity.workbench;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.adapter.SalesNotificationAdapter;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.SalesNotificationModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/12.
 */
public class SalesNotificationActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.salesnotification_swipy)
    SwipyRefreshLayout salesnotification_swipy;
    @Bind(R.id.salesnotification_rv)
    RecyclerView salesnotification_rv;
    SalesNotificationAdapter adapter=null;

    ArrayList<SalesNotificationModel> models=null;
    int page_no=1;

    @Override
    public int initContentView() {
        return R.layout.activity_salesnotification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("售后通知");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);

        salesnotification_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        salesnotification_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    getNotification();
                } else if (direction == SwipyRefreshLayoutDirection.TOP) {
                    page_no=1;
                    getNotification();
                }
            }
        });
        salesnotification_rv.setHasFixedSize(true);
        salesnotification_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SalesNotificationAdapter(this, models);
        salesnotification_rv.setAdapter(adapter);

        getNotification();
    }

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
        }
    }

    private void getNotification() {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.aftersaleMessagelist", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("serviceid", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("page_size", "20");
        params.put("page_no", ""+page_no);
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("SalesNotificationActivi", string);

                salesnotification_swipy.setRefreshing(false);
                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model=JsonParse.getSalesNotificationModel(string);
                    if (model==null) {
                        showToast("未知错误");
                    }
                    else if (model instanceof String) {
                        showToast((String) model);
                    }
                    else {
                        if (page_no==1) {
                            models.clear();
                            models.addAll((ArrayList<SalesNotificationModel>) model);
                            salesnotification_rv.setAdapter(adapter);
                        }
                        else {
                            models.addAll((ArrayList<SalesNotificationModel>) model);
                            adapter.notifyDataSetChanged();
                        }
                        page_no++;
                    }
                }
            }

            @Override
            public void onError() {
                salesnotification_swipy.setRefreshing(false);
            }
        });
    }
}
