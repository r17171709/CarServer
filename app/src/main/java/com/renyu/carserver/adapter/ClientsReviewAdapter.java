package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.model.ClientsReviewModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class ClientsReviewAdapter extends RecyclerView.Adapter<ClientsReviewAdapter.ClientsReviewHolder> {

    Context context=null;
    ArrayList<ClientsReviewModel> models=null;

    public ClientsReviewAdapter(Context context, ArrayList<ClientsReviewModel> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public ClientsReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_clientsreview, parent, false);
        return new ClientsReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientsReviewHolder holder, int position) {
        holder.adapter_clientsreview_info.setText(models.get(position).getRepairdepot_name()+"  "+models.get(position).getContact_person()+"  "+models.get(position).getContact_tel());
        String area="";
        for (int i=0;i<models.get(position).getAreaframe().split("/").length;i++) {
            area+= CommonUtils.getCityInfo(models.get(position).getAreaframe().split("/")[i]);
        }
        area+=models.get(position).getRepairdepot_address();
        holder.adapter_clientsreview_address.setText("地址："+area);
        if (models.get(position).getAppove_status()==1) {
            holder.adapter_clientsreview_state.setText("审核中");
        }
        else if (models.get(position).getAppove_status()==2) {
            holder.adapter_clientsreview_state.setText("已通过");
        }
        else if (models.get(position).getAppove_status()==3) {
            holder.adapter_clientsreview_state.setText("未通过");
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ClientsReviewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_clientsreview_info)
        TextView adapter_clientsreview_info;
        @Bind(R.id.adapter_clientsreview_address)
        TextView adapter_clientsreview_address;
        @Bind(R.id.adapter_clientsreview_state)
        TextView adapter_clientsreview_state;

        public ClientsReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
