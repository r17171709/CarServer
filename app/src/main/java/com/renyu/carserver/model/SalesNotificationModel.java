package com.renyu.carserver.model;

/**
 * Created by renyu on 15/11/25.
 */
public class SalesNotificationModel {
    String contents;
    String repairdepot_name;
    int user_id;
    int service_id;
    int aftersales_bn;
    int cre_time;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getRepairdepot_name() {
        return repairdepot_name;
    }

    public void setRepairdepot_name(String repairdepot_name) {
        this.repairdepot_name = repairdepot_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getAftersales_bn() {
        return aftersales_bn;
    }

    public void setAftersales_bn(int aftersales_bn) {
        this.aftersales_bn = aftersales_bn;
    }

    public int getCre_time() {
        return cre_time;
    }

    public void setCre_time(int cre_time) {
        this.cre_time = cre_time;
    }
}
