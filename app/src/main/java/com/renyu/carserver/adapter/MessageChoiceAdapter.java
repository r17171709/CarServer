package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.workbench.MessageChoiceActivity;
import com.renyu.carserver.model.CustomerModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 16/3/29.
 */
public class MessageChoiceAdapter extends RecyclerView.Adapter<MessageChoiceAdapter.MessageSendHolder> {

    Context context;
    ArrayList<CustomerModel> models=null;

    public MessageChoiceAdapter(Context context, ArrayList<CustomerModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public MessageSendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_messagechoice, parent, false);
        return new MessageSendHolder(view);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public void onBindViewHolder(MessageSendHolder holder, final int position) {
        holder.adapter_messagechoice_title.setText(models.get(position).getRepairdepot_name());
        holder.adapter_messagechoice_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MessageChoiceActivity) context).getChoice(models.get(position).getUser_id(), models.get(position).getRepairdepot_name());
            }
        });
    }

    public static class MessageSendHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_messagechoice_title)
        TextView adapter_messagechoice_title;

        public MessageSendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
