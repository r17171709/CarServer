package com.renyu.carserver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.renyu.carserver.R;
import com.renyu.carserver.adapter.AreaAdapter;
import com.renyu.carserver.base.BaseFragment;
import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.model.AreaModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * Created by renyu on 15/11/12.
 */
public class AreaFragment extends BaseFragment {

    public final static int PROVINCE=1;
    public final static int CITY=2;
    public final static int DISTRICT=3;

    int type=0;

    @Bind(R.id.fragment_area_lv)
    ListView fragment_area_lv;
    AreaAdapter adapter=null;

    ArrayList<AreaModel> models=null;

    FragmentControllListener listener=null;
    ChoiceEndListener endListener=null;

    public interface FragmentControllListener {
        void controllfragment(int type, String id);
    }

    public interface ChoiceEndListener {
        void endChoice(String value);
    }

    public static AreaFragment getInstance(int type, String id) {
        AreaFragment fragment=new AreaFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type", type);
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public int initContentView() {
        return R.layout.fragment_area;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener=(FragmentControllListener) context;
        endListener=(ChoiceEndListener) context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type=getArguments().getInt("type");

        models=new ArrayList<>();
        if (type==PROVINCE) {
            models.addAll(CommonUtils.getProvice());
        }
        else if (type==CITY) {
            models.addAll(CommonUtils.getCity(getArguments().getString("id")));
        }
        else if (type==DISTRICT) {
            models.addAll(CommonUtils.getCity(getArguments().getString("id")));
        }

        adapter=new AreaAdapter(models, getActivity());
        fragment_area_lv.setAdapter(adapter);
    }

    @OnItemClick(R.id.fragment_area_lv)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (type==PROVINCE) {
            listener.controllfragment(type, models.get(position).getId());
        }
        else if (type==CITY) {
            if (getArguments().getString("id").equals("310100")||getArguments().getString("id").equals("110100")||getArguments().getString("id").equals("120100")||getArguments().getString("id").equals("500100")) {
                endListener.endChoice(getArguments().getString("id")+","+models.get(position).getId());
            }
            else {
                listener.controllfragment(type, models.get(position).getId());
            }
        }
        else if (type==DISTRICT) {
            endListener.endChoice(CommonUtils.getProvinceId(getArguments().getString("id"))+","+getArguments().getString("id")+","+models.get(position).getId());
        }
    }
}
