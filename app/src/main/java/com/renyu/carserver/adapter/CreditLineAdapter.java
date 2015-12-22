package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.model.CreditLineModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class CreditLineAdapter extends RecyclerView.Adapter<CreditLineAdapter.CreditLineHolder> {

    ArrayList<CreditLineModel> models=null;
    Context context=null;

    ChangeCreditLineListener listener=null;

    public interface ChangeCreditLineListener {
        void change(int position);
    }

    public CreditLineAdapter(Context context, ArrayList<CreditLineModel> models, ChangeCreditLineListener listener) {
        this.models=models;
        this.context=context;
        this.listener=listener;
    }

    @Override
    public CreditLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_creditline, parent, false);
        return new CreditLineHolder(view);
    }

    @Override
    public void onBindViewHolder(CreditLineHolder holder, final int position) {
        holder.adapter_credit_name.setText(models.get(position).getRepairdepot_name());
        holder.adapter_credit_platforminfo.setText("平台授信总额："+models.get(position).getInit_amount()+"    余额："+models.get(position).getAmount());
        holder.adapter_credit_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.change(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class CreditLineHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_credit_name)
        TextView adapter_credit_name;
        @Bind(R.id.adapter_credit_platforminfo)
        TextView adapter_credit_platforminfo;
        @Bind(R.id.adapter_credit_change)
        TextView adapter_credit_change;

        public CreditLineHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
