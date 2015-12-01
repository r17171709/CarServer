package com.renyu.carserver;

import android.app.Application;

import com.renyu.carserver.commons.CommonUtils;
import com.renyu.carserver.commons.ParamUtils;

/**
 * Created by renyu on 15/10/17.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParamUtils.isOpen=true;

        CommonUtils.loadDir();

        CommonUtils.initImageLoader(getApplicationContext());

        CommonUtils.copyAssetsFile("area.db", ParamUtils.DB, getApplicationContext());
    }
}
