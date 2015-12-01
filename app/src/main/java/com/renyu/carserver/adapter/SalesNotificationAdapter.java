package com.renyu.carserver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.SalesNotificationModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by renyu on 15/11/12.
 */
public class SalesNotificationAdapter extends RecyclerView.Adapter<SalesNotificationAdapter.SalesNotificationHolder> {

    ArrayList<SalesNotificationModel> models=null;
    Context context=null;

    public SalesNotificationAdapter(Context context, ArrayList<SalesNotificationModel> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public SalesNotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_salesnotification, parent, false);
        return new SalesNotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesNotificationHolder holder, int position) {
        holder.salesnotification_name.setText(models.get(position).getRepairdepot_name());
        holder.salesnotification_oper.setVisibility(View.INVISIBLE);
        holder.salesnotification_time.setText(ParamUtils.getFormatTime(Long.parseLong(models.get(position).getCre_time()+"000")));
        holder.salesnotification_content.setText(models.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class SalesNotificationHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.salesnotification_image)
        CircleImageView salesnotification_image;
        @Bind(R.id.salesnotification_name)
        TextView salesnotification_name;
        @Bind(R.id.salesnotification_oper)
        TextView salesnotification_oper;
        @Bind(R.id.salesnotification_time)
        TextView salesnotification_time;
        @Bind(R.id.salesnotification_content)
        TextView salesnotification_content;

        public SalesNotificationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
