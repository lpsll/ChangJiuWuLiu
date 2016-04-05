package com.htlc.cjwl.bean;

/**
 * Created by luochuan on 15/11/14.
 */
public class Car {
    private String name;
    private int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "&" +
                "carsinfo[][name]=" + name +
                "&carsinfo[][num]=" + num ;
    }
}
