package com.renyu.carserver.model;

/**
 * Created by renyu on 16/3/28.
 */
public class MessageModel {

    /**
     * notice_id : 39
     * from_id : -1
     * to_id : 24
     * type : 2
     * title : [优惠券]您有优惠券可以领取!
     * content : 您可以领取优惠券：123456,该券满100减10,你最多可以领取10张,请于2016/03/20 - 2016/03/22时间内领取,优惠券有效期为2016/03/23 - 2016/03/31,请尽快领取使用,以免失效! <a href='http://192.168.51.67/b2b2c/public/index.php/shopCouponList.html?shop_id=1' target='_blank'> 查看</a>
     * create_time : 1458624167
     * status : 2
     * user_name : null
     */

    private int notice_id;
    private String from_id;
    private String to_id;
    private String type;
    private String title;
    private String content;
    private int create_time;
    private String status;
    private Object user_name;

    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getUser_name() {
        return user_name;
    }

    public void setUser_name(Object user_name) {
        this.user_name = user_name;
    }
}
