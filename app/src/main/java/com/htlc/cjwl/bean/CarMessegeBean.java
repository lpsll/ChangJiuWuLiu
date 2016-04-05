package com.htlc.cjwl.bean;

/**
 * Created by luochuan on 15/11/4.
 */
public class CarMessegeBean {

    private String code;
    private String msg;
    private CarMessege data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CarMessege getData() {
        return data;
    }

    public void setData(CarMessege data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CarMessegeBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
