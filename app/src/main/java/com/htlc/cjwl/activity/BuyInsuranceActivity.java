package com.htlc.cjwl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.htlc.cjwl.R;


/**
 * Created by luochuan on 15/11/5.
 * 投保界面
 * 暂时未使用 程序扩展用
 */
public class BuyInsuranceActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_insurance);
        //ButterKnife.bind(this);
        TextView title = (TextView) findViewById(R.id.tv_activity_title);
        title.setText("投保");
    }


}
