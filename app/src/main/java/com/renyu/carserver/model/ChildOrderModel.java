package com.renyu.carserver.model;

import java.io.Serializable;

/**
 * Created by renyu on 15/11/21.
 */
public class ChildOrderModel implements Serializable {
    String oid;
    String tid;
    String title;
    String spec_nature_info;
    String old_price;
    String settle_price;
    String price;
    String num;
    String pic_path;
    String aftersales_status;
    double edit_price=0;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpec_nature_info() {
        return spec_nature_info;
    }

    public void setSpec_nature_info(String spec_nature_info) {
        this.spec_nature_info = spec_nature_info;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getSettle_price() {
        return settle_price;
    }

    public void setSettle_price(String settle_price) {
        this.settle_price = settle_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getAftersales_status() {
        return aftersales_status;
    }

    public void setAftersales_status(String aftersales_status) {
        this.aftersales_status = aftersales_status;
    }

    public double getEdit_price() {
        return edit_price;
    }

    public void setEdit_price(double edit_price) {
        this.edit_price = edit_price;
    }
}
