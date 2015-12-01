package com.renyu.carserver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.renyu.carserver.R;
import com.renyu.carserver.activity.login.LoginActivity;
import com.renyu.carserver.activity.my.MyDataActivity;
import com.renyu.carserver.activity.my.MyFeedbackActivity;
import com.renyu.carserver.activity.my.MySaveActivity;
import com.renyu.carserver.activity.my.MyStatementActivity;
import com.renyu.carserver.base.BaseFragment;
import com.renyu.carserver.commons.ParamUtils;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by renyu on 15/10/20.
 */
public class MyFragment extends BaseFragment {

    @Bind(R.id.my_avatar)
    CircleImageView my_avatar;
    @Bind(R.id.my_name)
    TextView my_name;
    @Bind(R.id.my_curmonthincome)
    TextView my_curmonthincome;
    @Bind(R.id.my_deposit)
    TextView my_deposit;
    @Bind(R.id.my_receive_num)
    TextView my_receive_num;

    @Override
    public int initContentView() {
        return R.layout.fragment_my;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews() {
        my_name.setText(ParamUtils.getLoginModel(getActivity()).getAccount_name());
        my_curmonthincome.setText(ParamUtils.getLoginModel(getActivity()).getCurMonthIncome());
        my_deposit.setText(ParamUtils.getLoginModel(getActivity()).getDeposit());
        my_receive_num.setText(ParamUtils.getLoginModel(getActivity()).getLastMonthIncome());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ParamUtils.getLoginModel(getActivity()).getHead_photo().indexOf(Environment.getExternalStorageDirectory().getPath())!=-1) {
            ImageLoader.getInstance().displayImage("file://"+ParamUtils.getLoginModel(getActivity()).getHead_photo(), my_avatar, getAvatarDisplayImageOptions());
        }
        else {
            ImageLoader.getInstance().displayImage(ParamUtils.getLoginModel(getActivity()).getHead_photo(), my_avatar, getAvatarDisplayImageOptions());
        }
    }

    @OnClick({R.id.my_income_save_layout, R.id.my_income_statement_layout, R.id.my_income_feedback_layout, R.id.my_income_data_layout, R.id.myincomedata_loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_income_save_layout:
                Intent intent_save=new Intent(getActivity(), MySaveActivity.class);
                startActivity(intent_save);
                break;
            case R.id.my_income_statement_layout:
                Intent intent_statement=new Intent(getActivity(), MyStatementActivity.class);
                startActivity(intent_statement);
                break;
            case R.id.my_income_feedback_layout:
                Intent intent_feedback=new Intent(getActivity(), MyFeedbackActivity.class);
                startActivity(intent_feedback);
                break;
            case R.id.my_income_data_layout:
                Intent intent_data=new Intent(getActivity(), MyDataActivity.class);
                startActivity(intent_data);
                break;
            case R.id.myincomedata_loginout:
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("type", ParamUtils.NONEEDFINISH);
                intent.putExtras(bundle);
                startActivity(intent);
                ParamUtils.removeAllLoginInfo(getActivity());
        }
    }

    public DisplayImageOptions getAvatarDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
}
