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
    FactorApplyAdapter adapter_left=null;
    FactorApplyAdapter adapter_right=null;
    @Bind(R.id.factorapply_edit)
    EditText factorapply_edit;
    @Bind(R.id.factorapply_examining)
    TextView factorapply_examining;
    @Bind(R.id.factorapply_result)
    TextView factorapply_result;

    ArrayList<FactoryApplyModel> models_left=null;
    ArrayList<FactoryApplyModel> models_right=null;

    int page_no_left=1;
    int page_no_right=1;
    boolean isLeft=true;

    int applytime_=0;

    //阻止edittext刷新
    boolean isNeedLoad=false;

    @Override
    public int initContentView() {
        return R.layout.activity_factorapply;
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
        view_toolbar_center_title.setText("会员申请");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        factorapply_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        factorapply_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction==SwipyRefreshLayoutDirection.TOP) {
                   if (isLeft) {
                       page_no_left=1;
                   }
                    else {
                       page_no_right=1;
                   }
                }
                else if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

                }
                if (isLeft) {
                    getAllFactoryApply(factorapply_edit.getText().toString(), applytime_, 2);
                }
                else {
                    getAllFactoryApply(factorapply_edit.getText().toString(), applytime_, 3);
                }
            }
        });
        factorapply_rv.setHasFixedSize(true);
        factorapply_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter_left=new FactorApplyAdapter(this, models_left, new FactorApplyAdapter.OnReCheckStateListener() {
            @Override
            public void recheck(int position) {
                recheckState(models_left.get(position).getUser_id(), position);
            }
        });
        adapter_right=new FactorApplyAdapter(this, models_right, null);
        factorapply_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (factorapply_edit.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    applytime_=0;
                    factorapply_swipy.setRefreshing(true);
                    if (isLeft) {
                        page_no_left=1;
                        getAllFactoryApply(factorapply_edit.getText().toString(), applytime_, 2);
                    }
                    else {
                        page_no_right=1;
                        getAllFactoryApply(factorapply_edit.getText().toString(), applytime_, 3);
                    }
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
                if (isNeedLoad) {
                    isNeedLoad=false;
                    return;
                }
                applytime_=0;
                if (isLeft) {
                    page_no_left=1;
                    getAllFactoryApply(factorapply_edit.getText().toString(), applytime_, 2);
                }
                else {
                    page_no_right=1;
                    getAllFactoryApply(factorapply_edit.getText().toString(), applytime_, 3);
                }
            }
        });
        factorapply_examining.performClick();
    }

    @OnClick({R.id.view_toolbar_center_back, R.id.factorapply_time, R.id.factorapply_examining, R.id.factorapply_result})
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
                        if (isLeft) {
                            page_no_left=1;
                        }
                        else {
                            page_no_right=1;
                        }
                        String time=year+"-"+((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+"-"+((day < 10) ? "0" + day : day);
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date=format.parse(time);
                            if (isLeft) {
                                getAllFactoryApply(factorapply_edit.getText().toString(), (int) date.getTime(), 2);
                            }
                            else {
                                getAllFactoryApply(factorapply_edit.getText().toString(), (int) date.getTime(), 3);
                            }
                            applytime_=(int) date.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.factorapply_examining:
                hide(factorapply_edit);
                isLeft=true;
                factorapply_examining.setBackgroundColor(Color.RED);
                factorapply_examining.setTextColor(Color.WHITE);
                factorapply_result.setBackgroundColor(Color.WHITE);
                factorapply_result.setTextColor(Color.BLACK);
                factorapply_rv.setAdapter(adapter_left);
                isNeedLoad=true;
                factorapply_edit.setText("");
                if (models_left.size()==0) {
                    getAllFactoryApply(null, 0, 2);
                }
                break;
            case R.id.factorapply_result:
                hide(factorapply_edit);
                isLeft=false;
                factorapply_result.setBackgroundColor(Color.RED);
                factorapply_result.setTextColor(Color.WHITE);
                factorapply_examining.setBackgroundColor(Color.WHITE);
                factorapply_examining.setTextColor(Color.BLACK);
                factorapply_rv.setAdapter(adapter_right);
                isNeedLoad=true;
                factorapply_edit.setText("");
                if (models_right.size()==0) {
                    getAllFactoryApply(null, 0, 3);
                }
                break;
        }
    }

    public void getAllFactoryApply(String repairdepot_name, long applytime, int state) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.joinapplylist", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        if (isLeft) {
            params.put("page_no", ""+page_no_left);
        }
        else {
            params.put("page_no", ""+page_no_right);
        }
        params.put("page_size", "20");
        if (repairdepot_name!=null&&!repairdepot_name.equals("")) {
            params.put("repair_name", repairdepot_name);
        }
        else {
            params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        }
        if (applytime!=0) {
            params.put("applytime", ""+applytime);
        }
        params.put("state", ""+state);
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
                    //这里就是没有数据
                    if (model==null) {
                        if (isLeft) {
                            if (page_no_left==1) {
                                models_left.clear();
                                adapter_left.notifyDataSetChanged();
                            }
                        }
                        else {
                            if (page_no_right==1) {
                                models_right.clear();
                                adapter_right.notifyDataSetChanged();
                            }
                        }
                    }
                    else if (model instanceof String) {
                        showToast((String) model);
                    }
                    else {
                        if (isLeft) {
                            if (page_no_left==1) {
                                models_left.clear();
                                models_left.addAll((ArrayList<FactoryApplyModel>) model);
                                factorapply_rv.setAdapter(adapter_left);
                            }
                            else {
                                models_left.addAll((ArrayList<FactoryApplyModel>) model);
                                adapter_left.notifyDataSetChanged();
                            }
                            page_no_left++;
                        }
                        else {
                            if (page_no_right==1) {
                                models_right.clear();
                                models_right.addAll((ArrayList<FactoryApplyModel>) model);
                                factorapply_rv.setAdapter(adapter_right);
                            }
                            else {
                                models_right.addAll((ArrayList<FactoryApplyModel>) model);
                                adapter_right.notifyDataSetChanged();
                            }
                            page_no_right++;
                        }
                    }
                }
            }

            @Override
            public void onError() {
                factorapply_swipy.setRefreshing(false);
            }
        });
    }

    private void recheckState(String user_id, final int position) {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.RepairDepotApply", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("user_id", user_id);
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在提交");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                dismissDialog();
                if (JsonParse.getResultValue(string)!=null) {
                    showToast(JsonParse.getResultValue(string));
                    if (JsonParse.getResultCode(string)==0) {
                        factorapply_swipy.setRefreshing(true);
                        page_no_left=1;
                        getAllFactoryApply(factorapply_edit.getText().toString(), applytime_, 2);

                        models_right.clear();
                        adapter_right.notifyDataSetChanged();
                    }
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();
            }
        });
    }
}
