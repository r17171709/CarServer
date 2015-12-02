package com.renyu.carserver.commons;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.renyu.carserver.model.LoginModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by renyu on 15/10/17.
 */
public class ParamUtils {

    public final static String DIR= Environment.getExternalStorageDirectory().getPath()+File.separator+"carserver";
    public final static String IMAGECACHE=DIR+ File.separator+"cache";
    public final static String DB=DIR+File.separator+"db";

    public static String api="http://120.26.139.82/b2b2c/public/index.php/api";

    public final static int RESULT_AREA=10000;

    public final static int FINISH=1;
    public final static int NONEEDFINISH=2;
    public final static int takecamera_result=10003;
    public final static int choicePic_result=10008;
    public final static int crop_result=10021;

    public static boolean isOpen=false;
    public static String weibo_appkey="1104982206";
    public static String qq_appkey="1104982206";

    private static String getSign(String method, String token) {
        return CommonUtils.MD5(CommonUtils.MD5(method).toUpperCase() + token).toUpperCase();
    }

    public static HashMap<String, String> getSignParams(String method, String token) {
        HashMap<String, String> params=new HashMap<>();
        params.put("method", method);
        params.put("timestamp", ""+System.currentTimeMillis()/1000);
        params.put("format", "json");
        params.put("v", "v1");
        params.put("sign_type", "MD5");
        params.put("sign", getSign(method, token));
        return params;
    }

    /**
     * 保存登录信息
     * @param context
     * @param name
     * @param password
     */
    public static void saveLogin(Context context, String name, String password) {
        SharedPreferences sp=context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("name", name);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * 获取登录信息
     * @param context
     * @return
     */
    public static HashMap<String, String> getLogin(Context context) {
        HashMap<String, String> map=new HashMap<String, String>();
        SharedPreferences sp=context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        map.put("name", sp.getString("name", ""));
        map.put("password", sp.getString("password", ""));
        return map;
    }

    /**
     * 保存登录结果
     * @param context
     * @param model
     */
    public static void setLoginModel(Context context, LoginModel model) {
        SharedPreferences sp=context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("role_id", model.getRole_id());
        editor.putInt("seller_id", model.getSeller_id());
        editor.putInt("shop_id", model.getShop_id());
        editor.putString("account_name", model.getAccount_name());
        editor.putString("head_photo", model.getHead_photo());
        editor.putString("deposit", model.getDeposit());
        editor.putString("curMonthIncome", model.getCurMonthIncome());
        editor.putString("lastMonthIncome", model.getLastMonthIncome());
        editor.putString("count", model.getCount());
        editor.putString("sum", model.getSum());
        editor.putString("login_phone", model.getLogin_phone());
        editor.commit();
    }

    public static void setHeadPhoto(Context context, String head_photo) {
        SharedPreferences sp=context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("head_photo", head_photo);
        editor.commit();
    }

    /**
     * 获取登录结果
     * @param context
     * @return
     */
    public static LoginModel getLoginModel(Context context) {
        SharedPreferences sp=context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        LoginModel model=new LoginModel();
        model.setShop_id(sp.getInt("shop_id", -1));
        model.setSeller_id(sp.getInt("seller_id", -1));
        model.setRole_id(sp.getInt("role_id", -1));
        model.setAccount_name(sp.getString("account_name", ""));
        model.setHead_photo(sp.getString("head_photo", ""));
        model.setDeposit(sp.getString("deposit", ""));
        model.setCurMonthIncome(sp.getString("curMonthIncome", ""));
        model.setLastMonthIncome(sp.getString("lastMonthIncome", ""));
        model.setCount(sp.getString("count", ""));
        model.setSum(sp.getString("sum", ""));
        model.setLogin_phone(sp.getString("login_phone", ""));
        return model;
    }

    /**
     * 注销
     * @param context
     */
    public static void removeAllLoginInfo(Context context) {
        SharedPreferences sp=context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String converNull(String value) {
        return (value == null || value.equals("null") ? "" : value);
    }

    public static int converInt(String value) {
        return (value == null || value.equals("null") ? 0 : Integer.parseInt(value));
    }

    public static long converLong(String value) {
        return (value == null || value.equals("null") ? 0 : Long.parseLong(value));
    }

    public static String getFormatTime(long time) {
        if (time==0) {
            return "";
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getFormatTime2(long time) {
        if (time==0) {
            return "";
        }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time));
    }
}
