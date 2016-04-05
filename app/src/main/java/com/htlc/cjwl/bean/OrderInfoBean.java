package com.htlc.cjwl.bean;

public class OrderInfoBean {

    public String order_no;
    public String order_date;
    public String order_status;
    public String to_cityname;
    public String to_address;
    public String from_address;
    public String from_cityname;

    @Override
    public String toString() {
        return "OrderInfoBean{" +
                "order_no='" + order_no + '\'' +
                ", order_date='" + order_date + '\'' +
                ", order_status='" + order_status + '\'' +
                ", to_cityname='" + to_cityname + '\'' +
                ", to_address='" + to_address + '\'' +
                ", from_address='" + from_address + '\'' +
                ", from_cityname='" + from_cityname + '\'' +
                '}';
    }
}