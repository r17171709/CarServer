package com.renyu.carserver.activity.workbench;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.adapter.CreditLineAdapter;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.CreditLineModel;
import com.renyu.carserver.model.JsonParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by renyu on 15/11/12.
 */
public class CreditLineActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.createline_swipy)
    SwipyRefreshLayout createline_swipy;
    @Bind(R.id.createline_rv)
    RecyclerView createline_rv;
    CreditLineAdapter adapter=null;
    @Bind(R.id.creditline_change_layout)
    RelativeLayout creditline_change_layout;
    @Bind(R.id.creditline_num_layout)
    LinearLayout creditline_num_layout;
    @Bind(R.id.creditline_result_layout)
    LinearLayout creditline_result_layout;
    @Bind(R.id.creditline_platform_num)
    EditText creditline_platform_num;
    @Bind(R.id.creditline_name)
    TextView creditline_name;
    @Bind(R.id.createline_edit)
    EditText createline_edit;
    @Bind(R.id.creditline_result_text)
    TextView creditline_result_text;

    ArrayList<CreditLineModel> models=null;

    int page_no=1;

    int currentPosition=-1;

    @Override
    public int initContentView() {
        return R.layout.activity_creditline;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();

        initViews();
    }

    private void initViews() {
        view_toolbar_center_title.setText("授信额度");
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        createline_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (createline_edit.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    createline_swipy.setRefreshing(true);
                    page_no=1;
                    getCreditLine(createline_edit.getText().toString());
                }
                return false;
            }
        });
        createline_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                page_no=1;
                getCreditLine(s.toString());
            }
        });
        createline_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        createline_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction==SwipyRefreshLayoutDirection.TOP) {
                    page_no=1;
                }
                else if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

                }
                getCreditLine(createline_edit.getText().toString());
            }
        });
        createline_rv.setHasFixedSize(true);
        createline_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CreditLineAdapter(this, models, new CreditLineAdapter.ChangeCreditLineListener() {
            @Override
            public void change(int position) {
                creditline_change_layout.setVisibility(View.VISIBLE);
                creditline_num_layout.setVisibility(View.VISIBLE);

                creditline_name.setText(models.get(position).getRepairdepot_name());
                creditline_platform_num.setText(""+models.get(position).getInit_amount());
                currentPosition=position;
            }
        });
        createline_rv.setAdapter(adapter);

        getCreditLine(null);
    }

    @OnClick({R.id.view_toolbar_center_back, R.id.creditline_commit, R.id.creditline_change_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.creditline_commit:
                update(""+models.get(currentPosition).getUser_id(), ""+models.get(currentPosition).getInit_amount(), creditline_platform_num.getText().toString());
                break;
            case R.id.creditline_change_layout:
                creditline_change_layout.setVisibility(View.GONE);
                creditline_num_layout.setVisibility(View.GONE);
                break;
        }
    }

    private void getCreditLine(String repairdepot_name) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.appamountlist", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("page_no", ""+page_no);
        params.put("page_size", "20");
        if (repairdepot_name!=null&&!repairdepot_name.equals("")) {
            params.put("repairdepot_name", repairdepot_name);
        }
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                createline_swipy.setRefreshing(false);
                Log.d("CreditLineActivity", string);

                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model=JsonParse.getCreditLineModel(string);
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
                        models.addAll((ArrayList<CreditLineModel>) model);
                        adapter.notifyDataSetChanged();
                        page_no++;
                    }
                }
            }

            @Override
            public void onError() {
                createline_swipy.setRefreshing(false);
            }
        });
    }

    public void update(String user_id, String current, String apply) {
        if (apply.equals("")) {
            showToast("请填写变更后的授信额度数值");
            return;
        }
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.appamountmodify", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("user_id", user_id);
        params.put("serviceid", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("current", current);
        params.put("apply", apply);
        params.put("applytime", ""+(System.currentTimeMillis()/1000));
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在变更授信额度");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                dismissDialog();

                creditline_num_layout.setVisibility(View.GONE);
                creditline_result_text.setText(JsonParse.getResultValue(string));
                creditline_result_layout.setVisibility(View.VISIBLE);
                Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        creditline_result_layout.setVisibility(View.GONE);
                        creditline_change_layout.setVisibility(View.GONE);

                        createline_swipy.setRefreshing(true);
                        getCreditLine(createline_edit.getText().toString());
                    }
                });
            }

            @Override
            public void onError() {
                dismissDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (creditline_change_layout.getVisibility()==View.VISIBLE) {
            creditline_change_layout.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}
