package com.renyu.carserver.activity.workbench;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.adapter.MessageChoiceAdapter;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.CustomerModel;
import com.renyu.carserver.model.JsonParse;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by renyu on 16/3/28.
 */
public class MessageChoiceActivity extends BaseActivity {

    @Bind(R.id.messagesend_swipy)
    SwipyRefreshLayout messagesend_swipy;
    @Bind(R.id.salesnotification_rv)
    RecyclerView salesnotification_rv;
    MessageChoiceAdapter adapter;
    @Bind(R.id.messagesend__edittext)
    EditText messagesend__edittext;

    int page_no=1;

    ArrayList<CustomerModel> models=null;

    @Override
    public int initContentView() {
        return R.layout.activity_messagechoice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();

        initViews();

        getAllCustomerList(null);
    }

    private void initViews() {
        adapter=new MessageChoiceAdapter(this, models);
        salesnotification_rv.setHasFixedSize(true);
        salesnotification_rv.setLayoutManager(new LinearLayoutManager(this));
        salesnotification_rv.setAdapter(adapter);
        messagesend_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction==SwipyRefreshLayoutDirection.TOP) {
                    page_no=1;
                }
                else if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

                }
                getAllCustomerList(messagesend__edittext.getText().toString());
            }
        });
        messagesend__edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (messagesend__edittext.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    messagesend_swipy.setRefreshing(true);
                    page_no=1;
                    getAllCustomerList(messagesend__edittext.getText().toString());
                }
                return false;
            }
        });
        messagesend__edittext.addTextChangedListener(new TextWatcher() {
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
    }

    private void getAllCustomerList(String repairdepot_name) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.account.sysservice.xiuliuser.list", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(MessageChoiceActivity.this).getShop_id());
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
                messagesend_swipy.setRefreshing(false);

                if (JsonParse.getResultInt(string)==1) {
                    showToast(JsonParse.getErrorValue(string));
                }
                else {
                    Object model=JsonParse.getCustomerModel(string);
                    //这里就是没有数据
                    if (model==null) {
                        if (page_no==1) {
                            models.clear();
                            CustomerModel temp=new CustomerModel();
                            temp.setUser_id(-1);
                            temp.setRepairdepot_name("我的平台");
                            models.add(temp);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else if (model instanceof String) {
                        showToast((String) model);
                    }
                    else {
                        if (page_no==1) {
                            models.clear();
                            CustomerModel temp=new CustomerModel();
                            temp.setUser_id(-1);
                            temp.setRepairdepot_name("我的平台");
                            models.add(temp);
                        }
                        models.addAll((ArrayList<CustomerModel>) model);
                        adapter.notifyDataSetChanged();
                        page_no++;
                    }
                }
            }

            @Override
            public void onError() {
                messagesend_swipy.setRefreshing(false);
            }
        });
    }

    public void getChoice(int id, String name) {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
