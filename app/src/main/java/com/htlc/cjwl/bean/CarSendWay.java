package com.htlc.cjwl.bean;

import java.util.List;

/**
 * Created by luochuan on 15/11/13.
 */
public class CarSendWay {
    private List<Method> method;
    private String address;

    public List<Method> getMethod() {
        return method;
    }

    public void setMethod(List<Method> method) {
        this.method = method;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CarSendWay{" +
                "method=" + method +
                ", address='" + address + '\'' +
                '}';
    }
}
