package com.renyu.carserver.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by renyu on 15/11/21.
 */
public class ParentOrderModel implements Serializable {
    String tid;
    String status;
    String pay_time;
    String payment;
    String receiver_state;
    String receiver_city;
    String receiver_district;
    String receiver_address;
    String receiver_zip;
    String receiver_mobile;
    String receiver_name;
    String buyer_message;
    String need_invoice;
    String invoice_name;
    String invoice_type;
    String invoice_main;
    String itemnum;
    String user_memo;
    String created_time;
    String approve_time;
    String consign_time;
    String end_time;
    String modified_time;
    String timeout_action_time;
    String shop_memo;
    String total_fee;
    String receiver_time;
    String message;
    String needpaytime;

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    ArrayList<ChildOrderModel> models;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ChildOrderModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<ChildOrderModel> models) {
        this.models = models;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getReceiver_state() {
        return receiver_state;
    }

    public void setReceiver_state(String receiver_state) {
        this.receiver_state = receiver_state;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getReceiver_district() {
        return receiver_district;
    }

    public void setReceiver_district(String receiver_district) {
        this.receiver_district = receiver_district;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getReceiver_zip() {
        return receiver_zip;
    }

    public void setReceiver_zip(String receiver_zip) {
        this.receiver_zip = receiver_zip;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getBuyer_message() {
        return buyer_message;
    }

    public void setBuyer_message(String buyer_message) {
        this.buyer_message = buyer_message;
    }

    public String getNeed_invoice() {
        return need_invoice;
    }

    public void setNeed_invoice(String need_invoice) {
        this.need_invoice = need_invoice;
    }

    public String getInvoice_name() {
        return invoice_name;
    }

    public void setInvoice_name(String invoice_name) {
        this.invoice_name = invoice_name;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type) {
        this.invoice_type = invoice_type;
    }

    public String getInvoice_main() {
        return invoice_main;
    }

    public void setInvoice_main(String invoice_main) {
        this.invoice_main = invoice_main;
    }

    public String getItemnum() {
        return itemnum;
    }

    public void setItemnum(String itemnum) {
        this.itemnum = itemnum;
    }

    public String getUser_memo() {
        return user_memo;
    }

    public void setUser_memo(String user_memo) {
        this.user_memo = user_memo;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(String approve_time) {
        this.approve_time = approve_time;
    }

    public String getConsign_time() {
        return consign_time;
    }

    public void setConsign_time(String consign_time) {
        this.consign_time = consign_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getModified_time() {
        return modified_time;
    }

    public void setModified_time(String modified_time) {
        this.modified_time = modified_time;
    }

    public String getTimeout_action_time() {
        return timeout_action_time;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public void setTimeout_action_time(String timeout_action_time) {
        this.timeout_action_time = timeout_action_time;

    }

    public String getShop_memo() {
        return shop_memo;
    }

    public void setShop_memo(String shop_memo) {
        this.shop_memo = shop_memo;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getReceiver_time() {
        return receiver_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver_time(String receiver_time) {
        this.receiver_time = receiver_time;

    }

    public String getNeedpaytime() {
        return needpaytime;
    }

    public void setNeedpaytime(String needpaytime) {
        this.needpaytime = needpaytime;
    }
}
