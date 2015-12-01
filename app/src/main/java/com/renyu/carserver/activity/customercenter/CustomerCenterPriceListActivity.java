package com.renyu.carserver.activity.customercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.renyu.carserver.R;
import com.renyu.carserver.adapter.CustomerCenterPriceListLeftAdapter;
import com.renyu.carserver.adapter.CustomerCenterPriceListRightAdapter;
import com.renyu.carserver.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by renyu on 15/11/12.
 */
public class CustomerCenterPriceListActivity extends BaseActivity {

    @Bind(R.id.customercenterpricelist_left_lv)
    RecyclerView customercenterpricelist_left_lv;
    CustomerCenterPriceListLeftAdapter l_adapter = null;
    @Bind(R.id.customercenterpricelist_right_lv)
    RecyclerView customercenterpricelist_right_lv;
    CustomerCenterPriceListRightAdapter r_adapter = null;

    ArrayList<String> lModels = null;
    ArrayList<String> rModels = null;

    @Override
    public int initContentView() {
        return R.layout.activity_customercenterpricelist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lModels = new ArrayList<>();
        lModels.add("1");
        lModels.add("1");
        lModels.add("1");
        lModels.add("1");
        lModels.add("1");
        lModels.add("1");
        lModels.add("1");
        lModels.add("1");
        lModels.add("1");
        rModels = new ArrayList<>();
        rModels.add("1");
        rModels.add("1");
        rModels.add("1");
        rModels.add("1");
        rModels.add("1");
        rModels.add("1");
        rModels.add("1");

        initViews();
    }

    private void initViews() {
        customercenterpricelist_left_lv.setHasFixedSize(true);
        customercenterpricelist_left_lv.setLayoutManager(new LinearLayoutManager(this));
        l_adapter = new CustomerCenterPriceListLeftAdapter(this, lModels, new CustomerCenterPriceListLeftAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                r_adapter.notifyDataSetChanged();
            }
        });
        customercenterpricelist_left_lv.setAdapter(l_adapter);

        customercenterpricelist_right_lv.setHasFixedSize(true);
        customercenterpricelist_right_lv.setLayoutManager(new LinearLayoutManager(this));
        r_adapter = new CustomerCenterPriceListRightAdapter(this, rModels, new CustomerCenterPriceListRightAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(CustomerCenterPriceListActivity.this, ProductDetailActivity.class);
                startActivity(intent);
            }
        });
        customercenterpricelist_right_lv.setAdapter(r_adapter);
    }

}
