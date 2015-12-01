package com.renyu.carserver.model;

/**
 * Created by renyu on 15/11/25.
 */
public class CreditLineModel {
    int init_amount=0;
    int amount=0;
    int user_id=0;
    String repairdepot_name="";

    public int getInit_amount() {
        return init_amount;
    }

    public void setInit_amount(int init_amount) {
        this.init_amount = init_amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRepairdepot_name() {
        return repairdepot_name;
    }

    public void setRepairdepot_name(String repairdepot_name) {
        this.repairdepot_name = repairdepot_name;
    }
}
