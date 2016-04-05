package com.htlc.cjwl.bean;

/**
 * Created by luochuan on 15/11/5.
 */
public class SetOutCityBean {

    private String code;
    private String msg;
    private HotListCity data;

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

    public HotListCity getData() {
        return data;
    }

    public void setData(HotListCity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SetOutCityBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
