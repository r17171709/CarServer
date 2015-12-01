package com.renyu.carserver.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
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
        adapter=new CustomerCenterAdapter(getActivity(), models);
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
                getAllCustomerList();
            }
        });

        getAllCustomerList();
    }

    private void getAllCustomerList() {
        HashMap<String, String> params= ParamUtils.getSignParams("app.account.sysservice.xiuliuser.list", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(getActivity()).getShop_id());
        params.put("page_no", ""+page_no);
        params.put("page_size", "20");
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

}
