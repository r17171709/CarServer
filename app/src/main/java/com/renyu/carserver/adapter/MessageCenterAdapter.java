package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.carserver.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class MessageCenterAdapter extends RecyclerView.Adapter<MessageCenterAdapter.MessageCenterHolder> {

    ArrayList<String> models=null;
    Context context=null;

    public MessageCenterAdapter(Context context, ArrayList<String> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public MessageCenterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_messagecenter, parent, false);
        return new MessageCenterHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageCenterHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MessageCenterHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_messagecenter_image)
        ImageView adapter_messagecenter_image;
        @Bind(R.id.adapter_messagecenter_title)
        TextView adapter_messagecenter_title;
        @Bind(R.id.adapter_messagecenter_time)
        TextView adapter_messagecenter_time;

        public MessageCenterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
