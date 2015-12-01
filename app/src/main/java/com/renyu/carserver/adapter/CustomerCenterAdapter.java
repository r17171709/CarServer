package com.renyu.carserver.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.customercenter.CustomerCenterInfoActivity;
import com.renyu.carserver.activity.customercenter.CustomerCenterPriceActivity;
import com.renyu.carserver.activity.customercenter.CustomerCenterPriceListActivity;
import com.renyu.carserver.model.CustomerModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/10/21.
 */
public class CustomerCenterAdapter extends RecyclerView.Adapter<CustomerCenterAdapter.CustomerCenterHolder> {

    ArrayList<CustomerModel> models=null;
    Context context=null;

    public CustomerCenterAdapter(Context context, ArrayList<CustomerModel> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public CustomerCenterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_customercenter, parent, false);
        return new CustomerCenterHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerCenterHolder holder, final int position) {
        holder.customercenter_name.setText(models.get(position).getRepairdepot_name());
        holder.customercenter_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CustomerCenterInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("model", models.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.customercenter_blling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CustomerCenterPriceActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("model", models.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.customercenter_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CustomerCenterPriceListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("model", models.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    static class CustomerCenterHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.customercenter_name)
        TextView customercenter_name;
        @Bind(R.id.customercenter_info)
        TextView customercenter_info;
        @Bind(R.id.customercenter_blling)
        TextView customercenter_blling;
        @Bind(R.id.customercenter_price)
        TextView customercenter_price;
        public CustomerCenterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
