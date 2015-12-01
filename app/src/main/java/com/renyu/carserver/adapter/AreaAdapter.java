package com.renyu.carserver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.model.AreaModel;

import java.util.ArrayList;

/**
 * Created by renyu on 15/11/12.
 */
public class AreaAdapter extends BaseAdapter {

    ArrayList<AreaModel> models=null;
    Context context=null;

    public AreaAdapter(ArrayList<AreaModel> models, Context context) {
        this.models=models;
        this.context=context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AreaAdapterHolder holder=null;
        if (convertView==null) {
            holder=new AreaAdapterHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_area, parent, false);
            holder.adapter_area_name= (TextView) convertView.findViewById(R.id.adapter_area_name);
            convertView.setTag(holder);
        }
        else {
            holder= (AreaAdapterHolder) convertView.getTag();
        }
        holder.adapter_area_name.setText(models.get(position).getValue());
        return convertView;
    }

    public class AreaAdapterHolder {
        TextView adapter_area_name;
    }
}
