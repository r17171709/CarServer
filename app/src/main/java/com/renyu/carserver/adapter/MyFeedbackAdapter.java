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
 * Created by renyu on 15/11/14.
 */
public class MyFeedbackAdapter extends RecyclerView.Adapter<MyFeedbackAdapter.MyIncomeFeedbackAdapterHolder> {

    ArrayList<String> models=null;
    Context context=null;

    public MyFeedbackAdapter(Context context, ArrayList<String> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public MyIncomeFeedbackAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_salesnotification, parent, false);
        return new MyIncomeFeedbackAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(MyIncomeFeedbackAdapterHolder holder, int position) {
        holder.salesnotification_oper.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyIncomeFeedbackAdapterHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.salesnotification_oper)
        TextView salesnotification_oper;

        public MyIncomeFeedbackAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
