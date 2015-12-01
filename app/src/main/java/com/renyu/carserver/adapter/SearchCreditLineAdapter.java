package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renyu.carserver.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class SearchCreditLineAdapter extends RecyclerView.Adapter {

    ArrayList<String> models=null;
    Context context=null;

    public SearchCreditLineAdapter(Context context, ArrayList<String> models) {
        this.context=context;
        this.models=models;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_searchcreditlinechild, parent, false);
        return new SearchCreditLineChildHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class SearchCreditLineChildHolder extends RecyclerView.ViewHolder {
        public SearchCreditLineChildHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
