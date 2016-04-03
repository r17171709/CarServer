package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.workbench.MessageCenterActivity;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.model.MessageModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class MessageCenterAdapter extends RecyclerView.Adapter<MessageCenterAdapter.MessageCenterHolder> {

    ArrayList<MessageModel> models=null;
    Context context=null;

    public MessageCenterAdapter(Context context, ArrayList<MessageModel> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public MessageCenterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_messagecenter, parent, false);
        return new MessageCenterHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageCenterHolder holder, final int position) {
        holder.adapter_messagecenter_title.setText(models.get(position).getTitle());
        holder.adapter_messagecenter_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MessageCenterActivity) context).readMessage(models.get(position).getNotice_id());
                holder.adapter_messagecenter_content.setVisibility(View.VISIBLE);
            }
        });
        holder.adapter_messagecenter_time.setText(CommonUtils.getTimeFormat(models.get(position).getCreate_time()));
        holder.adapter_messagecenter_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MessageCenterActivity) context).deleteMessage(models.get(position).getNotice_id(), position);
            }
        });
        holder.adapter_messagecenter_content.setText(models.get(position).getContent());
        holder.adapter_messagecenter_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapter_messagecenter_content.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MessageCenterHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_messagecenter_title)
        TextView adapter_messagecenter_title;
        @Bind(R.id.adapter_messagecenter_time)
        TextView adapter_messagecenter_time;
        @Bind(R.id.adapter_messagecenter_delete)
        TextView adapter_messagecenter_delete;
        @Bind(R.id.adapter_messagecenter_content)
        TextView adapter_messagecenter_content;

        public MessageCenterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
