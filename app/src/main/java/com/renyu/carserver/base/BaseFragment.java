package com.renyu.carserver.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.renyu.carserver.commons.OKHttpHelper;

import butterknife.ButterKnife;

/**
 * Created by renyu on 15/10/18.
 */
public abstract class BaseFragment extends Fragment {

    public OKHttpHelper httpHelper=null;
    View contentView=null;

    ProgressDialog pd=null;

    public abstract int initContentView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView==null) {
            contentView=inflater.inflate(initContentView(), container, false);
            ButterKnife.bind(this, contentView);
        }
        ViewGroup parent= (ViewGroup) contentView.getParent();
        if (parent!=null) {
            parent.removeView(contentView);
        }
        httpHelper=new OKHttpHelper();
        return contentView;
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
    public void showDialog(String title, String message) {
        pd=ProgressDialog.show(getActivity(), title, message);
    }

    public void dismissDialog() {
        if (pd!=null) {
            pd.dismiss();
        }
    }

    /**
     * 关闭键盘
     * @param view
     */
    public void hide(View view) {
        InputMethodManager inputmethodmanager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputmethodmanager != null)
            inputmethodmanager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 打开键盘
     * @param view
     */
    public void show(View view) {
        InputMethodManager inputmethodmanager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputmethodmanager != null)
            inputmethodmanager.showSoftInput(view, 0);
    }
}
