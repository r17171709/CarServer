package com.renyu.carserver.model;

import com.renyu.carserver.commons.ParamUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by renyu on 15/11/16.
 */
public class JsonParse {

    public static int getResultInt(String string) {
        try {
            JSONObject object=new JSONObject(string);
            return object.getInt("errorcode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String getErrorValue(String string) {
        try {
            JSONObject object=new JSONObject(string);
            JSONObject error=object.getJSONObject("error");
            return error.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "未知错误";
    }

    public static String getResultValue(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                return result.getString("result_info");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 获取登录信息
     * @param string
     * @return
     */
    public static Object getLoginModel(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                int type=result.getInt("type");
                if (type==0) {
                    JSONObject data=result.getJSONObject("data");
                    LoginModel model=new LoginModel();
                    model.setRole_id(data.getInt("role_id"));
                    model.setSeller_id(data.getInt("seller_id"));
                    model.setShop_id(data.getInt("shop_id"));
                    model.setHead_photo(data.getString("head_photo"));
                    model.setDeposit(data.getString("deposit"));
                    model.setAccount_name(data.getString("account_name"));
                    model.setSum(data.getString("sum"));
                    model.setCount(data.getString("count"));
                    model.setLastMonthIncome(data.getString("lastMonthIncome"));
                    model.setCurMonthIncome(data.getString("curMonthIncome"));
                    model.setLogin_phone(data.getString("login_phone"));
                    return model;
                }
                else if (type==2) {
                    return result.getString("info");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 获取客户中心修理厂列表
     * @param string
     * @return
     */
    public static Object getCustomerModel(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONArray data2=data1.getJSONArray("data");
                ArrayList<CustomerModel> models=new ArrayList<>();
                for (int i=0;i<data2.length();i++) {
                    JSONObject jsonObject=data2.getJSONObject(i);
                    CustomerModel model=new CustomerModel();
                    model.setRepairdepot_name(ParamUtils.converNull(jsonObject.getString("repairdepot_name")));
                    model.setUser_id(jsonObject.getInt("user_id"));
                    model.setContact_person(jsonObject.getString("contact_person"));
                    model.setCorporation(ParamUtils.converNull(jsonObject.getString("corporation")));
                    model.setCorporation_codeId_photo(ParamUtils.converNull(jsonObject.getString("corporation_codeId_photo")));
                    model.setShop_photo(ParamUtils.converNull(jsonObject.getString("shop_photo")));
                    model.setCorporation_codeId(ParamUtils.converNull(jsonObject.getString("corporation_codeId")));
                    model.setLogin_account(ParamUtils.converNull(jsonObject.getString("login_account")));
                    model.setAccount_name(ParamUtils.converNull(jsonObject.getString("account_name")));
                    model.setAreaframe(ParamUtils.converNull(jsonObject.getString("areaframe")));
                    model.setRepairdepot_address(ParamUtils.converNull(jsonObject.getString("repairdepot_address")));
                    model.setCorporation(ParamUtils.converNull(jsonObject.getString("corporation")));
                    model.setMobile(ParamUtils.converNull(jsonObject.getString("mobile")));
                    model.setBusiness_encoding(ParamUtils.converNull(jsonObject.getString("business_encoding")));
                    model.setBusiness_photo(ParamUtils.converNull(jsonObject.getString("business_photo")));
                    model.setShop_photo_new(ParamUtils.converNull(jsonObject.getString("shop_photo_new")));
                    model.setRevenues_code(ParamUtils.converNull(jsonObject.getString("revenues_code")));
                    model.setBank_name(ParamUtils.converNull(jsonObject.getString("bank_name")));
                    model.setBank_account(ParamUtils.converNull(jsonObject.getString("bank_account")));
                    model.setContact_phone(jsonObject.getString("contact_phone"));
                    models.add(model);
                }
                return models;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 获取订单列表
     * @param string
     * @return
     */
    public static Object getOrderModel(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONArray data2=data1.getJSONArray("data");
                ArrayList<ParentOrderModel> models=new ArrayList<>();
                for (int i=0;i<data2.length();i++) {
                    ParentOrderModel pmodel=new ParentOrderModel();
                    pmodel.setStatus(data2.getJSONObject(i).getString("status"));
                    pmodel.setTid(data2.getJSONObject(i).getString("tid"));
                    pmodel.setPayment(data2.getJSONObject(i).getString("payment"));
                    pmodel.setReceiver_state(data2.getJSONObject(i).getString("receiver_state"));
                    pmodel.setReceiver_city(data2.getJSONObject(i).getString("receiver_city"));
                    pmodel.setReceiver_district(data2.getJSONObject(i).getString("receiver_district"));
                    pmodel.setReceiver_address(data2.getJSONObject(i).getString("receiver_address"));
                    pmodel.setReceiver_zip(data2.getJSONObject(i).getString("receiver_zip"));
                    pmodel.setReceiver_mobile(data2.getJSONObject(i).getString("receiver_mobile"));
                    pmodel.setBuyer_message(data2.getJSONObject(i).getString("buyer_message"));
                    pmodel.setNeed_invoice(data2.getJSONObject(i).getString("need_invoice"));
                    pmodel.setInvoice_name(data2.getJSONObject(i).getString("invoice_name"));
                    pmodel.setInvoice_type(data2.getJSONObject(i).getString("invoice_type"));
                    pmodel.setInvoice_main(data2.getJSONObject(i).getString("invoice_main"));
                    pmodel.setItemnum(data2.getJSONObject(i).getString("itemnum"));
                    pmodel.setUser_memo(data2.getJSONObject(i).getString("user_memo"));
                    pmodel.setCreated_time(ParamUtils.converNull(data2.getJSONObject(i).getString("created_time")));
                    pmodel.setApprove_time(ParamUtils.converNull(data2.getJSONObject(i).getString("approve_time")));
                    pmodel.setConsign_time(ParamUtils.converNull(data2.getJSONObject(i).getString("consign_time")));
                    pmodel.setEnd_time(ParamUtils.converNull(data2.getJSONObject(i).getString("end_time")));
                    pmodel.setTimeout_action_time(ParamUtils.converNull(data2.getJSONObject(i).getString("timeout_action_time")));
                    pmodel.setReceiver_name(data2.getJSONObject(i).getString("receiver_name"));
                    pmodel.setShop_memo(ParamUtils.converNull(data2.getJSONObject(i).getString("shop_memo")));
                    pmodel.setPay_time(ParamUtils.converNull(data2.getJSONObject(i).getString("pay_time")));
                    pmodel.setTotal_fee(ParamUtils.converNull(data2.getJSONObject(i).getString("total_fee")));
                    pmodel.setReceiver_time(ParamUtils.converNull(data2.getJSONObject(i).getString("receiver_time")));
                    pmodel.setMessage(ParamUtils.converNull(data2.getJSONObject(i).getString("message")));
                    pmodel.setNeedpaytime(ParamUtils.converNull(data2.getJSONObject(i).getString("needpaytime")));
                    ArrayList<ChildOrderModel> cModels=new ArrayList<>();
                    JSONArray order=new JSONArray(data2.getJSONObject(i).getString("order"));
                    for (int j=0;j<order.length();j++) {
                        JSONObject jsonObject=order.getJSONObject(j);
                        ChildOrderModel cmodel=new ChildOrderModel();
                        cmodel.setTid(jsonObject.getString("tid"));
                        cmodel.setNum(jsonObject.getString("num"));
                        cmodel.setOid(jsonObject.getString("oid"));
                        cmodel.setOld_price(jsonObject.getString("old_price"));
                        cmodel.setSettle_price(jsonObject.getString("settle_price"));
                        cmodel.setPrice(jsonObject.getString("price"));
                        cmodel.setSpec_nature_info(ParamUtils.converNull(jsonObject.getString("spec_nature_info")));
                        cmodel.setTitle(jsonObject.getString("title"));
                        cmodel.setPic_path(jsonObject.getString("pic_path"));
                        cmodel.setAftersales_status(jsonObject.getString("aftersales_status"));
                        cModels.add(cmodel);
                    }
                    pmodel.setModels(cModels);
                    models.add(pmodel);
                }
                return models;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 获取授权额度列表
     * @param string
     * @return
     */
    public static Object getCreditLineModel(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONArray data2=data1.getJSONArray("data");
                ArrayList<CreditLineModel> models=new ArrayList<>();
                for (int i=0;i<data2.length();i++) {
                    JSONObject jsonObject=data2.getJSONObject(i);
                    CreditLineModel model=new CreditLineModel();
                    model.setAmount(jsonObject.getInt("amount"));
                    model.setInit_amount(jsonObject.getInt("init_amount"));
                    model.setRepairdepot_name(jsonObject.getString("repairdepot_name"));
                    model.setUser_id(jsonObject.getInt("user_id"));
                    models.add(model);
                }
                return models;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 获取售后通知
     * @param string
     * @return
     */
    public static Object getSalesNotificationModel(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONArray data2=data1.getJSONArray("data");
                ArrayList<SalesNotificationModel> models=new ArrayList<>();
                for (int i=0;i<data2.length();i++) {
                    JSONObject jsonObject=data2.getJSONObject(i);
                    SalesNotificationModel model=new SalesNotificationModel();
                    model.setRepairdepot_name(jsonObject.getString("repairdepot_name"));
                    model.setAftersales_bn(jsonObject.getInt("aftersales_bn"));
                    model.setContents(jsonObject.getString("contents"));
                    model.setCre_time(jsonObject.getInt("cre_time"));
                    model.setService_id(jsonObject.getInt("service_id"));
                    model.setUser_id(jsonObject.getInt("user_id"));
                    model.setUser_photo(jsonObject.getString("user_photo"));
                    models.add(model);
                }
                return models;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 修理厂申请列表
     * @param string
     * @return
     */
    public static Object getFactoryApplyModel(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONArray data2=data1.getJSONArray("data");
                ArrayList<FactoryApplyModel> models=new ArrayList<>();
                for (int i=0;i<data2.length();i++) {
                    JSONObject jsonObject=data2.getJSONObject(i);
                    FactoryApplyModel model=new FactoryApplyModel();
                    model.setAddrs(jsonObject.getString("addrs"));
                    model.setArea(jsonObject.getString("area"));
                    model.setAreaframe(jsonObject.getString("areaframe"));
                    model.setBusiness_encoding(jsonObject.getString("business_encoding"));
                    model.setBusiness_photo(jsonObject.getString("business_photo"));
                    model.setContact_person(jsonObject.getString("contact_person"));
                    model.setContact_tel(jsonObject.getString("contact_tel"));
                    model.setDesc(jsonObject.getString("desc"));
                    model.setEparchy(jsonObject.getString("eparchy"));
                    model.setRepair_name(jsonObject.getString("repair_name"));
                    model.setShop_id(ParamUtils.converNull(jsonObject.getString("shop_id")));
                    model.setStatus(jsonObject.getInt("status"));
                    model.setUser_id(jsonObject.getString("user_id"));
                    model.setUser_identity(jsonObject.getString("user_identity"));
                    model.setUser_name(jsonObject.getString("user_name"));
                    models.add(model);
                }
                return models;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 订单改价
     * @param string
     * @return
     */
    public static int getPriceRemind(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                return result.getInt("result_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 我的资料
     * @param string
     * @return
     */
    public static Object getMyData(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                LoginModel model=new LoginModel();
                model.setHead_photo(data1.getString("head_photo"));
                model.setShop_name(data1.getString("shop_name"));
                model.setContact_person(data1.getString("contact_person"));
                model.setMobile(data1.getString("mobile"));
                model.setAreaframe(data1.getString("areaframe"));
                model.setShop_addr(data1.getString("shop_addr"));
                model.setShopuser_name(data1.getString("shopuser_name"));
                model.setRevenues_code(data1.getString("revenues_code"));
                model.setContact_tel(data1.getString("contact_tel"));
                model.setBusiness_encoding(data1.getString("business_encoding"));
                return model;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 获取订单数量
     * @param string
     * @return
     */
    public static HashMap<String, String> getOrderNum(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONObject data2=data1.getJSONObject("data");
                HashMap<String, String> map=new HashMap<>();
                map.put("TRADE_FINISHED", data2.getString("TRADE_FINISHED"));
                map.put("TRADE_CANCEL", data2.getString("TRADE_CANCEL"));
                map.put("WAIT_APPROVE", data2.getString("WAIT_APPROVE"));
                map.put("DELIVER_GOODS", data2.getString("DELIVER_GOODS"));
                map.put("WAIT_GOODS", data2.getString("WAIT_GOODS"));
                map.put("TRADE_CLOSED", data2.getString("TRADE_CLOSED"));
                map.put("WAIT_CONFRIM", data2.getString("WAIT_CONFRIM"));
                map.put("RECEIVE_GOODS", data2.getString("RECEIVE_GOODS"));
                map.put("AFTERSALES", data2.getString("AFTERSALES"));
                return map;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    /**
     * 新增客户状态审核列表
     * @param string
     * @return
     */
    public static Object getClientsReviewModels(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONArray data2=data1.getJSONArray("data");
                ArrayList<ClientsReviewModel> models=new ArrayList<>();
                for (int i = 0; i < data2.length(); i++) {
                    JSONObject jsonObject=data2.getJSONObject(i);
                    ClientsReviewModel model=new ClientsReviewModel();
                    model.setAreaframe(jsonObject.getString("areaframe"));
                    model.setContact_person(jsonObject.getString("contact_person"));
                    model.setContact_tel(jsonObject.getString("contact_tel"));
                    model.setRepairdepot_address(jsonObject.getString("repairdepot_address"));
                    model.setRepairdepot_name(jsonObject.getString("repairdepot_name"));
                    model.setAppove_status(jsonObject.getInt("appove_status"));
                    model.setUser_id(jsonObject.getString("user_id"));
                    models.add(model);
                }
                return models;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }

    /**
     * 授信额度变更列表
     * @param string
     * @return
     */
    public static Object getSearchCreditLineModels(String string) {
        if (getResultInt(string)==0) {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject result=object.getJSONObject("result");
                JSONObject data1=result.getJSONObject("data");
                JSONArray data2=data1.getJSONArray("data");
                ArrayList<SearchCreditLineModel> models=new ArrayList<>();
                for (int i=0;i<data2.length();i++) {
                    JSONObject jsonObject=data2.getJSONObject(i);
                    SearchCreditLineModel model=new SearchCreditLineModel();
                    model.setCurrent(jsonObject.getString("current"));
                    model.setApply(jsonObject.getString("apply"));
                    model.setResult(jsonObject.getString("result"));
                    model.setStatus(jsonObject.getString("status"));
                    model.setUser_id(jsonObject.getInt("user_id"));
                    model.setRepairdepot_name(jsonObject.getString("repairdepot_name"));
                    models.add(model);
                }
                return models;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            return getErrorValue(string);
        }
    }
}
