package com.renyu.carserver.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.workbench.AddCustomerActivity;
import com.renyu.carserver.activity.workbench.ClientsReviewActivity;
import com.renyu.carserver.activity.workbench.CreditLineActivity;
import com.renyu.carserver.activity.workbench.FactorApplyActivity;
import com.renyu.carserver.activity.workbench.MessageCenterActivity;
import com.renyu.carserver.activity.workbench.SalesNotificationActivity;
import com.renyu.carserver.activity.workbench.SearchCreditLineActivity;
import com.renyu.carserver.activity.workbench.ShareActivity;
import com.renyu.carserver.base.BaseFragment;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.OKHttpHelper;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.JsonParse;

import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by renyu on 15/10/20.
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.main_top_gridlayout)
    GridLayout main_top_gridlayout;
    @Bind(R.id.main_middle_gridlayout)
    GridLayout main_middle_gridlayout;
    TextView main_top_gridlayout_num1=null;
    TextView main_top_gridlayout_num2=null;
    RelativeLayout main_top_layout;

    JumpListener listener=null;

    public interface JumpListener {
        void changeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener=(JumpListener) context;
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTopView();
        initMiddleView();
    }

    private void initTopView() {
        int width=CommonUtils.getScreenWidth(getActivity())/2;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, CommonUtils.dp2px(getActivity(), 60));
        for (int i=0;i<2;i++) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.view_main_topgrid_item, null, false);
            TextView main_top_gridlayout_title= (TextView) view.findViewById(R.id.main_top_gridlayout_title);
            if (i==0) {
                main_top_gridlayout_title.setText("今日订单数");
                main_top_gridlayout_num1= (TextView) view.findViewById(R.id.main_top_gridlayout_num);
                main_top_gridlayout_num1.setText(ParamUtils.getLoginModel(getActivity()).getCount());
                main_top_layout= (RelativeLayout) view.findViewById(R.id.main_top_layout);
                main_top_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.changeFragment();
                    }
                });
            }
            else if (i==1) {
                main_top_gridlayout_title.setText("今日成交额");
                main_top_gridlayout_num2= (TextView) view.findViewById(R.id.main_top_gridlayout_num);
                main_top_gridlayout_num2.setText(ParamUtils.getLoginModel(getActivity()).getSum());
            }
            LinearLayout linearLayout=new LinearLayout(getActivity());
            linearLayout.addView(view, params);
            main_top_gridlayout.addView(linearLayout);
        }
    }

    private void initMiddleView() {
        int width=CommonUtils.getScreenWidth(getActivity())/4;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<12;i++) {
            final int position=i;
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.view_main_middlegrid_item, null, false);
            TextView main_middle_gridlayout_item_name= (TextView) view.findViewById(R.id.main_middle_gridlayout_item_name);
            ImageView main_middle_gridlayout_item_image= (ImageView) view.findViewById(R.id.main_middle_gridlayout_item_image);
            switch (i) {
                case 0:
                    main_middle_gridlayout_item_name.setText("站内信");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon1);
                    break;
                case 1:
                    main_middle_gridlayout_item_name.setText("授信额度");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon2);
                    break;
                case 2:
                    main_middle_gridlayout_item_name.setText("授信额度\n变更查询");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon3);
                    break;
                case 3:
                    main_middle_gridlayout_item_name.setText("数据中心");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon4);
                    break;
                case 4:
                    main_middle_gridlayout_item_name.setText("会员申请");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon5);
                    break;
                case 5:
                    main_middle_gridlayout_item_name.setText("新增会员");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon6);
                    break;
                case 6:
                    main_middle_gridlayout_item_name.setText("会员审核");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon7);
                    break;
                case 7:
                    main_middle_gridlayout_item_name.setText("售后通知");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon8);
                    break;
                case 8:
                    main_middle_gridlayout_item_name.setText("服务商应用");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon9);
                    break;
                case 9:
                    main_middle_gridlayout_item_name.setText("修理厂应用");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon10);
                    break;
                case 10:
                    main_middle_gridlayout_item_name.setText("微信公众号");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon11);
                    break;
                case 11:
                    main_middle_gridlayout_item_name.setText("查找商品");
                    main_middle_gridlayout_item_image.setImageResource(R.mipmap.ic_workplate_icon12);
                    break;
            }
            LinearLayout linearLayout=new LinearLayout(getActivity());
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jump(position);
                }
            });
            linearLayout.addView(view, params);
            main_middle_gridlayout.addView(linearLayout);
        }
    }

    private void jump(int position) {
        Intent intent=null;
        switch (position) {
            case 0:
                intent=new Intent(getActivity(), MessageCenterActivity.class);
                break;
            case 1:
                intent=new Intent(getActivity(), CreditLineActivity.class);
                break;
            case 2:
                intent=new Intent(getActivity(), SearchCreditLineActivity.class);
                break;
            case 3:
                return;
            case 4:
                intent=new Intent(getActivity(), FactorApplyActivity.class);
                break;
            case 5:
                intent=new Intent(getActivity(), AddCustomerActivity.class);
                break;
            case 6:
                intent=new Intent(getActivity(), ClientsReviewActivity.class);
                break;
            case 7:
                intent=new Intent(getActivity(), SalesNotificationActivity.class);
                break;
            case 8:
                intent=new Intent(getActivity(), ShareActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt("type", ParamUtils.RESULT_CARSERVER);
                intent.putExtras(bundle1);
                break;
            case 9:
                intent=new Intent(getActivity(), ShareActivity.class);
                Bundle bundle2=new Bundle();
                bundle2.putInt("type", ParamUtils.RESULT_CARCLIENT);
                intent.putExtras(bundle2);
                break;
            case 10:
                intent=new Intent(getActivity(), ShareActivity.class);
                Bundle bundle3=new Bundle();
                bundle3.putInt("type", ParamUtils.RESULT_CARWEIXIN);
                intent.putExtras(bundle3);
                break;
            case 11:
                return;
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateNum();
    }

    private void updateNum() {
        httpHelper.cancel(ParamUtils.api);
        HashMap<String, String> params= ParamUtils.getSignParams("app.sysservice.user.ordercount", "28062e40a8b27e26ba3be45330ebcb0133bc1d1cf03e17673872331e859d2cd4");
        params.put("serviceid", ""+ParamUtils.getLoginModel(getActivity()).getShop_id());
        httpHelper.commonPostRequest(ParamUtils.api, params, null, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                HashMap<String, String> map= JsonParse.getTodayInfo(string);
                if (map!=null) {
                    main_top_gridlayout_num1.setText(map.get("count"));
                    main_top_gridlayout_num2.setText(map.get("sum"));
                }
            }

            @Override
            public void onError() {

            }
        });
    }
}
