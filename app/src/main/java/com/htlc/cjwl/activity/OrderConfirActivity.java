package com.htlc.cjwl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.AddressInfo;

/**
 * Created by luochuan on 15/11/3.
 */
public class OrderConfirActivity extends Activity {
    @Nullable
    int od_count;
    @Nullable
    int st_count;
    @Nullable
    int big_count;
    @Nullable
    int ot_count;
    private String code;//网络请求状态码

    private String carInfo;//订单 轿车数量信息
    private String id;//用户登录ID
    private String insure;//保单价格
    private AddressInfo addressInfo;//用于接收传入的地址及联系人信息
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
    }
}
