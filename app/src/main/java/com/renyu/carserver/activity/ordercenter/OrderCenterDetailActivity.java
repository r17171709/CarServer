package com.renyu.carserver.activity.ordercenter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.ParentOrderModel;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/13.
 */
public class OrderCenterDetailActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.ordercenterdetail_address)
    TextView ordercenterdetail_address;
    @Bind(R.id.ordercenterdetail_message)
    TextView ordercenterdetail_message;
    @Bind(R.id.ordercenterdetail_idinfo)
    TextView ordercenterdetail_idinfo;
    @Bind(R.id.ordercenterdetail_remark)
    TextView ordercenterdetail_remark;
    @Bind(R.id.ordercenterdetail_createtime)
    TextView ordercenterdetail_createtime;
    @Bind(R.id.ordercenterdetail_paytime)
    TextView ordercenterdetail_paytime;
    @Bind(R.id.ordercenterdetail_shippedtime)
    TextView ordercenterdetail_shippedtime;
    @Bind(R.id.ordercenterdetail_receivetime)
    TextView ordercenterdetail_receivetime;
    @Bind(R.id.ordercenterdetail_needpaytime)
    TextView ordercenterdetail_needpaytime;
    @Bind(R.id.ordercenterdetail_detail)
    LinearLayout ordercenterdetail_detail;

    ParentOrderModel model=null;

    DecimalFormat df = null;

    @Override
    public int initContentView() {
        return R.layout.activity_ordercenterdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        df = new DecimalFormat("###.00");

        model= (ParentOrderModel) getIntent().getExtras().getSerializable("model");

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("订单详情");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);

        ordercenterdetail_address.setText(model.getReceiver_name() + " " + model.getReceiver_mobile() + "\n" +
                model.getReceiver_state() + " " + model.getReceiver_city() + " " + model.getReceiver_district() + model.getReceiver_address());
        ordercenterdetail_message.setText(ParamUtils.converNull(model.getBuyer_message()));
        ordercenterdetail_idinfo.setText(model.getMessage());
        ordercenterdetail_remark.setText(model.getShop_memo());
        ordercenterdetail_createtime.setText("创建时间：\n" + ParamUtils.getFormatTime(ParamUtils.converLong(model.getCreated_time() + "000")));
        ordercenterdetail_paytime.setText("付款时间：\n"+ParamUtils.getFormatTime(ParamUtils.converLong(model.getPay_time() + "000")));
        ordercenterdetail_shippedtime.setText("发货时间：\n"+ParamUtils.getFormatTime(ParamUtils.converLong(model.getConsign_time()+"000")));
        ordercenterdetail_receivetime.setText("收货时间：\n"+ParamUtils.getFormatTime(ParamUtils.converLong(model.getReceiver_time()+"000")));
        ordercenterdetail_needpaytime.setText("应付款时间：\n"+ParamUtils.getFormatTime(ParamUtils.converLong(model.getNeedpaytime()+"000")));

        View convertView= LayoutInflater.from(this).inflate(R.layout.adapter_ordercenterpending, null, false);
        LinearLayout adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
        TextView ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
        TextView ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
        TextView adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
        TextView ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
        TextView ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
        TextView ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
        adapter_ordercenterpending_cancel.setVisibility(View.GONE);
        ordercenterpending_tid.setText(model.getTid());
        getItemViewType(ordercenterpending_state);
        ordercenterpending_num.setText("共"+model.getItemnum()+"件商品");
        ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("value", model.getTid());
                cmb.setPrimaryClip(clip);
            }
        });
        if ((int) Double.parseDouble(model.getTotal_fee())==0) {
            ordercenterpending_price.setText("0");
        }
        else {
            ordercenterpending_price.setText(""+df.format(Double.parseDouble(model.getTotal_fee())));
        }
        adapter_ordercenterpending_detail.removeAllViews();
        for (int i=0;i<model.getModels().size();i++) {
            View view= LayoutInflater.from(this).inflate(R.layout.adapter_ordercenterpending_child, null, false);
            ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
            ImageLoader.getInstance().displayImage(model.getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
            TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
            ordercenterpending_child_title.setText(model.getModels().get(i).getTitle());
            TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
            ordercenterpending_child_sec_title.setText(model.getModels().get(i).getSpec_nature_info());
            TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
            ordercenterpending_child_num.setText("x"+model.getModels().get(i).getNum());
            TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
            if ((int) Double.parseDouble(model.getModels().get(i).getOld_price())==0) {
                ordercenterpending_child_normalprice.setText("0");
            }
            else {
                ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(model.getModels().get(i).getOld_price())));
            }
            TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
            if ((int) Double.parseDouble(model.getModels().get(i).getPrice())==0) {
                ordercenterpending_child_finalprice.setText("0");
            }
            else {
                ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(model.getModels().get(i).getPrice())));
            }
            adapter_ordercenterpending_detail.addView(view);
        }
        ordercenterdetail_detail.addView(convertView);
    }

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
        }
    }

    public DisplayImageOptions getGoodsImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void getItemViewType(TextView textView) {
        Drawable drawable=null;
        if (model.getStatus().equals("WAIT_CONFRIM")) {
            textView.setText("待确认");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon2_red);
        }
        else if (model.getStatus().equals("WAIT_APPROVE")) {
            textView.setText("待审核");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon3_red);
        }
        else if (model.getStatus().equals("DELIVER_GOODS")) {
            textView.setText("待发货");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon4_red);
        }
        else if (model.getStatus().equals("WAIT_GOODS")) {
            textView.setText("配送中");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon5_red);
        }
        else if (model.getStatus().equals("RECEIVE_GOODS")) {
            textView.setText("已收货");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon6_red);
        }
        else if (model.getStatus().equals("TRADE_FINISHED")) {
            textView.setText("已完成");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon7_red);
        }
        else if (model.getStatus().equals("TRADE_CANCEL")) {
            textView.setText("已取消");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon8_red);
        }
        else if (model.getStatus().equals("TRADE_CLOSED")) {
            textView.setText("已关闭");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon10_red);
        }
        else {
            textView.setText("已退货");
            drawable= ContextCompat.getDrawable(this, R.mipmap.order_icon9_red);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
        textView.setCompoundDrawables(drawable, null, null, null);
    }
}
