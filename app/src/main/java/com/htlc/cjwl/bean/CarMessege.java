package com.htlc.cjwl.bean;

import java.util.List;

/**
 * Created by luochuan on 15/11/4.
 */
public class CarMessege {
/**
 * 我要运车--地址及车辆信息接口http://t2.damaimob.com/Home/CYTransport/transportCarInfo?user_loginID=1
 */

    private AddressInfo addressInfo;
    private List<CarInfo> carList;

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public List<CarInfo> getCarList() {
        return carList;
    }

    public void setCarList(List<CarInfo> carList) {
        this.carList = carList;
    }
}
