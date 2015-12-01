package com.renyu.carserver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.renyu.carserver.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/10/21.
 */
public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ProductDetailHolder> {

    ArrayList<String> models=null;
    Context context=null;

    OnTagChangeListener listener;

    public interface OnTagChangeListener {
        void getTag(String tag);
    }

    public ProductDetailAdapter(Context context, ArrayList<String> models, OnTagChangeListener listener) {
        this.context=context;
        this.models=models;
        this.listener=listener;
    }

    @Override
    public ProductDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_productdetail, parent, false);
        return new ProductDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductDetailHolder holder, final int position) {
        holder.adapter_productdetail_change_edit.setTag("EditText"+position);
        holder.adapter_productdetail_change_text.setTag("TextView" + position);
        holder.adapter_productdetail_change_commit.setTag("change"+position);
        holder.adapter_productdetail_change_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.adapter_productdetail_change_text.getVisibility()==View.VISIBLE) {
                    holder.adapter_productdetail_change_text.setVisibility(View.GONE);
                    holder.adapter_productdetail_change_edit.setVisibility(View.VISIBLE);
                    holder.adapter_productdetail_change_commit.setTextColor(Color.RED);
                    holder.adapter_productdetail_change_commit.setText("确定");
                    listener.getTag(""+position);
                }
                else {
                    holder.adapter_productdetail_change_text.setVisibility(View.VISIBLE);
                    holder.adapter_productdetail_change_edit.setVisibility(View.GONE);
                    holder.adapter_productdetail_change_commit.setTextColor(Color.BLACK);
                    holder.adapter_productdetail_change_commit.setText("改价");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    static class ProductDetailHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.adapter_productdetail_change_edit)
        EditText adapter_productdetail_change_edit;
        @Bind(R.id.adapter_productdetail_change_text)
        TextView adapter_productdetail_change_text;
        @Bind(R.id.adapter_productdetail_change_commit)
        TextView adapter_productdetail_change_commit;

        public ProductDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
