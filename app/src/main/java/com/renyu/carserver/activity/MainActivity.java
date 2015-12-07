package com.renyu.carserver.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.carserver.R;
import com.renyu.carserver.activity.login.LoginActivity;
import com.renyu.carserver.base.BaseActivity;
import com.renyu.carserver.commons.ParamUtils;
import com.renyu.carserver.fragment.CustomerCenterFragment;
import com.renyu.carserver.fragment.MainFragment;
import com.renyu.carserver.fragment.MyFragment;
import com.renyu.carserver.fragment.OrderCenterFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_image1)
    ImageView main_image1;
    @Bind(R.id.main_text1)
    TextView main_text1;
    @Bind(R.id.main_image2)
    ImageView main_image2;
    @Bind(R.id.main_text2)
    TextView main_text2;
    @Bind(R.id.main_image3)
    ImageView main_image3;
    @Bind(R.id.main_text3)
    TextView main_text3;
    @Bind(R.id.main_image4)
    ImageView main_image4;
    @Bind(R.id.main_text4)
    TextView main_text4;
    @Bind(R.id.main_image)
    ImageView main_image;

    MainFragment mainFragment=null;
    OrderCenterFragment orderCenterFragment=null;
    CustomerCenterFragment customerCenterFragment=null;
    MyFragment myFragment=null;

    //当前选中的title
    String currentTitle="one";
    //当前选中的fragment副本
    Fragment currentFragment=null;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras().getBoolean("isNeedAnimation")) {
            setTheme(R.style.LoginTheme);
        }

        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {
        if (getIntent().getExtras().getBoolean("isNeedAnimation")) {
            splashAnimation();
        }
        //隐藏全部缓存fragment
        List<Fragment> fragmentList=getSupportFragmentManager().getFragments();
        if (fragmentList!=null) {
            for (int i=0;i<fragmentList.size();i++) {
                Fragment fragment=fragmentList.get(i);
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
        }
        if (savedInstanceState!=null) {
            currentTitle=savedInstanceState.getString("title");
        }
        switchFragment(currentTitle);
    }

    private void switchFragment(String title) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        if (currentFragment!=null) {
            transaction.hide(currentFragment);
        }
        Fragment toFragment = getSupportFragmentManager().findFragmentByTag(title);
        if (toFragment==null) {
            if (title.equals("one")) {
                mainFragment=new MainFragment();
                transaction.add(R.id.main_fl, mainFragment, "one");
                currentFragment=mainFragment;
            }
            else if (title.equals("two")) {
                orderCenterFragment=new OrderCenterFragment();
                transaction.add(R.id.main_fl, orderCenterFragment, "two");
                currentFragment=orderCenterFragment;
            }
            else if (title.equals("three")) {
                customerCenterFragment=new CustomerCenterFragment();
                transaction.add(R.id.main_fl, customerCenterFragment, "three");
                currentFragment=customerCenterFragment;
            }
            else if (title.equals("four")) {
                myFragment=new MyFragment();
                transaction.add(R.id.main_fl, myFragment, "four");
                currentFragment=myFragment;
            }
        }
        else {
            transaction.show(toFragment);
            currentFragment=toFragment;
        }
        transaction.commit();

        main_image1.setImageResource(R.mipmap.tab_icon1);
        main_text1.setTextColor(Color.WHITE);
        main_image2.setImageResource(R.mipmap.tab_icon2);
        main_text2.setTextColor(Color.WHITE);
        main_image3.setImageResource(R.mipmap.tab_icon3);
        main_text3.setTextColor(Color.WHITE);
        main_image4.setImageResource(R.mipmap.tab_icon4);
        main_text4.setTextColor(Color.WHITE);
        switch (title) {
            case "one":
                main_image1.setImageResource(R.mipmap.tab_icon1_selected);
                main_text1.setTextColor(Color.parseColor("#c90001"));
                break;
            case "two":
                main_image2.setImageResource(R.mipmap.tab_icon2_selected);
                main_text2.setTextColor(Color.parseColor("#c90001"));
                break;
            case "three":
                main_image3.setImageResource(R.mipmap.tab_icon3_selected);
                main_text3.setTextColor(Color.parseColor("#c90001"));
                break;
            case "four":
                main_image4.setImageResource(R.mipmap.tab_icon4_selected);
                main_text4.setTextColor(Color.parseColor("#c90001"));
                break;
        }

        currentTitle=title;
    }

    @OnClick({R.id.main_main, R.id.main_ordercenter, R.id.main_customercenter, R.id.main_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_main:
                switchFragment("one");
                break;
            case R.id.main_ordercenter:
                switchFragment("two");
                break;
            case R.id.main_customercenter:
                switchFragment("three");
                break;
            case R.id.main_my:
                switchFragment("four");
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("title", currentTitle);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("type", ParamUtils.FINISH);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        customerCenterFragment.onActivityResult(requestCode, resultCode, data);
    }

    private void splashAnimation() {
        ValueAnimator valueAnimator=ValueAnimator.ofInt(1000, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                main_image.setScaleX(1+animation.getAnimatedFraction()/2);
                main_image.setScaleY(1+animation.getAnimatedFraction()/2);
                main_image.setAlpha(1-animation.getAnimatedFraction());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                main_image.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                main_image.setImageResource(R.mipmap.splash_bg);
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }
}
