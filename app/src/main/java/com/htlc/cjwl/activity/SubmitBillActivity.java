package com.htlc.cjwl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cjwl.R;

/**
 * Created by sks on 2016/4/7.
 */
public class SubmitBillActivity extends Activity{
    private TextView textTitle,textBillPrice;
    private EditText editBillHeader,editBillType,editBillAddress,editBillReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bill);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("发票");

        textBillPrice = (TextView) findViewById(R.id.textBillPrice);
        editBillHeader = (EditText) findViewById(R.id.editBillHeader);
        editBillType = (EditText) findViewById(R.id.editBillType);
        editBillAddress = (EditText) findViewById(R.id.editBillAddress);
        editBillReceiver = (EditText) findViewById(R.id.editBillReceiver);

        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {

    }
}
