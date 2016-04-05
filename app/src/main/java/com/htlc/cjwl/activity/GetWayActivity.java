package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.htlc.cjwl.R;
import com.htlc.cjwl.bean.CarSendWay;


/**
 * Created by luochuan on 15/11/13.
 */
public class GetWayActivity extends Activity {
    private String address;//地址信息
    private Intent intent;
    private Intent intent2;
    private int tag = 1;//发车方式标签
    private CarSendWay carSendWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_way);
    }
}
