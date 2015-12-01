package com.renyu.carserver.activity.workbench;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.renyu.carserver.R;
import com.renyu.carserver.adapter.SearchCreditLineAdapter;
import com.renyu.carserver.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by renyu on 15/11/12.
 */
public class SearchCreditLineActivity extends BaseActivity {

    @Bind(R.id.searchcreateline_rv)
    RecyclerView searchcreateline_rv;
    SearchCreditLineAdapter adapter=null;

    ArrayList<String> models=null;

    @Override
    public int initContentView() {
        return R.layout.activity_searchcreditline;
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
        searchcreateline_rv.setHasFixedSize(true);
        searchcreateline_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SearchCreditLineAdapter(this, models);
        searchcreateline_rv.setAdapter(adapter);
    }
}
