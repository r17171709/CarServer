package com.renyu.carserver.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/14.
 */
public class MyStatementDetailActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;

    @Override
    public int initContentView() {
        return R.layout.activity_mystatementdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        view_toolbar_center_title.setText("收支详情");
        view_toolbar_center_back.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
        }
    }

}
