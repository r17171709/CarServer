package com.renyu.carserver.model;

/**
 * Created by renyu on 15/12/1.
 */
public class SearchCreditLineModel {

    private String result;
    private int serviceid;
    private String current;
    private String remark;
    private String apply;
    private String status;
    private int aid;
    private int user_id;
    private int applytime;
    private int approvetime;
    private String repairdepot_name;

    public void setResult(String result) {
        this.result = result;
    }

    public void setServiceid(int serviceid) {
        this.serviceid = serviceid;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setApplytime(int applytime) {
        this.applytime = applytime;
    }

    public void setApprovetime(int approvetime) {
        this.approvetime = approvetime;
    }

    public String getResult() {
        return result;
    }

    public int getServiceid() {
        return serviceid;
    }

    public String getCurrent() {
        return current;
    }

    public String getRemark() {
        return remark;
    }

    public String getApply() {
        return apply;
    }

    public String getStatus() {
        return status;
    }

    public int getAid() {
        return aid;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getApplytime() {
        return applytime;
    }

    public int getApprovetime() {
        return approvetime;
    }

    public String getRepairdepot_name() {
        return repairdepot_name;
    }

    public void setRepairdepot_name(String repairdepot_name) {
        this.repairdepot_name = repairdepot_name;
    }
}
