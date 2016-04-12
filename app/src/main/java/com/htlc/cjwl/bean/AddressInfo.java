package com.htlc.cjwl.bean;

import java.io.Serializable;

/**
 * Created by Larno on 16/04/04.
 */
public class AddressInfo implements Serializable{

    private String id;

    private String from_address;
    private String from_mobile;
    private String from_name;
    private String from_cityname;
    private String to_cityname;
    private String to_address;
    private String to_mobile;
    private String to_name;

    public String getFrom_cityname() {
        return from_cityname;
    }

    public void setFrom_cityname(String from_cityname) {
        this.from_cityname = from_cityname;
    }

    public String getTo_cityname() {
        return to_cityname;
    }

    public void setTo_cityname(String to_cityname) {
        this.to_cityname = to_cityname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getFrom_mobile() {
        return from_mobile;
    }

    public void setFrom_mobile(String from_mobile) {
        this.from_mobile = from_mobile;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getTo_mobile() {
        return to_mobile;
    }

    public void setTo_mobile(String to_mobile) {
        this.to_mobile = to_mobile;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "id='" + id + '\'' +
                ", from_address='" + from_address + '\'' +
                ", from_mobile='" + from_mobile + '\'' +
                ", from_name='" + from_name + '\'' +
                ", from_cityname='" + from_cityname + '\'' +
                ", to_cityname='" + to_cityname + '\'' +
                ", to_address='" + to_address + '\'' +
                ", to_mobile='" + to_mobile + '\'' +
                ", to_name='" + to_name + '\'' +
                '}';
    }
}
