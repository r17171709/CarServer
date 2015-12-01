package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class CustomerCenterPriceListRightAdapter extends RecyclerView.Adapter<CustomerCenterPriceListRightAdapter.CustomerCenterPriceListRightHolder> {

    Context context=null;
    ArrayList<String> models=null;

    OnItemClickListener listener=null;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CustomerCenterPriceListRightAdapter(Context context, ArrayList<String> models, OnItemClickListener listener) {
        this.context=context;
        this.models=models;
        this.listener=listener;
    }

    @Override
    public CustomerCenterPriceListRightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_right_customercenterpricelist, parent, false);
        return new CustomerCenterPriceListRightHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerCenterPriceListRightHolder holder, final int position) {
        holder.customercenterpricelistright_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class CustomerCenterPriceListRightHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.customercenterpricelistright_name)
        TextView customercenterpricelistright_name;

        public CustomerCenterPriceListRightHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
