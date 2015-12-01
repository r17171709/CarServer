package com.renyu.carserver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.FactoryApplyModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class FactorApplyAdapter extends RecyclerView.Adapter<FactorApplyAdapter.FactorApplyHolder> {

    ArrayList<FactoryApplyModel> models=null;
    Context context=null;

    public FactorApplyAdapter(Context context, ArrayList<FactoryApplyModel> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public FactorApplyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_factorapply, parent, false);
        return new FactorApplyHolder(view);
    }

    @Override
    public void onBindViewHolder(FactorApplyHolder holder, int position) {
        holder.adapter_factoryapplyinfo.setText(models.get(position).getRepair_name()+"，"+models.get(position).getContact_person()+"，"+models.get(position).getContact_tel());
        String address="";
        for (int i=0;i<models.get(position).getAreaframe().split("/").length;i++) {
            address+=CommonUtils.getCityInfo(models.get(position).getAreaframe().split("/")[i]);
        }
        address+=models.get(position).getAddrs();
        holder.adapter_factoryapplyaddress.setText(address);
        if (models.get(position).getStatus()==2) {
            holder.adapter_factoryapplystate.setText("已处理");
            holder.adapter_factoryapplystate.setTextColor(Color.parseColor("#efefef"));
            holder.adapter_factoryapplyinfo.setTextColor(Color.parseColor("#efefef"));
            holder.adapter_factoryapplyaddress.setTextColor(Color.parseColor("#efefef"));
        }
        else if (models.get(position).getStatus()==1) {
            holder.adapter_factoryapplystate.setText("确认处理");
            holder.adapter_factoryapplystate.setTextColor(Color.parseColor("#a20000"));
            holder.adapter_factoryapplyinfo.setTextColor(Color.BLACK);
            holder.adapter_factoryapplyaddress.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class FactorApplyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.adapter_factoryapplytime)
        TextView adapter_factoryapplytime;
        @Bind(R.id.adapter_factoryapplyinfo)
        TextView adapter_factoryapplyinfo;
        @Bind(R.id.adapter_factoryapplystate)
        TextView adapter_factoryapplystate;
        @Bind(R.id.adapter_factoryapplyaddress)
        TextView adapter_factoryapplyaddress;
        public FactorApplyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
