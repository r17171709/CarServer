package com.renyu.carserver.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.renyu.carserver.R;
import com.renyu.carserver.activity.ordercenter.OrderCenterDetailActivity;
import com.renyu.carserver.model.ParentOrderModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by renyu on 15/10/21.
 */
public class OrderCenterAdapter extends BaseAdapter {

    public final static int TYPE_TOBEPAID=0;
    public final static int TYPE_PENDING=1;
    public final static int TYPE_TOBESHIPPED=2;
    public final static int TYPE_SHIPPED=3;
    public final static int TYPE_RECEIVED=4;
    public final static int TYPE_FINISH=5;
    public final static int TYPE_CANCEL=6;
    public final static int TYPE_CLOSE=8;

    Context context=null;
    ArrayList<ParentOrderModel> models=null;
    TobepaidChangePriceListener listener=null;

    DecimalFormat df = null;

    public OrderCenterAdapter(Context context, ArrayList<ParentOrderModel> models, TobepaidChangePriceListener listener) {
        this.context=context;
        this.models=models;
        this.listener=listener;

        df = new DecimalFormat("###.00");
    }

    public interface TobepaidChangePriceListener {
        void getTag(String tag);
        void showChange(int position);
        void cancel(int position);
        void returnControl(String oid, boolean flag);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int position_=position;
        if (getItemViewType(position)==TYPE_TOBEPAID) {
            OrderCenterTobepaidHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterTobepaidHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercentertobepaid, parent, false);
                holder.adapter_ordercentertobepaid_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercentertobepaid_detail);
                holder.ordercentertobepaid_state= (TextView) convertView.findViewById(R.id.ordercentertobepaid_state);
                holder.ordercentertobepaid_tid= (TextView) convertView.findViewById(R.id.ordercentertobepaid_tid);
                holder.ordercentertobepaid_num= (TextView) convertView.findViewById(R.id.ordercentertobepaid_num);
                holder.ordercentertobepaid_price= (TextView) convertView.findViewById(R.id.ordercentertobepaid_price);
                holder.adapter_ordercentertobepaid_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercentertobepaid_cancel);
                holder.ordercentertobepaid_copy= (TextView) convertView.findViewById(R.id.ordercentertobepaid_copy);
                holder.adapter_ordercentertobepaid_commit= (TextView) convertView.findViewById(R.id.adapter_ordercentertobepaid_commit);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterTobepaidHolder) convertView.getTag();
            }
            holder.ordercentertobepaid_tid.setText(models.get(position).getTid());
            holder.ordercentertobepaid_state.setText("待确认");
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon2_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            holder.ordercentertobepaid_state.setCompoundDrawables(drawable, null, null, null);
            holder.ordercentertobepaid_num.setText("共"+models.get(position).getItemnum()+"件商品");
            holder.adapter_ordercentertobepaid_cancel.setText("取消订单");
            holder.adapter_ordercentertobepaid_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.cancel(position_);
                }
            });
            holder.adapter_ordercentertobepaid_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.showChange(position_);
                }
            });
            holder.ordercentertobepaid_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercentertobepaid_price.setText("0");
            }
            else {
                holder.ordercentertobepaid_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            holder.adapter_ordercentertobepaid_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                final int i_=i;
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercentertobepaid_child, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                ImageView ordercentertobepaid_child_image= (ImageView) view.findViewById(R.id.ordercentertobepaid_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercentertobepaid_child_image, getGoodsImageOptions());
                TextView ordercentertobepaid_child_title= (TextView) view.findViewById(R.id.ordercentertobepaid_child_title);
                ordercentertobepaid_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercentertobepaid_child_sec_title= (TextView) view.findViewById(R.id.ordercentertobepaid_child_sec_title);
                ordercentertobepaid_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercentertobepaid_child_num= (TextView) view.findViewById(R.id.ordercentertobepaid_child_num);
                ordercentertobepaid_child_num.setText("x" + models.get(position).getModels().get(i).getNum());
                TextView ordercentertobepaid_child_normalprice= (TextView) view.findViewById(R.id.ordercentertobepaid_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercentertobepaid_child_normalprice.setText("0");
                }
                else {
                    ordercentertobepaid_child_normalprice.setText("" + df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                final TextView ordercentertobepaid_child_finalprice= (TextView) view.findViewById(R.id.ordercentertobepaid_child_finalprice);
                ordercentertobepaid_child_finalprice.setTag("ordercentertobepaid_child_finalprice_" + position + "_" + i);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercentertobepaid_child_finalprice.setText("0");
                }
                else {
                    ordercentertobepaid_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercentertobepaid_child_state= (TextView) view.findViewById(R.id.ordercentertobepaid_child_state);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercentertobepaid_child_state.setText("退货中");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercentertobepaid_child_state.setText("已退货");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercentertobepaid_child_state.setText("已驳回");
                }
                final EditText ordercentertobepaid_child_finalprice_edit= (EditText) view.findViewById(R.id.ordercentertobepaid_child_finalprice_edit);
                ordercentertobepaid_child_finalprice_edit.setText(""+models.get(position).getModels().get(i).getEdit_price());
                ordercentertobepaid_child_finalprice_edit.setTag("ordercentertobepaid_child_finalprice_edit_"+position+"_"+i);
                final TextView ordercentertobepaid_child_changeprice= (TextView) view.findViewById(R.id.ordercentertobepaid_child_changeprice);
                ordercentertobepaid_child_changeprice.setTag("ordercentertobepaid_child_changeprice_" + position + "_" + i);
                ordercentertobepaid_child_changeprice.setVisibility(View.VISIBLE);
                final TextView ordercentertobepaid_child_commit= (TextView) view.findViewById(R.id.ordercentertobepaid_child_commit);
                ordercentertobepaid_child_commit.setTag("ordercentertobepaid_child_commit_"+position+"_"+i);
                final TextView ordercentertobepaid_child_commitsync= (TextView) view.findViewById(R.id.ordercentertobepaid_child_commitsync);
                ordercentertobepaid_child_commitsync.setTag("ordercentertobepaid_child_commitsync_" + position + "_" + i);

                ordercentertobepaid_child_commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ordercentertobepaid_child_finalprice.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_finalprice_edit.setVisibility(View.GONE);
                        ordercentertobepaid_child_changeprice.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_commit.setVisibility(View.GONE);
                        ordercentertobepaid_child_commitsync.setVisibility(View.GONE);

                        if (!ordercentertobepaid_child_finalprice_edit.getText().toString().equals("")) {
                            if ((int) Double.parseDouble(ordercentertobepaid_child_finalprice_edit.getText().toString())==0) {
                                ordercentertobepaid_child_finalprice.setText("0");
                            }
                            else {
                                ordercentertobepaid_child_finalprice.setText(""+df.format(Double.parseDouble(ordercentertobepaid_child_finalprice_edit.getText().toString())));
                            }
                            models.get(position).getModels().get(i_).setPrice(ordercentertobepaid_child_finalprice_edit.getText().toString());
                            models.get(position).getModels().get(i_).setEdit_price(Double.parseDouble(ordercentertobepaid_child_finalprice_edit.getText().toString()));
                            notifyDataSetChanged();
                        }
                    }
                });
                ordercentertobepaid_child_commitsync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ordercentertobepaid_child_finalprice.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_finalprice_edit.setVisibility(View.GONE);
                        ordercentertobepaid_child_changeprice.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_commit.setVisibility(View.GONE);
                        ordercentertobepaid_child_commitsync.setVisibility(View.GONE);

                        if (!ordercentertobepaid_child_finalprice_edit.getText().toString().equals("")) {
                            if ((int) Double.parseDouble(ordercentertobepaid_child_finalprice_edit.getText().toString())==0) {
                                ordercentertobepaid_child_finalprice.setText("0");
                            }
                            else {
                                ordercentertobepaid_child_finalprice.setText(""+df.format(Double.parseDouble(ordercentertobepaid_child_finalprice_edit.getText().toString())));
                            }
                            models.get(position).getModels().get(i_).setPrice(ordercentertobepaid_child_finalprice_edit.getText().toString());
                            models.get(position).getModels().get(i_).setEdit_price(Double.parseDouble(ordercentertobepaid_child_finalprice_edit.getText().toString()));
                            notifyDataSetChanged();
                        }
                    }
                });
                ordercentertobepaid_child_changeprice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.getTag(position_ + "_" + i_);

                        ordercentertobepaid_child_finalprice.setVisibility(View.INVISIBLE);
                        ordercentertobepaid_child_finalprice_edit.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_changeprice.setVisibility(View.GONE);
                        ordercentertobepaid_child_commit.setVisibility(View.VISIBLE);
                        ordercentertobepaid_child_commitsync.setVisibility(View.VISIBLE);
                    }
                });
                holder.adapter_ordercentertobepaid_detail.addView(view);
            }
        }
        else if (getItemViewType(position)==TYPE_PENDING) {
            OrderCenterPendingHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterPendingHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending, parent, false);
                holder.adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
                holder.ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
                holder.ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
                holder.ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
                holder.adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
                holder.ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
                holder.ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterPendingHolder) convertView.getTag();
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.VISIBLE);
            holder.adapter_ordercenterpending_cancel.setText("取消订单");
            holder.adapter_ordercenterpending_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.cancel(position_);
                }
            });
            holder.ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            holder.ordercenterpending_tid.setText(models.get(position).getTid());
            holder.ordercenterpending_state.setText("待审核");
            holder.ordercenterpending_num.setText("共" + models.get(position).getItemnum() + "件商品");
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercenterpending_price.setText("0");
            }
            else {
                holder.ordercenterpending_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon3_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            holder.ordercenterpending_state.setCompoundDrawables(drawable, null, null, null);
            holder.adapter_ordercenterpending_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending_child, parent, false);
                ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
                TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
                ordercenterpending_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
                ordercenterpending_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
                ordercenterpending_child_num.setText("x" + models.get(position).getModels().get(i).getNum());
                TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercenterpending_child_normalprice.setText("0");
                }
                else {
                    ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercenterpending_child_finalprice.setText("0");
                }
                else {
                    ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercenterpending_child_state= (TextView) view.findViewById(R.id.ordercenterpending_child_state);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercenterpending_child_state.setText("退货中");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercenterpending_child_state.setText("已退货");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercenterpending_child_state.setText("已驳回");
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                holder.adapter_ordercenterpending_detail.addView(view);
            }
        }
        else if (getItemViewType(position)==TYPE_TOBESHIPPED) {
            OrderCenterTobeshippedHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterTobeshippedHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending, parent, false);
                holder.adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
                holder.ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
                holder.ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
                holder.ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
                holder.adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
                holder.ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
                holder.ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterTobeshippedHolder) convertView.getTag();
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.VISIBLE);
            holder.adapter_ordercenterpending_cancel.setText("取消订单");
            holder.adapter_ordercenterpending_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.cancel(position_);
                }
            });
            holder.ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            holder.ordercenterpending_tid.setText(models.get(position).getTid());
            holder.ordercenterpending_state.setText("待发货");
            holder.ordercenterpending_num.setText("共"+models.get(position).getItemnum()+"件商品");
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercenterpending_price.setText("0");
            }
            else {
                holder.ordercenterpending_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon4_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            holder.ordercenterpending_state.setCompoundDrawables(drawable, null, null, null);
            holder.adapter_ordercenterpending_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending_child, parent, false);
                ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
                TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
                ordercenterpending_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
                ordercenterpending_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
                ordercenterpending_child_num.setText("x"+models.get(position).getModels().get(i).getNum());
                TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercenterpending_child_normalprice.setText("0");
                }
                else {
                    ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercenterpending_child_finalprice.setText("0");
                }
                else {
                    ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercenterpending_child_state= (TextView) view.findViewById(R.id.ordercenterpending_child_state);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercenterpending_child_state.setText("退货中");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercenterpending_child_state.setText("已退货");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercenterpending_child_state.setText("已驳回");
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                holder.adapter_ordercenterpending_detail.addView(view);
            }
        }
        else if (getItemViewType(position)==TYPE_SHIPPED) {
            OrderCenterShippedHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterShippedHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending, parent, false);
                holder.adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
                holder.ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
                holder.ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
                holder.adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
                holder.ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
                holder.ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
                holder.ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterShippedHolder) convertView.getTag();
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
            holder.ordercenterpending_tid.setText(models.get(position).getTid());
            holder.ordercenterpending_state.setText("已发货");
            holder.ordercenterpending_num.setText("共" + models.get(position).getItemnum() + "件商品");
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercenterpending_price.setText("0");
            }
            else {
                holder.ordercenterpending_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            holder.ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon5_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            holder.ordercenterpending_state.setCompoundDrawables(drawable, null, null, null);
            holder.adapter_ordercenterpending_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending_child, parent, false);
                ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
                TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
                ordercenterpending_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
                ordercenterpending_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
                ordercenterpending_child_num.setText("x"+models.get(position).getModels().get(i).getNum());
                TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercenterpending_child_normalprice.setText("0");
                }
                else {
                    ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercenterpending_child_finalprice.setText("0");
                }
                else {
                    ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercenterpending_child_state= (TextView) view.findViewById(R.id.ordercenterpending_child_state);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercenterpending_child_state.setText("退货中");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercenterpending_child_state.setText("已退货");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercenterpending_child_state.setText("已驳回");
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                holder.adapter_ordercenterpending_detail.addView(view);
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
        }
        else if (getItemViewType(position)==TYPE_RECEIVED) {
            OrderCenterReceivedHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterReceivedHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending, parent, false);
                holder.ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
                holder.ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
                holder.adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
                holder.ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
                holder.adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
                holder.ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
                holder.ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterReceivedHolder) convertView.getTag();
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.VISIBLE);
            holder.adapter_ordercenterpending_cancel.setText("代付");
            holder.adapter_ordercenterpending_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            holder.ordercenterpending_tid.setText(models.get(position).getTid());
            holder.ordercenterpending_state.setText("已收货");
            holder.ordercenterpending_num.setText("共"+models.get(position).getItemnum()+"件商品");
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercenterpending_price.setText("0");
            }
            else {
                holder.ordercenterpending_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon6_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            holder.ordercenterpending_state.setCompoundDrawables(drawable, null, null, null);            holder.adapter_ordercenterpending_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                final int i_=i;
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending_child, parent, false);
                ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
                TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
                ordercenterpending_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
                ordercenterpending_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
                ordercenterpending_child_num.setText("x"+models.get(position).getModels().get(i).getNum());
                TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercenterpending_child_normalprice.setText("0");
                }
                else {
                    ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercenterpending_child_finalprice.setText("0");
                }
                else {
                    ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercenterpending_child_state= (TextView) view.findViewById(R.id.ordercenterpending_child_state);
                LinearLayout ordercenterpending_child_return= (LinearLayout) view.findViewById(R.id.ordercenterpending_child_return);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercenterpending_child_state.setText("退货中");
                    ordercenterpending_child_return.setVisibility(View.VISIBLE);
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercenterpending_child_state.setText("已退货");
                    ordercenterpending_child_return.setVisibility(View.GONE);
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercenterpending_child_state.setText("已驳回");
                    ordercenterpending_child_return.setVisibility(View.GONE);
                }
                TextView ordercenterpending_child_forbid= (TextView) view.findViewById(R.id.ordercenterpending_child_forbid);
                ordercenterpending_child_forbid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.returnControl(models.get(position).getModels().get(i_).getOid(), false);
                    }
                });
                TextView ordercenterpending_child_commit= (TextView) view.findViewById(R.id.ordercenterpending_child_commit);
                ordercenterpending_child_commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.returnControl(models.get(position).getModels().get(i_).getOid(), true);
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                holder.adapter_ordercenterpending_detail.addView(view);
            }
        }
        else if (getItemViewType(position)==TYPE_FINISH) {
            OrderCenterFinishHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterFinishHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending, parent, false);
                holder.adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
                holder.ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
                holder.ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
                holder.adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
                holder.ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
                holder.ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
                holder.ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterFinishHolder) convertView.getTag();
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
            holder.ordercenterpending_tid.setText(models.get(position).getTid());
            holder.ordercenterpending_state.setText("已完成");
            holder.ordercenterpending_num.setText("共" + models.get(position).getItemnum() + "件商品");
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercenterpending_price.setText("0");
            }
            else {
                holder.ordercenterpending_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            holder.ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon7_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
            holder.ordercenterpending_state.setCompoundDrawables(drawable, null, null, null);
            holder.adapter_ordercenterpending_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending_child, parent, false);
                ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
                TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
                ordercenterpending_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
                ordercenterpending_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
                ordercenterpending_child_num.setText("x"+models.get(position).getModels().get(i).getNum());
                TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercenterpending_child_normalprice.setText("0");
                }
                else {
                    ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercenterpending_child_finalprice.setText("0");
                }
                else {
                    ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercenterpending_child_state= (TextView) view.findViewById(R.id.ordercenterpending_child_state);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercenterpending_child_state.setText("退货中");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercenterpending_child_state.setText("已退货");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercenterpending_child_state.setText("已驳回");
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                holder.adapter_ordercenterpending_detail.addView(view);
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
        }
        else if (getItemViewType(position)==TYPE_CANCEL) {
            OrderCenterCancelHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterCancelHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending, parent, false);
                holder.adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
                holder.ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
                holder.ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
                holder.adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
                holder.ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
                holder.ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
                holder.ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterCancelHolder) convertView.getTag();
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
            holder.ordercenterpending_tid.setText(models.get(position).getTid());
            holder.ordercenterpending_state.setText("已取消");
            holder.ordercenterpending_num.setText("共" + models.get(position).getItemnum() + "件商品");
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercenterpending_price.setText("0");
            }
            else {
                holder.ordercenterpending_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            holder.ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon8_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            holder.ordercenterpending_state.setCompoundDrawables(drawable, null, null, null);
            holder.adapter_ordercenterpending_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending_child, parent, false);
                ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
                TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
                ordercenterpending_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
                ordercenterpending_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
                ordercenterpending_child_num.setText("x"+models.get(position).getModels().get(i).getNum());
                TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercenterpending_child_normalprice.setText("0");
                }
                else {
                    ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercenterpending_child_finalprice.setText("0");
                }
                else {
                    ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercenterpending_child_state= (TextView) view.findViewById(R.id.ordercenterpending_child_state);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercenterpending_child_state.setText("退货中");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercenterpending_child_state.setText("已退货");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercenterpending_child_state.setText("已驳回");
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                holder.adapter_ordercenterpending_detail.addView(view);
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
        }
        else if (getItemViewType(position)==TYPE_CLOSE) {
            OrderCenterCloseHolder holder=null;
            if (convertView==null) {
                holder=new OrderCenterCloseHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending, parent, false);
                holder.adapter_ordercenterpending_detail= (LinearLayout) convertView.findViewById(R.id.adapter_ordercenterpending_detail);
                holder.ordercenterpending_state= (TextView) convertView.findViewById(R.id.ordercenterpending_state);
                holder.ordercenterpending_tid= (TextView) convertView.findViewById(R.id.ordercenterpending_tid);
                holder.adapter_ordercenterpending_cancel= (TextView) convertView.findViewById(R.id.adapter_ordercenterpending_cancel);
                holder.ordercenterpending_num= (TextView) convertView.findViewById(R.id.ordercenterpending_num);
                holder.ordercenterpending_price= (TextView) convertView.findViewById(R.id.ordercenterpending_price);
                holder.ordercenterpending_copy= (TextView) convertView.findViewById(R.id.ordercenterpending_copy);
                convertView.setTag(holder);
            }
            else {
                holder= (OrderCenterCloseHolder) convertView.getTag();
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
            holder.ordercenterpending_tid.setText(models.get(position).getTid());
            holder.ordercenterpending_state.setText("已关闭");
            holder.ordercenterpending_num.setText("共" + models.get(position).getItemnum() + "件商品");
            if ((int) Double.parseDouble(models.get(position).getTotal_fee())==0) {
                holder.ordercenterpending_price.setText("0");
            }
            else {
                holder.ordercenterpending_price.setText(""+df.format(Double.parseDouble(models.get(position).getTotal_fee())));
            }
            holder.ordercenterpending_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("value",models.get(position).getTid());
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                }
            });
            Drawable drawable=ContextCompat.getDrawable(context, R.mipmap.order_icon10_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            holder.ordercenterpending_state.setCompoundDrawables(drawable, null, null, null);
            holder.adapter_ordercenterpending_detail.removeAllViews();
            for (int i=0;i<models.get(position).getModels().size();i++) {
                View view=LayoutInflater.from(context).inflate(R.layout.adapter_ordercenterpending_child, parent, false);
                ImageView ordercenterpending_child_image= (ImageView) view.findViewById(R.id.ordercenterpending_child_image);
                ImageLoader.getInstance().displayImage(models.get(position).getModels().get(i).getPic_path(), ordercenterpending_child_image, getGoodsImageOptions());
                TextView ordercenterpending_child_title= (TextView) view.findViewById(R.id.ordercenterpending_child_title);
                ordercenterpending_child_title.setText(models.get(position).getModels().get(i).getTitle());
                TextView ordercenterpending_child_sec_title= (TextView) view.findViewById(R.id.ordercenterpending_child_sec_title);
                ordercenterpending_child_sec_title.setText(models.get(position).getModels().get(i).getSpec_nature_info());
                TextView ordercenterpending_child_num= (TextView) view.findViewById(R.id.ordercenterpending_child_num);
                ordercenterpending_child_num.setText("x"+models.get(position).getModels().get(i).getNum());
                TextView ordercenterpending_child_normalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_normalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getOld_price())==0) {
                    ordercenterpending_child_normalprice.setText("0");
                }
                else {
                    ordercenterpending_child_normalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getOld_price())));
                }
                TextView ordercenterpending_child_finalprice= (TextView) view.findViewById(R.id.ordercenterpending_child_finalprice);
                if ((int) Double.parseDouble(models.get(position).getModels().get(i).getPrice())==0) {
                    ordercenterpending_child_finalprice.setText("0");
                }
                else {
                    ordercenterpending_child_finalprice.setText(""+df.format(Double.parseDouble(models.get(position).getModels().get(i).getPrice())));
                }
                TextView ordercenterpending_child_state= (TextView) view.findViewById(R.id.ordercenterpending_child_state);
                if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_SELLER_AGREE")) {
                    ordercenterpending_child_state.setText("退货中");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("WAIT_BUYER_RETURN_GOODS")) {
                    ordercenterpending_child_state.setText("已退货");
                }
                else if (models.get(position).getModels().get(i).getAftersales_status().equals("SELLER_REFUSE_BUYER")) {
                    ordercenterpending_child_state.setText("已驳回");
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OrderCenterDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("model", models.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                holder.adapter_ordercenterpending_detail.addView(view);
            }
            holder.adapter_ordercenterpending_cancel.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (models.get(position).getStatus().equals("WAIT_CONFRIM")) {
            return TYPE_TOBEPAID;
        }
        else if (models.get(position).getStatus().equals("WAIT_APPROVE")) {
            return TYPE_PENDING;
        }
        else if (models.get(position).getStatus().equals("DELIVER_GOODS")) {
            return TYPE_TOBESHIPPED;
        }
        else if (models.get(position).getStatus().equals("WAIT_GOODS")) {
            return TYPE_SHIPPED;
        }
        else if (models.get(position).getStatus().equals("RECEIVE_GOODS")) {
            return TYPE_RECEIVED;
        }
        else if (models.get(position).getStatus().equals("TRADE_FINISHED")) {
            return TYPE_FINISH;
        }
        else if (models.get(position).getStatus().equals("TRADE_CANCEL")) {
            return TYPE_CANCEL;
        }
        else if (models.get(position).getStatus().equals("TRADE_CLOSED")) {
            return TYPE_CLOSE;
        }
        else {
            return 100;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 9;
    }

    static class OrderCenterTobepaidHolder {
        LinearLayout adapter_ordercentertobepaid_detail;
        TextView ordercentertobepaid_tid;
        TextView ordercentertobepaid_copy;
        TextView ordercentertobepaid_state;
        TextView ordercentertobepaid_num;
        TextView ordercentertobepaid_price;
        TextView ordercentertobepaid_invoice;
        TextView ordercentertobepaid_remarks;
        TextView adapter_ordercentertobepaid_commit;
        TextView adapter_ordercentertobepaid_cancel;
    }

    static class OrderCenterPendingHolder {
        LinearLayout adapter_ordercenterpending_detail;
        TextView ordercenterpending_tid;
        TextView ordercenterpending_copy;
        TextView ordercenterpending_state;
        TextView ordercenterpending_num;
        TextView ordercenterpending_price;
        TextView ordercenterpending_remarks;
        TextView adapter_ordercenterpending_cancel;
    }

    static class OrderCenterTobeshippedHolder {
        LinearLayout adapter_ordercenterpending_detail;
        TextView ordercenterpending_tid;
        TextView ordercenterpending_copy;
        TextView ordercenterpending_state;
        TextView ordercenterpending_num;
        TextView ordercenterpending_price;
        TextView ordercenterpending_remarks;
        TextView adapter_ordercenterpending_cancel;
    }

    static class OrderCenterShippedHolder {
        LinearLayout adapter_ordercenterpending_detail;
        TextView ordercenterpending_tid;
        TextView ordercenterpending_copy;
        TextView ordercenterpending_state;
        TextView ordercenterpending_num;
        TextView ordercenterpending_price;
        TextView ordercenterpending_remarks;
        TextView adapter_ordercenterpending_cancel;
    }

    static class OrderCenterReceivedHolder {
        LinearLayout adapter_ordercenterpending_detail;
        TextView ordercenterpending_tid;
        TextView ordercenterpending_copy;
        TextView ordercenterpending_state;
        TextView ordercenterpending_num;
        TextView ordercenterpending_price;
        TextView ordercenterpending_remarks;
        TextView adapter_ordercenterpending_cancel;
    }

    static class OrderCenterFinishHolder {
        LinearLayout adapter_ordercenterpending_detail;
        TextView ordercenterpending_tid;
        TextView ordercenterpending_copy;
        TextView ordercenterpending_state;
        TextView ordercenterpending_num;
        TextView ordercenterpending_price;
        TextView ordercenterpending_remarks;
        TextView adapter_ordercenterpending_cancel;
    }

    static class OrderCenterCancelHolder {
        LinearLayout adapter_ordercenterpending_detail;
        TextView ordercenterpending_tid;
        TextView ordercenterpending_copy;
        TextView ordercenterpending_state;
        TextView ordercenterpending_num;
        TextView ordercenterpending_price;
        TextView ordercenterpending_remarks;
        TextView adapter_ordercenterpending_cancel;
    }

    static class OrderCenterReturnHolder {
        LinearLayout adapter_ordercentertobepaid_detail;
        TextView ordercentertobepaid_tid;
        TextView ordercentertobepaid_copy;
        TextView ordercentertobepaid_state;
        TextView ordercentertobepaid_num;
        TextView ordercentertobepaid_price;
        TextView ordercentertobepaid_invoice;
        TextView ordercentertobepaid_remarks;
        TextView adapter_ordercentertobepaid_commit;
        TextView adapter_ordercentertobepaid_cancel;
    }

    static class OrderCenterCloseHolder {
        LinearLayout adapter_ordercenterpending_detail;
        TextView ordercenterpending_tid;
        TextView ordercenterpending_copy;
        TextView ordercenterpending_state;
        TextView ordercenterpending_num;
        TextView ordercenterpending_price;
        TextView ordercenterpending_remarks;
        TextView adapter_ordercenterpending_cancel;
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

}
