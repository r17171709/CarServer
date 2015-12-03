package com.renyu.carserver.activity.workbench;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.adapter.ClientsReviewAdapter;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.ClientsReviewModel;
import com.renyu.carserver.model.JsonParse;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/12.
 */
public class ClientsReviewActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.clientsreview_examining)
    TextView clientsreview_examining;
    @Bind(R.id.clientsreview_result)
    TextView clientsreview_result;
    @Bind(R.id.clientsreview_swipy)
    SwipyRefreshLayout clientsreview_swipy;
    @Bind(R.id.clientsreview_rv)
    RecyclerView clientsreview_rv;
    ClientsReviewAdapter adapter_left=null;
    ClientsReviewAdapter adapter_right=null;
    @Bind(R.id.clientsreview_edit)
    EditText clientsreview_edit;

    ArrayList<ClientsReviewModel> models_left=null;
    ArrayList<ClientsReviewModel> models_right=null;

    int page_no_left=1;
    int page_no_right=1;
    boolean isLeft=true;

    //阻止edittext刷新
    boolean isNeedLoad=false;

    @Override
    public int initContentView() {
        return R.layout.activity_clientsreview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models_left=new ArrayList<>();
        models_right=new ArrayList<>();

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("新增客户审核进度");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        clientsreview_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        clientsreview_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    getStatusAudit(clientsreview_edit.getText().toString());
                } else if (direction == SwipyRefreshLayoutDirection.TOP) {
                    if (isLeft) {
                        page_no_left=1;
                    }
                    else {
                        page_no_right=1;
                    }
                    getStatusAudit(clientsreview_edit.getText().toString());
                }
            }
        });
        clientsreview_rv.setHasFixedSize(true);
        clientsreview_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter_left=new ClientsReviewAdapter(this, models_left);
        adapter_right=new ClientsReviewAdapter(this, models_right);
        clientsreview_examining.performClick();
        clientsreview_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (clientsreview_edit.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    clientsreview_swipy.setRefreshing(true);
                    if (isLeft) {
                        page_no_left=1;
                    }
                    else {
                        page_no_right=1;
                    }
                    getStatusAudit(clientsreview_edit.getText().toString());
                }
                return false;
            }
        });
        clientsreview_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isNeedLoad) {
                    isNeedLoad=false;
                    return;
                }
                if (isLeft) {
                    page_no_left=1;
                }
                else {
                    page_no_right=1;
                }
                getStatusAudit(s.toString());
            }
        });
    }

    @OnClick({R.id.clientsreview_examining, R.id.clientsreview_result, R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clientsreview_examining:
                hide(clientsreview_edit);
                isLeft=true;
                clientsreview_examining.setBackgroundColor(Color.RED);
                clientsreview_examining.setTextColor(Color.WHITE);
                clientsreview_result.setBackgroundColor(Color.WHITE);
                clientsreview_result.setTextColor(Color.BLACK);
                clientsreview_rv.setAdapter(adapter_left);
                isNeedLoad=true;
                clientsreview_edit.setText("");
                if (models_left.size()==0) {
                    getStatusAudit(null);
                }
                break;
            case R.id.clientsreview_result:
                hide(clientsreview_edit);
                isLeft=false;
                clientsreview_result.setBackgroundColor(Color.RED);
                clientsreview_result.setTextColor(Color.WHITE);
                clientsreview_examining.setBackgroundColor(Color.WHITE);
                clientsreview_examining.setTextColor(Color.BLACK);
                clientsreview_rv.setAdapter(adapter_right);
                isNeedLoad=true;
                clientsreview_edit.setText("");
                if (models_right.size()==0) {
                    getStatusAudit(null);
                }
                break;
            case R.id.view_toolbar_center_back:
                finish();
                break;
        }
    }

    private void getStatusAudit(String repairdepot_name) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.account.sysservice.statusAudit", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("page_size", "20");
        if (isLeft) {
            params.put("page_no", ""+page_no_left);
            params.put("appove_status", "1");
        }
        else {
            params.put("page_no", ""+page_no_right);
            params.put("appove_status", "2,3");
        }
        if (repairdepot_name!=null&&!repairdepot_name.equals("")) {
            params.put("repairdepot_name", repairdepot_name);
        }
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                clientsreview_swipy.setRefreshing(false);
                if (JsonParse.getResultInt(string) == 1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model= JsonParse.getClientsReviewModels(string);
                    if (model == null) {
                        showToast("未知错误");
                    } else if (model instanceof String) {
                        showToast((String) model);
                    } else {
                        if (isLeft) {
                            if (page_no_left==1) {
                                models_left.clear();
                                models_left.addAll((ArrayList<ClientsReviewModel>) model);
                                clientsreview_rv.setAdapter(adapter_left);
                            }
                            else {
                                models_left.addAll((ArrayList<ClientsReviewModel>) model);
                                adapter_left.notifyDataSetChanged();
                            }
                            page_no_left++;
                        }
                        else {
                            if (page_no_right==1) {
                                models_right.clear();
                                models_right.addAll((ArrayList<ClientsReviewModel>) model);
                                clientsreview_rv.setAdapter(adapter_right);
                            }
                            else {
                                models_right.addAll((ArrayList<ClientsReviewModel>) model);
                                adapter_right.notifyDataSetChanged();
                            }
                            page_no_right++;
                        }
                    }
                }
            }

            @Override
            public void onError() {
                clientsreview_swipy.setRefreshing(false);
                showToast("未知错误");
            }
        });
    }
}
