package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2016/4/7.
 */
public class SubmitBillActivity extends Activity{
    public static final String TotalPrice = "TotalPrice";
    public static final String OrderArrayStr = "OrderArrayStr";
    public static String orderArrayStr;
    public static String totalPrice;
    private TextView textTitle,textBillPrice;
    private EditText editBillHeader,editBillAddress,editBillReceiver,editBillReceiverTel;
    private TextView editBillType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bill);
        Intent intent = getIntent();

        totalPrice = intent.getStringExtra(TotalPrice);
        orderArrayStr = intent.getStringExtra(OrderArrayStr);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("发票");

        textBillPrice = (TextView) findViewById(R.id.textBillPrice);
        textBillPrice.setText(totalPrice+"");
        editBillHeader = (EditText) findViewById(R.id.editBillHeader);
        editBillType = (TextView) findViewById(R.id.editBillType);
        editBillAddress = (EditText) findViewById(R.id.editBillAddress);
        editBillReceiver = (EditText) findViewById(R.id.editBillReceiver);
        editBillReceiverTel = (EditText) findViewById(R.id.editBillReceiverTel);

        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        String billHeader = editBillHeader.getText().toString().trim();
        String billType = "运费";
        String address = editBillAddress.getText().toString().trim();
        String billReceiver = editBillReceiver.getText().toString().trim();
        String phone = editBillReceiverTel.getText().toString().trim();

        App.appAction.submitBillOrder(billHeader, totalPrice + "", billType, address, billReceiver, orderArrayStr, phone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"申请发票成功！");
                finish();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }
}
