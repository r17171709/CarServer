package com.renyu.carserver.activity.customercenter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.adapter.ProductDetailAdapter;
import com.renyu.carserver.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by renyu on 15/10/21.
 */
public class ProductDetailActivity extends BaseActivity {

    @Bind(R.id.productdetail_rv)
    RecyclerView productdetail_rv;

    ProductDetailAdapter adapter=null;

    ArrayList<String> models=null;

    String currentTag="";

    @Override
    public int initContentView() {
        return R.layout.activity_productdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");
        models.add("aa");

        initViews();
    }

    private void initViews() {
        productdetail_rv.setHasFixedSize(true);
        productdetail_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ProductDetailAdapter(this, models, new ProductDetailAdapter.OnTagChangeListener() {
            @Override
            public void getTag(String tag) {
                if (currentTag.equals("")) {
                    currentTag=tag;
                    return;
                }
                if (currentTag.equals(tag)) {
                    currentTag=tag;
                    return;
                }
                productdetail_rv.findViewWithTag("EditText"+currentTag).setVisibility(View.GONE);
                ((EditText) productdetail_rv.findViewWithTag("EditText"+currentTag)).setText("");
                productdetail_rv.findViewWithTag("TextView"+currentTag).setVisibility(View.VISIBLE);
                ((TextView) productdetail_rv.findViewWithTag("change"+currentTag)).setTextColor(Color.BLACK);
                ((TextView) productdetail_rv.findViewWithTag("change"+currentTag)).setText("改价");
                currentTag=tag;
            }
        });
        productdetail_rv.setAdapter(adapter);
        productdetail_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState==RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (!currentTag.equals("")) {
                        recyclerView.findViewWithTag("EditText"+currentTag).setVisibility(View.GONE);
                        ((EditText) recyclerView.findViewWithTag("EditText"+currentTag)).setText("");
                        recyclerView.findViewWithTag("TextView"+currentTag).setVisibility(View.VISIBLE);
                        ((TextView) productdetail_rv.findViewWithTag("change"+currentTag)).setTextColor(Color.BLACK);
                        ((TextView) productdetail_rv.findViewWithTag("change"+currentTag)).setText("改价");
                        currentTag="";
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
