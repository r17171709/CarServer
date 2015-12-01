package com.renyu.carserver.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.adapter.MyFeedbackAdapter;
import com.renyu.carserver.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/14.
 */
public class MyFeedbackActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_layout)
    RelativeLayout view_toolbar_center_layout;
    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.view_toolbar_center_image)
    ImageView view_toolbar_center_image;
    @Bind(R.id.salesnotification_rv)
    RecyclerView salesnotification_rv;
    MyFeedbackAdapter adapter=null;

    ArrayList<String> models=null;

    @Override
    public int initContentView() {
        return R.layout.activity_salesnotification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();
        models.add("1");
        models.add("1");
        models.add("1");
        models.add("1");
        models.add("1");
        models.add("1");
        models.add("1");

        initViews();
    }

    private void initViews() {
        view_toolbar_center_layout.setBackgroundColor(Color.parseColor("#efefef"));
        view_toolbar_center_title.setText("意见反馈");
        view_toolbar_center_title.setTextColor(Color.BLACK);
        view_toolbar_center_image.setImageResource(R.mipmap.logo_red);
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        view_toolbar_center_back.setImageResource(R.mipmap.ic_back_gray);
        salesnotification_rv.setHasFixedSize(true);
        salesnotification_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MyFeedbackAdapter(this, models);
        salesnotification_rv.setAdapter(adapter);
    }

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
        }
    }
}
