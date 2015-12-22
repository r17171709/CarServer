package com.renyu.carserver.fragment;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.activity.customercenter.CustomerCenterInfoActivity;
import com.renyu.carserver.activity.customercenter.CustomerCenterPriceActivity;
import com.renyu.carserver.activity.customercenter.CustomerCenterPriceListActivity;
import com.renyu.carserver.adapter.CustomerCenterAdapter;
import com.renyu.carserver.base.BaseFragment;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.CustomerModel;
import com.renyu.carserver.model.JsonParse;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by renyu on 15/10/20.
 */
public class CustomerCenterFragment extends BaseFragment {

    @Bind(R.id.customercenter_rv)
    RecyclerView customercenter_rv;
    @Bind(R.id.customercenter_swipy)
    SwipyRefreshLayout customercenter_swipy;
    CustomerCenterAdapter adapter=null;
    @Bind(R.id.customercenter_edittext)
    EditText customercenter_edittext;

    ArrayList<CustomerModel> models=null;

    int page_no=1;

    @Override
    public int initContentView() {
        return R.layout.fragment_customercenter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        models=new ArrayList<>();

        initViews();

    }

    private void initViews() {
        customercenter_rv.setHasFixedSize(true);
        customercenter_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new CustomerCenterAdapter(getActivity(), models, new CustomerCenterAdapter.OnJumpListener() {
            @Override
            public void jumpCustomerCenterInfo(int position) {
                Intent intent=new Intent(getActivity(), CustomerCenterInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("model", models.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, ParamUtils.customercenterinfo_result);
            }

            @Override
            public void jumpCustomerCenterPrice(int position) {
                Intent intent=new Intent(getActivity(), CustomerCenterPriceActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("model", models.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, ParamUtils.customercenterprice);
            }

            @Override
            public void jumpCustomerCenterPriceList(int position) {
                Intent intent=new Intent(getActivity(), CustomerCenterPriceListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("model", models.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, ParamUtils.customercenterpricelist);
            }
        });
        customercenter_rv.setAdapter(adapter);
        customercenter_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        customercenter_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

                }
                else if (direction==SwipyRefreshLayoutDirection.TOP) {
                    page_no=1;
                }
                getAllCustomerList(customercenter_edittext.getText().toString());
            }
        });
        customercenter_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (customercenter_edittext.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    customercenter_swipy.setRefreshing(true);
                    page_no=1;
                    getAllCustomerList(customercenter_edittext.getText().toString());
                }
                return false;
            }
        });
        customercenter_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                page_no=1;
                getAllCustomerList(s.toString());
            }
        });

        getAllCustomerList(null);
    }

    private void getAllCustomerList(String repairdepot_name) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.account.sysservice.xiuliuser.list", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(getActivity()).getShop_id());
        params.put("page_no", ""+page_no);
        params.put("page_size", "20");
        params.put("appove_status", "2");
        if (repairdepot_name!=null&&!repairdepot_name.equals("")) {
            params.put("repairdepot_name", repairdepot_name);
        }
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {

            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("CustomerCenterFragment", string);
                customercenter_swipy.setRefreshing(false);

                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model=JsonParse.getCustomerModel(string);
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
                        models.addAll((ArrayList<CustomerModel>) model);
                        adapter.notifyDataSetChanged();
                        page_no++;
                    }
                }
            }

            @Override
            public void onError() {
                customercenter_swipy.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            customercenter_swipy.setRefreshing(true);
            page_no=1;
            getAllCustomerList(customercenter_edittext.getText().toString());
        }
    }
}
