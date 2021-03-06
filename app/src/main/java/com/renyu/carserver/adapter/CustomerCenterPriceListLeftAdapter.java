package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renyu.carserver.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class CustomerCenterPriceListLeftAdapter extends RecyclerView.Adapter<CustomerCenterPriceListLeftAdapter.CustomerCenterPriceListLeftHolder> {

    Context context=null;
    ArrayList<String> models=null;
    OnItemClickListener listener=null;

    public CustomerCenterPriceListLeftAdapter(Context context, ArrayList<String> models, OnItemClickListener listener) {
        this.context=context;
        this.models=models;
        this.listener=listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public CustomerCenterPriceListLeftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_left_customercenterpricelist, parent, false);
        return new CustomerCenterPriceListLeftHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerCenterPriceListLeftHolder holder, final int position) {
        holder.customercenterpricelistleft_layout.setOnClickListener(new View.OnClickListener() {
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

    public class CustomerCenterPriceListLeftHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.customercenterpricelistleft_image)
        ImageView customercenterpricelistleft_image;
        @Bind(R.id.customercenterpricelistleft_text)
        TextView customercenterpricelistleft_text;
        @Bind(R.id.customercenterpricelistleft_layout)
        LinearLayout customercenterpricelistleft_layout;

        public CustomerCenterPriceListLeftHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
