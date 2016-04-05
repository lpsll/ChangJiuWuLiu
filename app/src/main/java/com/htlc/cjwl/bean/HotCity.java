package com.htlc.cjwl.bean;

/**
 * Created by luochuan on 15/11/5.
 */
public class HotCity {
    private String id;
    private String cityname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    @Override
    public String toString() {
        return "HotCity{" +
                "id='" + id + '\'' +
                ", cityname='" + cityname + '\'' +
                '}';
    }
}
