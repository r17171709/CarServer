package com.renyu.carserver.model;

/**
 * Created by renyu on 15/11/29.
 */
public class ClientsReviewModel {

    String contact_person;
    String repairdepot_address;
    String repairdepot_name;
    String areaframe;
    String contact_tel;
    int appove_status;

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getRepairdepot_address() {
        return repairdepot_address;
    }

    public void setRepairdepot_address(String repairdepot_address) {
        this.repairdepot_address = repairdepot_address;
    }

    public String getRepairdepot_name() {
        return repairdepot_name;
    }

    public void setRepairdepot_name(String repairdepot_name) {
        this.repairdepot_name = repairdepot_name;
    }

    public String getAreaframe() {
        return areaframe;
    }

    public void setAreaframe(String areaframe) {
        this.areaframe = areaframe;
    }

    public String getContact_tel() {
        return contact_tel;
    }

    public void setContact_tel(String contact_tel) {
        this.contact_tel = contact_tel;
    }

    public int getAppove_status() {
        return appove_status;
    }

    public void setAppove_status(int appove_status) {
        this.appove_status = appove_status;
    }
}
