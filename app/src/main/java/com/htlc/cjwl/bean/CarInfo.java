package com.htlc.cjwl.bean;

/**
 * Created by luochuan on 15/11/4.
 */
public class CarInfo {

    private String id;
    private String car_name;
    private String car_desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_desc() {
        return car_desc;
    }

    public void setCar_desc(String car_desc) {
        this.car_desc = car_desc;
    }

    @Override
    public String toString() {
        return "CarInfo{" +
                "id='" + id + '\'' +
                ", car_name='" + car_name + '\'' +
                ", car_desc='" + car_desc + '\'' +
                '}';
    }
}
