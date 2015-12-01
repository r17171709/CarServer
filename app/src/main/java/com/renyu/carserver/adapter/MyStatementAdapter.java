package com.renyu.carserver.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.my.MyStatementDetailActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/14.
 */
public class MyStatementAdapter extends RecyclerView.Adapter<MyStatementAdapter.MyIncomeStatementHolder> {

    Context context=null;
    ArrayList<String> models=null;

    public MyStatementAdapter(Context context, ArrayList<String> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public MyIncomeStatementHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_myincomestatement, parent, false);
        return new MyIncomeStatementHolder(view);
    }

    @Override
    public void onBindViewHolder(MyIncomeStatementHolder holder, int position) {
        holder.adapter_myincomestatement_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MyStatementDetailActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyIncomeStatementHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_myincomestatement_layout)
        LinearLayout adapter_myincomestatement_layout;

        public MyIncomeStatementHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
