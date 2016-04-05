package com.htlc.cjwl.activity;

import android.app.Activity;
import android.os.Bundle;

import com.htlc.cjwl.R;


/**
 * Created by luochuan on 15/11/3.
 * 请求参数
 *
 *
 * /**
 * user_loginID----当前登录用户id
 * from_address---出发地地址
 * from_mobile----发货人手机号
 * from_name------发货人姓名
 * to_address-----目的地地址
 * to_mobile------收货人手机号
 * to_name--------收货人姓名
 */

public class AddAdressActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
    }

}
