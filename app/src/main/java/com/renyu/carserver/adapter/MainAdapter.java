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
 * Created by renyu on 15/10/20.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    ArrayList<String> strings=null;
    Context context=null;

    public MainAdapter(Context context, ArrayList<String> strings) {
        this.strings=strings;
        this.context=context;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_main, parent, false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
        holder.adapter_main_title.setText(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    static class MainHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_main_title)
        TextView adapter_main_title;
        @Bind(R.id.adapter_main_time)
        TextView adapter_main_time;

        public MainHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
