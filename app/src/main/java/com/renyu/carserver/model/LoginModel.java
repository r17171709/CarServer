package com.renyu.carserver.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by renyu on 15/11/16.
 */
public class LoginModel implements Parcelable {
    int shop_id=-1;
    int seller_id=-1;
    int role_id=-1;
    String account_name;
    String head_photo;
    String deposit;
    String curMonthIncome;
    String lastMonthIncome;
    String count;
    String sum;
    String contact_person;
    String mobile;
    String areaframe;
    String shop_addr;
    String revenues_code;
    String shopuser_name;
    String shop_name;
    String contact_tel;
    String business_encoding;
    String login_phone;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shop_id);
        dest.writeInt(this.seller_id);
        dest.writeInt(this.role_id);
        dest.writeString(this.account_name);
        dest.writeString(this.head_photo);
        dest.writeString(this.deposit);
        dest.writeString(this.curMonthIncome);
        dest.writeString(this.lastMonthIncome);
        dest.writeString(this.count);
        dest.writeString(this.sum);
        dest.writeString(this.contact_person);
        dest.writeString(this.mobile);
        dest.writeString(this.areaframe);
        dest.writeString(this.shop_addr);
        dest.writeString(this.revenues_code);
        dest.writeString(this.shopuser_name);
        dest.writeString(this.shop_name);
        dest.writeString(this.contact_tel);
        dest.writeString(this.business_encoding);
        dest.writeString(this.login_phone);
    }

    public LoginModel() {
    }

    protected LoginModel(Parcel in) {
        this.shop_id = in.readInt();
        this.seller_id = in.readInt();
        this.role_id = in.readInt();
        this.account_name = in.readString();
        this.head_photo = in.readString();
        this.deposit = in.readString();
        this.curMonthIncome = in.readString();
        this.lastMonthIncome = in.readString();
        this.count = in.readString();
        this.sum = in.readString();
        this.contact_person = in.readString();
        this.mobile = in.readString();
        this.areaframe = in.readString();
        this.shop_addr = in.readString();
        this.revenues_code = in.readString();
        this.shopuser_name = in.readString();
        this.shop_name = in.readString();
        this.contact_tel = in.readString();
        this.business_encoding = in.readString();
        this.login_phone = in.readString();
    }

    public static final Creator<LoginModel> CREATOR = new Creator<LoginModel>() {
        public LoginModel createFromParcel(Parcel source) {
            return new LoginModel(source);
        }

        public LoginModel[] newArray(int size) {
            return new LoginModel[size];
        }
    };

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getHead_photo() {
        return head_photo;
    }

    public void setHead_photo(String head_photo) {
        this.head_photo = head_photo;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getCurMonthIncome() {
        return curMonthIncome;
    }

    public void setCurMonthIncome(String curMonthIncome) {
        this.curMonthIncome = curMonthIncome;
    }

    public String getLastMonthIncome() {
        return lastMonthIncome;
    }

    public void setLastMonthIncome(String lastMonthIncome) {
        this.lastMonthIncome = lastMonthIncome;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAreaframe() {
        return areaframe;
    }

    public void setAreaframe(String areaframe) {
        this.areaframe = areaframe;
    }

    public String getShop_addr() {
        return shop_addr;
    }

    public void setShop_addr(String shop_addr) {
        this.shop_addr = shop_addr;
    }

    public String getRevenues_code() {
        return revenues_code;
    }

    public void setRevenues_code(String revenues_code) {
        this.revenues_code = revenues_code;
    }

    public String getShopuser_name() {
        return shopuser_name;
    }

    public void setShopuser_name(String shopuser_name) {
        this.shopuser_name = shopuser_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getContact_tel() {
        return contact_tel;
    }

    public void setContact_tel(String contact_tel) {
        this.contact_tel = contact_tel;
    }

    public String getBusiness_encoding() {
        return business_encoding;
    }

    public void setBusiness_encoding(String business_encoding) {
        this.business_encoding = business_encoding;
    }

    public String getLogin_phone() {
        return login_phone;
    }

    public void setLogin_phone(String login_phone) {
        this.login_phone = login_phone;
    }
}
