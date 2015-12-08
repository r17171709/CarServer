package com.renyu.carserver.activity.ordercenter;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.renyu.carserver.R;
import com.renyu.carserver.adapter.OrderCenterAdapter;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.ParentOrderModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/12/8.
 */
public class OrderCenterSearchResultActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.ordercenter_change_layout)
    RelativeLayout ordercenter_change_layout;
    @Bind(R.id.ordercenter_change_commit)
    Button ordercenter_change_commit;
    @Bind(R.id.ordercenter_change_cancel)
    Button ordercenter_change_cancel;
    @Bind(R.id.ordercenter_swipy)
    SwipyRefreshLayout ordercenter_swipy;
    @Bind(R.id.ordercenter_lv)
    ListView ordercenter_lv;
    OrderCenterAdapter adapter=null;
    @Bind(R.id.ordercenter_layout)
    LinearLayout ordercenter_layout;

    ArrayList<ParentOrderModel> shopModels=null;

    String currentTag="";

    ArrayList<View> views=null;

    ArrayList<TextView> numTextViews=null;

    @Override
    public int initContentView() {
        return R.layout.fragment_ordercenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        views=new ArrayList<>();
        shopModels=new ArrayList<>();
        numTextViews=new ArrayList<>();

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("删选结果");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);

        ordercenter_layout.setVisibility(View.GONE);
        adapter=new OrderCenterAdapter(this, shopModels, new OrderCenterAdapter.TobepaidChangePriceListener() {
            @Override
            public void getTag(String tag) {
                if (currentTag.equals("")) {
                    currentTag=tag;
                    return;
                }
                if (currentTag.equals(tag)) {
                    currentTag=tag;
                    return;
                }
                TextView ordercentertobepaid_child_finalprice= (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_finalprice_"+currentTag);
                EditText ordercentertobepaid_child_finalprice_edit= (EditText) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_finalprice_edit_"+currentTag);
                ordercentertobepaid_child_finalprice_edit.setText("0");
                TextView ordercentertobepaid_child_changeprice= (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_changeprice_" + currentTag);
                TextView ordercentertobepaid_child_commit= (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commit_" + currentTag);
                TextView ordercentertobepaid_child_commitsync= (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commitsync_" + currentTag);
                ordercentertobepaid_child_finalprice.setVisibility(View.VISIBLE);
                ordercentertobepaid_child_finalprice_edit.setVisibility(View.GONE);
                ordercentertobepaid_child_changeprice.setVisibility(View.VISIBLE);
                ordercentertobepaid_child_commit.setVisibility(View.GONE);
                ordercentertobepaid_child_commitsync.setVisibility(View.GONE);
                currentTag=tag;
            }

            @Override
            public void showChange(String oid, String price, String tid, int position, int i) {
                priceRemind(oid, price, tid, position, i);
            }

            @Override
            public void cancel(int position) {
                cancelTrade(position);
            }
        });
        ordercenter_lv.setAdapter(adapter);
        ordercenter_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    if (!currentTag.equals("")) {
                        TextView ordercentertobepaid_child_finalprice = (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_finalprice_" + currentTag);
                        EditText ordercentertobepaid_child_finalprice_edit = (EditText) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_finalprice_edit_" + currentTag);
                        ordercentertobepaid_child_finalprice_edit.setText("0");
                        TextView ordercentertobepaid_child_changeprice = (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_changeprice_" + currentTag);
                        TextView ordercentertobepaid_child_commit = (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commit_" + currentTag);
                        TextView ordercentertobepaid_child_commitsync = (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commitsync_" + currentTag);
                        ordercentertobepaid_child_finalprice.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_finalprice_edit.setVisibility(View.GONE);
                        ordercentertobepaid_child_changeprice.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_commit.setVisibility(View.GONE);
                        ordercentertobepaid_child_commitsync.setVisibility(View.GONE);
                        currentTag = "";
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        ordercenter_swipy.setEnabled(false);
        loadOrderCenter();
    }

    private void cancelTrade(int position) {
        HashMap<String, String> params= ParamUtils.getSignParams("app.user.order.cancel", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("tid", shopModels.get(position).getTid());
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
                    if (JsonParse.getResultInt(string)==0) {
                        loadOrderCenter();
                    }
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();
                showToast("未知错误");
            }
        });
    }

    private void loadOrderCenter() {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.user.order.advancelist", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        if (getIntent().getExtras().getString("senior_custom")!=null) {
            params.put("repairdepot_name", getIntent().getExtras().getString("senior_custom"));
        }
        if (getIntent().getExtras().getString("senior_product")!=null) {
            params.put("title", getIntent().getExtras().getString("senior_product"));
        }
        if (getIntent().getExtras().getString("senior_productid")!=null) {
            params.put("tid", getIntent().getExtras().getString("senior_productid"));
        }
        if (getIntent().getExtras().getString("senior_time1")!=null && getIntent().getExtras().getString("senior_time2")!=null) {
            params.put("created_time_start", getIntent().getExtras().getString("senior_time1"));
            params.put("created_time_end", getIntent().getExtras().getString("senior_time2"));
        }
        if (getIntent().getExtras().getString("senior_price1")!=null && getIntent().getExtras().getString("senior_price2")!=null) {
            params.put("fee_start", getIntent().getExtras().getString("senior_price1"));
            params.put("fee_end", getIntent().getExtras().getString("senior_price2"));
        }
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在搜索");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                dismissDialog();

                if (JsonParse.getResultInt(string) == 1) {
                    showToast(JsonParse.getErrorValue(string));
                } else {
                    Object model = JsonParse.getOrderModel(string);
                    if (model==null) {
                        showToast("未知错误");
                    } else if (model instanceof String) {
                        showToast((String) model);
                    } else {
                        shopModels.clear();
                        shopModels.addAll((ArrayList<ParentOrderModel>) model);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError() {
                dismissDialog();
            }
        });
    }

    @OnClick({R.id.ordercenter_change_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ordercenter_change_cancel:
                ordercenter_change_layout.setVisibility(View.GONE);
                break;
        }
    }

    private void priceRemind(final String oid, final String price, final String tid, final int position, final int i) {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.priceRemind", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("oid", oid);
        params.put("price", price);
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在提交");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("OrderCenterFragment", string);
                dismissDialog();
                if (JsonParse.getPriceRemind(string)==0) {
                    price(oid, price, tid, position, i);
                }
                else if (JsonParse.getPriceRemind(string)==1) {
                    ordercenter_change_layout.setVisibility(View.VISIBLE);
                    ordercenter_change_commit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ordercenter_change_layout.setVisibility(View.GONE);
                            price(oid, price, tid, position, i);
                        }
                    });
                    ordercenter_change_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ordercenter_change_layout.setVisibility(View.GONE);
                        }
                    });
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();

                showToast("未知错误");
            }
        });
    }

    private void price(String oid, final String price, String tid, final int position, final int i) {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.price", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("oid", oid);
        params.put("tid", tid);
        params.put("price", price);
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                showDialog("提示", "正在提交");
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("OrderCenterFragment", string);
                dismissDialog();
                if (JsonParse.getResultValue(string)!=null) {
                    showToast(JsonParse.getResultValue(string));
                    if (JsonParse.getResultInt(string)==0) {
                        shopModels.get(position).getModels().get(i).setSettle_price(price);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();

                showToast("未知错误");
            }
        });
    }

}
