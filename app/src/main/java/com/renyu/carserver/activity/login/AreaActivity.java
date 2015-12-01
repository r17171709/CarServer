package com.renyu.carserver.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.fragment.AreaFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/12.
 */
public class AreaActivity extends BaseActivity implements AreaFragment.FragmentControllListener, AreaFragment.ChoiceEndListener {

    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    AreaFragment province=null;

    @Override
    public int initContentView() {
        return R.layout.activity_area;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();

        province=AreaFragment.getInstance(AreaFragment.PROVINCE, "-1");
        addFragment(province, "province");

    }

    private void initViews() {
        view_toolbar_center_title.setText("所在地区");
        view_toolbar_center_back.setVisibility(View.VISIBLE);
    }

    private void addFragment(AreaFragment fragment, String tag) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.area_layout, fragment, tag);
        if (!tag.equals("province"))
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void controllfragment(int type, String id) {
        if (type==AreaFragment.PROVINCE) {
            AreaFragment city=AreaFragment.getInstance(AreaFragment.CITY, id);
            addFragment(city, "city");
        }
        else if (type==AreaFragment.CITY) {
            AreaFragment district=AreaFragment.getInstance(AreaFragment.DISTRICT, id);
            addFragment(district, "district");
        }
    }

    @Override
    public void endChoice(String value) {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("value", value);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
        Log.d("AreaActivity", value);
    }

    @OnClick({R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
        }
    }
}
