package com.renyu.carserver.activity.my;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.adapter.MyStatementAdapter;
import com.renyu.carserver.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by renyu on 15/11/14.
 */
public class MyStatementActivity extends BaseActivity {

    @Bind(R.id.view_toolbar_center_title)
    TextView view_toolbar_center_title;
    @Bind(R.id.view_toolbar_center_back)
    ImageView view_toolbar_center_back;
    @Bind(R.id.popchoice)
    Button popchoice;
    TextView pop_income_statement_all;
    TextView pop_income_statement_service;
    TextView pop_income_statement_commission;
    TextView pop_income_statement_fee;
    TextView pop_income_statement_other;
    @Bind(R.id.myincomestatement_rv)
    RecyclerView myincomestatement_rv;

    MyStatementAdapter adapter=null;

    PopupWindow popupWindow=null;

    int choiceType=1;

    ArrayList<String> models=null;

    @Override
    public int initContentView() {
        return R.layout.activity_mystatement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        models=new ArrayList<>();
        models.add("");
        models.add("");
        models.add("");
        models.add("");
        models.add("");
        models.add("");

        initViews();
    }

    private void initViews() {
        view_toolbar_center_title.setText("收支明细");
        view_toolbar_center_back.setVisibility(View.VISIBLE);
        myincomestatement_rv.setHasFixedSize(true);
        myincomestatement_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MyStatementAdapter(this, models);
        myincomestatement_rv.setAdapter(adapter);
    }

    @OnClick({R.id.popchoice, R.id.view_toolbar_center_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_toolbar_center_back:
                finish();
                break;
            case R.id.popchoice:
                if (popupWindow==null) {
                    View view1= LayoutInflater.from(MyStatementActivity.this).inflate(R.layout.pop_income_statement, null, false);
                    pop_income_statement_all= (TextView) view1.findViewById(R.id.pop_income_statement_all);
                    pop_income_statement_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            choiceType=1;
                            popchoice.setText("全部");
                        }
                    });
                    pop_income_statement_service= (TextView) view1.findViewById(R.id.pop_income_statement_service);
                    pop_income_statement_service.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            choiceType=2;
                            popchoice.setText("服务费");
                        }
                    });
                    pop_income_statement_commission= (TextView) view1.findViewById(R.id.pop_income_statement_commission);
                    pop_income_statement_commission.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            choiceType=3;
                            popchoice.setText("佣金");
                        }
                    });
                    pop_income_statement_fee= (TextView) view1.findViewById(R.id.pop_income_statement_fee);
                    pop_income_statement_fee.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            choiceType=4;
                            popchoice.setText("手续费");
                        }
                    });
                    pop_income_statement_other= (TextView) view1.findViewById(R.id.pop_income_statement_other);
                    pop_income_statement_other.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            choiceType=5;
                            popchoice.setText("其他");
                        }
                    });
                    popupWindow=new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setTouchable(true);
                    popupWindow.setFocusable(true);
                }
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                else {
                    pop_income_statement_all.setVisibility(View.VISIBLE);
                    pop_income_statement_service.setVisibility(View.VISIBLE);
                    pop_income_statement_commission.setVisibility(View.VISIBLE);
                    pop_income_statement_fee.setVisibility(View.VISIBLE);
                    pop_income_statement_other.setVisibility(View.VISIBLE);
                    if (choiceType==-1) {

                    }
                    else if (choiceType==1) {
                        pop_income_statement_all.setVisibility(View.GONE);
                    }
                    else if (choiceType==2) {
                        pop_income_statement_service.setVisibility(View.GONE);
                    }
                    else if (choiceType==3) {
                        pop_income_statement_commission.setVisibility(View.GONE);
                    }
                    else if (choiceType==4) {
                        pop_income_statement_fee.setVisibility(View.GONE);
                    }
                    else if (choiceType==5) {
                        pop_income_statement_other.setVisibility(View.GONE);
                    }
                    popupWindow.showAsDropDown(popchoice, 0, 0);
                }
                break;
        }
    }
}
