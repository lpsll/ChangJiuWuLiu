package com.htlc.cjwl.bean;

import java.util.List;

/**
 * Created by luochuan on 15/11/4.
 */
public class CommonRuteBean {

    private String code;
    private String msg;
    private List<AddressInfo> data;

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

    public List<AddressInfo> getData() {
        return data;
    }

    public void setData(List<AddressInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonRuteBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
