package com.renyu.carserver.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.carserver.R;
import com.renyu.carserver.activity.ordercenter.OrderCenterDetailActivity;
import com.renyu.carserver.activity.ordercenter.OrderCenterSearchActivity;
import com.renyu.carserver.adapter.OrderCenterAdapter;
import com.renyu.carserver.base.BaseFragment;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;
import com.renyu.carserver.model.ParentOrderModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/10/20.
 */
public class OrderCenterFragment extends BaseFragment {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
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
    @Bind(R.id.ordercenter_edittext)
    EditText ordercenter_edittext;

    ArrayList<ParentOrderModel> shopModels=null;

    String currentTag="";

    int page_no=1;

    int curPosition=0;

    ArrayList<View> views=null;

    ArrayList<TextView> numTextViews=null;

    //阻止edittext刷新
    boolean isNeedLoad=false;

    @Override
    public int initContentView() {
        return R.layout.fragment_ordercenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shopModels=new ArrayList<>();
        views=new ArrayList<>();
        numTextViews=new ArrayList<>();

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setVisibility(View.GONE);
        View headview= LayoutInflater.from(getActivity()).inflate(R.layout.view_ordercenterheadview, null, false);
        GridLayout ordercenter_gridlayout= (GridLayout) headview.findViewById(R.id.ordercenter_gridlayout);
        int width= CommonUtils.getScreenWidth(getActivity())/5;
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(width, CommonUtils.dp2px(getActivity(), 85));
        for (int i=0;i<10;i++) {
            final int i_=i;
            View itemView=LayoutInflater.from(getActivity()).inflate(R.layout.view_ordercenter_item, null, false);
            views.add(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    page_no=1;
                    isNeedLoad=true;
                    ordercenter_edittext.setText("");
                    hide(ordercenter_edittext);
                    changeChoice(i_);
                }
            });
            TextView view_ordercenter_item_num= (TextView) itemView.findViewById(R.id.view_ordercenter_item_num);
            numTextViews.add(view_ordercenter_item_num);
            TextView view_ordercenter_item_text= (TextView) itemView.findViewById(R.id.view_ordercenter_item_text);
            switch (i) {
                case 0:
                    view_ordercenter_item_text.setText("全部");
                    break;
                case 1:
                    view_ordercenter_item_text.setText("待确认");
                    break;
                case 2:
                    view_ordercenter_item_text.setText("待审核");
                    break;
                case 3:
                    view_ordercenter_item_text.setText("代发货");
                    break;
                case 4:
                    view_ordercenter_item_text.setText("已发货");
                    break;
                case 5:
                    view_ordercenter_item_text.setText("已收货");
                    break;
                case 6:
                    view_ordercenter_item_text.setText("已完成");
                    break;
                case 7:
                    view_ordercenter_item_text.setText("已取消");
                    break;
                case 8:
                    view_ordercenter_item_text.setText("退货");
                    break;
                case 9:
                    view_ordercenter_item_text.setText("已关闭");
                    break;
            }
            ordercenter_gridlayout.addView(itemView, params);
        }
        LinearLayout ordercenter_senior= (LinearLayout) headview.findViewById(R.id.ordercenter_senior);
        ordercenter_senior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderCenterSearchActivity.class);
                startActivity(intent);
            }
        });
        ordercenter_lv.addHeaderView(headview);
        adapter=new OrderCenterAdapter(getActivity(), shopModels, new OrderCenterAdapter.TobepaidChangePriceListener() {
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
                TextView ordercentertobepaid_child_changeprice= (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_changeprice_" + currentTag);
                TextView ordercentertobepaid_child_commit= (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commit_" + currentTag);
                TextView ordercentertobepaid_child_commitsync= (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commitsync_" + currentTag);
                if (ordercentertobepaid_child_finalprice==null||ordercentertobepaid_child_finalprice_edit==null||ordercentertobepaid_child_changeprice==null
                        ||ordercentertobepaid_child_commit==null||ordercentertobepaid_child_commitsync==null) {
                    return;
                }
                ordercentertobepaid_child_finalprice.setVisibility(View.VISIBLE);
                ordercentertobepaid_child_finalprice_edit.setVisibility(View.GONE);
                ordercentertobepaid_child_changeprice.setVisibility(View.VISIBLE);
                ordercentertobepaid_child_commit.setVisibility(View.GONE);
                ordercentertobepaid_child_commitsync.setVisibility(View.GONE);
                currentTag=tag;
            }

            @Override
            public void showChange(int position) {
                priceRemind(position);
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
                        TextView ordercentertobepaid_child_changeprice = (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_changeprice_" + currentTag);
                        TextView ordercentertobepaid_child_commit = (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commit_" + currentTag);
                        TextView ordercentertobepaid_child_commitsync = (TextView) ordercenter_lv.findViewWithTag("ordercentertobepaid_child_commitsync_" + currentTag);
                        if (ordercentertobepaid_child_finalprice==null||ordercentertobepaid_child_finalprice_edit==null||ordercentertobepaid_child_changeprice==null
                                ||ordercentertobepaid_child_commit==null||ordercentertobepaid_child_commitsync==null) {
                            return;
                        }
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
        ordercenter_swipy.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        ordercenter_swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.BOTTOM) {

                } else if (direction == SwipyRefreshLayoutDirection.TOP) {
                    page_no=1;
                }
                loadOrderCenter(ordercenter_edittext.getText().toString());
            }
        });
        ordercenter_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (ordercenter_edittext.getText().toString().equals("")) {
                    showToast("请输入修理厂全称或修理厂简称");
                    return false;
                }
                if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                    ordercenter_swipy.setRefreshing(true);
                    page_no=1;
                    loadOrderCenter(ordercenter_edittext.getText().toString());
                }
                return false;
            }
        });
        ordercenter_edittext.addTextChangedListener(new TextWatcher() {
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
                page_no=1;
                loadOrderCenter(s.toString());
            }
        });
        changeChoice(0);
        getNumber();
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
                        page_no=1;
                        loadOrderCenter(ordercenter_edittext.getText().toString());
                    }
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();

                showToast(getResources().getString(R.string.network_error));
            }
        });
    }

    public void changeChoice(int position) {
        this.curPosition=position;
        shopModels.clear();
        adapter.notifyDataSetChanged();
        for (int i=0;i<views.size();i++) {
            View itemView=views.get(i);
            TextView view_ordercenter_item_text= (TextView) itemView.findViewById(R.id.view_ordercenter_item_text);
            ImageView view_ordercenter_item_image= (ImageView) itemView.findViewById(R.id.view_ordercenter_item_image);
            if (position==i) {
                view_ordercenter_item_text.setTextColor(Color.parseColor("#a20000"));
                switch (i) {
                    case 0:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon1_red);
                        break;
                    case 1:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon2_red);
                        break;
                    case 2:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon3_red);
                        break;
                    case 3:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon4_red);
                        break;
                    case 4:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon5_red);
                        break;
                    case 5:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon6_red);
                        break;
                    case 6:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon7_red);
                        break;
                    case 7:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon8_red);
                        break;
                    case 8:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon9_red);
                        break;
                    case 9:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon10_red);
                        break;
                }
                page_no=1;
                loadOrderCenter(ordercenter_edittext.getText().toString());
            }
            else {
                view_ordercenter_item_text.setTextColor(Color.BLACK);
                switch (i) {
                    case 0:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon1_black);
                        break;
                    case 1:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon2_black);
                        break;
                    case 2:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon3_black);
                        break;
                    case 3:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon4_black);
                        break;
                    case 4:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon5_black);
                        break;
                    case 5:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon6_black);
                        break;
                    case 6:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon7_black);
                        break;
                    case 7:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon8_black);
                        break;
                    case 8:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon9_black);
                        break;
                    case 9:
                        view_ordercenter_item_image.setImageResource(R.mipmap.order_icon10_black);
                        break;
                }
            }
        }
    }

    private void loadOrderCenter(String title) {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.user.order.list", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(getActivity()).getShop_id());
        params.put("page_size", "20");
        params.put("page_no", "" + page_no);
        if (title!=null&&!title.equals("")) {
            params.put("title", title);
        }
        if (curPosition==0) {

        }
        else if (curPosition==1) {
            params.put("status", "WAIT_CONFRIM");
        }
        else if (curPosition==2) {
            params.put("status", "WAIT_APPROVE");
        }
        else if (curPosition==3) {
            params.put("status", "DELIVER_GOODS");
        }
        else if (curPosition==4) {
            params.put("status", "WAIT_GOODS");
        }
        else if (curPosition==5) {
            params.put("status", "RECEIVE_GOODS");
        }
        else if (curPosition==6) {
            params.put("status", "TRADE_FINISHED");
        }
        else if (curPosition==7) {
            params.put("status", "TRADE_CANCEL");
        }
        else if (curPosition==8) {
            params=ParamUtils.getSignParams("app.user.order.aftersaleslist", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
            params.put("service_id", ""+ParamUtils.getLoginModel(getActivity()).getShop_id());
            params.put("page_size", "20");
            params.put("page_no", "" + page_no);
        }
        else if (curPosition==9) {
            params.put("status", "TRADE_CLOSED");
        }
        httpHelper.commonPostRequest(ParamUtils.api, params, new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {
                if (page_no==1) {
                    shopModels.clear();
                    adapter.notifyDataSetChanged();
                    ordercenter_lv.setSelection(0);
                }
            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("OrderCenterFragment", string);
                ordercenter_swipy.setRefreshing(false);
                if (JsonParse.getResultInt(string) == 1) {
                    showToast(JsonParse.getErrorValue(string));
                } else {
                    Object model = JsonParse.getOrderModel(string);
                    //这里就是没有数据
                    if (model==null) {
                        if (page_no==1) {
                            shopModels.clear();
                            adapter.notifyDataSetChanged();
                        }
                    } else if (model instanceof String) {
                        showToast((String) model);
                    } else {
                        if (page_no == 1) {
                            shopModels.clear();
                            shopModels.addAll((ArrayList<ParentOrderModel>) model);
                            ordercenter_lv.setAdapter(adapter);

                            getNumber();
                        } else {
                            shopModels.addAll((ArrayList<ParentOrderModel>) model);
                            adapter.notifyDataSetChanged();
                        }
                        page_no++;
                    }
                }
            }

            @Override
            public void onError() {
                ordercenter_swipy.setRefreshing(false);

                showToast(getResources().getString(R.string.network_error));
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

    private void priceRemind(int position) {
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.priceRemind", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        final String tid=shopModels.get(position).getTid();
        String oid="";
        String price="";
        for (int i=0;i<shopModels.get(position).getModels().size();i++) {
            if (i!=shopModels.get(position).getModels().size()-1) {
                oid+=shopModels.get(position).getModels().get(i).getOid()+",";
                price+=shopModels.get(position).getModels().get(i).getEdit_price()+",";
            }
            else {
                oid+=shopModels.get(position).getModels().get(i).getOid();
                price+=shopModels.get(position).getModels().get(i).getEdit_price();
            }
        }
        final String oid_=oid;
        final String price_=price;
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
                if (JsonParse.getResultCode(string)==0) {
                    price(oid_, price_, tid);
                }
                else if (JsonParse.getResultCode(string)==1) {
                    ordercenter_change_layout.setVisibility(View.VISIBLE);
                    ordercenter_change_commit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ordercenter_change_layout.setVisibility(View.GONE);
                            price(oid_, price_, tid);
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

                showToast(getResources().getString(R.string.network_error));
            }
        });
    }

    private void price(String oid, final String price, String tid) {
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
                        page_no=1;
                        loadOrderCenter(ordercenter_edittext.getText().toString());
                    }
                }
                else {
                    showToast("未知错误");
                }
            }

            @Override
            public void onError() {
                dismissDialog();

                showToast(getResources().getString(R.string.network_error));
            }
        });
    }

    public void getNumber() {
        HashMap<String, String> params= ParamUtils.getSignParams("app.user.order.count", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("service_id", ""+ParamUtils.getLoginModel(getActivity()).getShop_id());
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Log.d("OrderCenterFragment", string);

                HashMap<String, String> map=JsonParse.getOrderNum(string);
                if (map!=null) {
                    /**
                     *
                     'WAIT_CONFRIM'  => '待确认',
                     'WAIT_APPROVE'  => '待审核',
                     'DELIVER_GOODS' => '待发货',
                     'WAIT_GOODS' 	=> '配送中',
                     'RECEIVE_GOODS' => '已收货',
                     'TRADE_FINISHED'=> '已完成',
                     'TRADE_CANCEL'  => '已取消',
                     'TRADE_CLOSED'  => '已关闭',
                     */
                    int WAIT_CONFRIM=Integer.parseInt(map.get("WAIT_CONFRIM"));
                    int WAIT_APPROVE=Integer.parseInt(map.get("WAIT_APPROVE"));
                    int DELIVER_GOODS=Integer.parseInt(map.get("DELIVER_GOODS"));
                    int WAIT_GOODS=Integer.parseInt(map.get("WAIT_GOODS"));
                    int RECEIVE_GOODS=Integer.parseInt(map.get("RECEIVE_GOODS"));
                    int TRADE_FINISHED=Integer.parseInt(map.get("TRADE_FINISHED"));
                    int TRADE_CANCEL=Integer.parseInt(map.get("TRADE_CANCEL"));
                    int TRADE_CLOSED=Integer.parseInt(map.get("TRADE_CLOSED"));
                    int AFTERSALES=Integer.parseInt(map.get("AFTERSALES"));
                    int all=WAIT_CONFRIM+WAIT_APPROVE+DELIVER_GOODS+WAIT_GOODS+RECEIVE_GOODS+TRADE_FINISHED+TRADE_CANCEL+TRADE_CLOSED+AFTERSALES;
                    showHideIndicator(all, numTextViews.get(0));
                    showHideIndicator(WAIT_CONFRIM, numTextViews.get(1));
                    showHideIndicator(WAIT_APPROVE, numTextViews.get(2));
                    showHideIndicator(DELIVER_GOODS, numTextViews.get(3));
                    showHideIndicator(WAIT_GOODS, numTextViews.get(4));
                    showHideIndicator(RECEIVE_GOODS, numTextViews.get(5));
                    showHideIndicator(TRADE_FINISHED, numTextViews.get(6));
                    showHideIndicator(TRADE_CANCEL, numTextViews.get(7));
                    showHideIndicator(AFTERSALES, numTextViews.get(8));
                    showHideIndicator(TRADE_CLOSED, numTextViews.get(9));
                    numTextViews.get(0).setText(""+all);
                    numTextViews.get(1).setText(""+WAIT_CONFRIM);
                    numTextViews.get(2).setText(""+WAIT_APPROVE);
                    numTextViews.get(3).setText(""+DELIVER_GOODS);
                    numTextViews.get(4).setText(""+WAIT_GOODS);
                    numTextViews.get(5).setText(""+RECEIVE_GOODS);
                    numTextViews.get(6).setText(""+TRADE_FINISHED);
                    numTextViews.get(7).setText(""+TRADE_CANCEL);
                    numTextViews.get(8).setText(""+AFTERSALES);
                    numTextViews.get(9).setText(""+TRADE_CLOSED);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void showHideIndicator(int num, TextView view) {
        if (num>0) {
            view.setVisibility(View.VISIBLE);
        }
        else {
            view.setVisibility(View.GONE);
        }
    }
}
