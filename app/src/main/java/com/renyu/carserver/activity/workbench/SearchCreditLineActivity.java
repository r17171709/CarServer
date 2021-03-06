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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.adapter.SearchCreditLineAdapter;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.SearchCreditLineModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/12.
 */
public class SearchCreditLineActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.searchcreateline_swipy)
    SwipyRefreshLayout searchcreateline_swipy;
    @Bind(R.id.searchcreateline_rv)
    RecyclerView searchcreateline_rv;
    SearchCreditLineAdapter adapter=null;
    @Bind(R.id.searchcreateline_edit)
    EditText searchcreateline_edit;

    ArrayList<SearchCreditLineModel> models=null;

    int page_no=1;

    @Override
    public int initContentView() {
        return R.layout.activity_searchcreditline;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("授信额度变更查询");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        searchcreateline_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        searchcreateline_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction==SwipyRefreshLayoutDirection.TOP) {
                    page_no=1;
                }
                else if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

                }
                getSearchCreditLine(searchcreateline_edit.getText().toString());
            }
        });
        searchcreateline_rv.setHasFixedSize(true);
        searchcreateline_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SearchCreditLineAdapter(this, models, new SearchCreditLineAdapter.ReCommitListener() {
            @Override
            public void commitPosition(int position) {
                reCommit(""+models.get(position).getUser_id(), models.get(position).getCurrent(), models.get(position).getApply(), position);
            }
        });
        searchcreateline_rv.setAdapter(adapter);
        searchcreateline_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (searchcreateline_edit.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    searchcreateline_swipy.setRefreshing(true);
                    page_no=1;
                    getSearchCreditLine(searchcreateline_edit.getText().toString());
                }
                return false;
            }
        });
        searchcreateline_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                page_no=1;
                getSearchCreditLine(s.toString());
            }
        });

        getSearchCreditLine(null);
    }

    public void getSearchCreditLine(String repairdepot_name) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.appamountChange", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("page_size", "20");
        params.put("page_no", ""+page_no);
        if (repairdepot_name!=null&&!repairdepot_name.equals("")) {
            params.put("repairdepot_name", repairdepot_name);
        }
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("SearchCreditLineActivit", string);

                searchcreateline_swipy.setRefreshing(false);

                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model=JsonParse.getSearchCreditLineModels(string);
                    //这里就是没有数据
                    if (model==null) {
                        if (page_no==1) {
                            models.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else if (model instanceof String) {
                        showToast((String) model);
                    }
                    else {
                        if (page_no==1) {
                            models.clear();
                        }
                        models.addAll((ArrayList<SearchCreditLineModel>) model);
                        adapter.notifyDataSetChanged();
                        page_no++;
                    }
                }
            }

            @Override
            public void onError() {
                searchcreateline_swipy.setRefreshing(false);
            }
        });
    }

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
        }
    }

    private void reCommit(String user_id, String current, String apply, final int position) {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.Resubmit", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(this).getShop_id());
        params.put("user_id", user_id);
        params.put("current", current);
        params.put("apply", apply);
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
                    models.get(position).setStatus("0");
                    adapter.notifyDataSetChanged();
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                showToast(getResources().getString(R.string.network_error));
            }
        });
    }
}
