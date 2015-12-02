package com.renyu.carserver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.model.SearchCreditLineModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by renyu on 15/11/12.
 */
public class SearchCreditLineAdapter extends RecyclerView.Adapter<SearchCreditLineAdapter.SearchCreditLineChildHolder> {

    ArrayList<SearchCreditLineModel> models=null;
    Context context=null;

    ReCommitListener listener=null;

    public SearchCreditLineAdapter(Context context, ArrayList<SearchCreditLineModel> models, ReCommitListener listener) {
        this.context=context;
        this.models=models;
        this.listener=listener;
    }

    @Override
    public SearchCreditLineAdapter.SearchCreditLineChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_searchcreditlinechild, parent, false);
        return new SearchCreditLineChildHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchCreditLineChildHolder holder, final int position) {
        holder.searchcreditlinechild_name.setText(models.get(position).getRepairdepot_name());
        holder.searchcreditlinechild_before.setText(models.get(position).getCurrent());
        holder.searchcreditlinechild_after.setText(models.get(position).getApply());
        if (models.get(position).getStatus().equals("0")) {
            holder.searchcreditlinechild_finish.setText("待审核");
            holder.searchcreditlinechild_finish.setVisibility(View.VISIBLE);
            holder.searchcreditlinechild_reject.setVisibility(View.GONE);
        }
        else {
            if (models.get(position).getResult().equals("SUCC")) {
                holder.searchcreditlinechild_finish.setText("已通过");
                holder.searchcreditlinechild_finish.setVisibility(View.VISIBLE);
                holder.searchcreditlinechild_reject.setVisibility(View.GONE);
            }
            else if (models.get(position).getResult().equals("UNSUCC")) {
                holder.searchcreditlinechild_finish.setVisibility(View.GONE);
                holder.searchcreditlinechild_reject.setVisibility(View.VISIBLE);
            }
        }
        holder.searchcreditlinechild_recommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.commitPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class SearchCreditLineChildHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.searchcreditlinechild_name)
        TextView searchcreditlinechild_name;
        @Bind(R.id.searchcreditlinechild_before)
        TextView searchcreditlinechild_before;
        @Bind(R.id.searchcreditlinechild_after)
        TextView searchcreditlinechild_after;
        @Bind(R.id.searchcreditlinechild_finish)
        TextView searchcreditlinechild_finish;
        @Bind(R.id.searchcreditlinechild_reject)
        LinearLayout searchcreditlinechild_reject;
        @Bind(R.id.searchcreditlinechild_recommit)
        TextView searchcreditlinechild_recommit;

        public SearchCreditLineChildHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ReCommitListener {
        void commitPosition(int position);
    }
}
