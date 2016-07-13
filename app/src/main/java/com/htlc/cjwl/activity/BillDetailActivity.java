package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;

import core.ActionCallbackListener;
import model.BillDetailBean;
import util.ToastUtil;

/**
 * Created by sks on 2016/4/7.
 * 发票详情（申请发票）
 */
public class BillDetailActivity extends Activity {
    public static final String BillId = "BillId";
    public static final String BillStatus = "BillStatus";
    private TextView textTitle, textBillPrice;//标题，发票总额
    private EditText editBillHeader, editBillAddress, editBillReceiver, editBillReceiverTel;//发票抬头，发票地址，发票收获人，发票收货人电话；
    private TextView editBillType;//发票类型

    private String billId;//发票id
    private boolean billStatus;//是否可修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        billId = getIntent().getStringExtra(BillId);
        billStatus = getIntent().getBooleanExtra(BillStatus, false);


        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("发票详情");

        textBillPrice = (TextView) findViewById(R.id.textBillPrice);
        editBillHeader = (EditText) findViewById(R.id.editBillHeader);
        editBillType = (TextView) findViewById(R.id.editBillType);
        editBillAddress = (EditText) findViewById(R.id.editBillAddress);
        editBillReceiver = (EditText) findViewById(R.id.editBillReceiver);
        editBillReceiverTel = (EditText) findViewById(R.id.editBillReceiverTel);

        View button = findViewById(R.id.tv_sure);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        if (billStatus) {
            button.setVisibility(View.VISIBLE);
        } else {
            textBillPrice.setEnabled(false);
            editBillHeader.setEnabled(false);
            editBillType.setEnabled(false);
            editBillAddress.setEnabled(false);
            editBillReceiver.setEnabled(false);
            editBillReceiverTel.setEnabled(false);
        }

        initData();
    }

    /*获取发票信息*/
    private void initData() {
        App.appAction.billOrderDetail(billId, new ActionCallbackListener<BillDetailBean>() {
            @Override
            public void onSuccess(BillDetailBean data) {
                textBillPrice.setText(data.invoice_price);
                editBillHeader.setText(data.invoice_buyer);
                editBillAddress.setText(data.invoice_address);
                editBillReceiver.setText(data.invoice_taker);
                editBillReceiverTel.setText(data.invoice_taker);
                editBillReceiverTel.setText(data.invoice_phone);
            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });
    }

    /*提交发票信息*/
    private void submit() {
        String billHeader = editBillHeader.getText().toString().trim();
        String address = editBillAddress.getText().toString().trim();
        String billReceiver = editBillReceiver.getText().toString().trim();
        String phone = editBillReceiverTel.getText().toString().trim();

        App.appAction.billOrderModify(billId, billHeader, address, billReceiver, phone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "申请发票成功！");
                finish();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
