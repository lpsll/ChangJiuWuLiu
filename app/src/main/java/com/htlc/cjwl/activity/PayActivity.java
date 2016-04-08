package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.R;

import java.util.ArrayList;

import model.CarInfoBean;

/**
 * Created by sks on 2016/4/7.
 */
public class PayActivity extends Activity implements View.OnClickListener {
    public static final String PayDetail = "PayDetail";
    private TextView textTitle, textFromAddress, textToAddress,
            textFromTel,textToTel,textFromName,textToName,
            textCarTypeNameArray, textCarNumArray, textPrice,
            textInsurancePrice, textScore;
    private EditText editFromUsername, editFromTel, editFromUserCard,
            editToUsername, editToTel, editToUserCard;
    private LinearLayout linearPayContainer;
    private CheckBox checkBox;

    private ArrayList<String> payArray;
    private int selectPayWay = 0;
    private String html = "<font color=\"#3c3c3c\">本次可用积分</font>" +
            "<font color=\"#ff0000\">%1$s</font>" +
            "<font color=\"#3c3c3c\">分，可抵</font>" +
            "<font color=\"#ff0000\">%2$s</font>" +
            "<font color=\"#3c3c3c\">元</font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        getIntent().getStringExtra(PayDetail);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("支付");
        textFromAddress = (TextView) findViewById(R.id.tv_from_address);
        textToAddress = (TextView) findViewById(R.id.tv_to_address);
        textFromTel = (TextView) findViewById(R.id.tv_from_tel);
        textToTel = (TextView) findViewById(R.id.tv_to_tel);
        textFromName = (TextView) findViewById(R.id.tv_from_name);
        textToName = (TextView) findViewById(R.id.tv_to_name);

        textCarTypeNameArray = (TextView) findViewById(R.id.textCarTypeNameArray);
        textCarNumArray = (TextView) findViewById(R.id.textCarNumArray);
        textPrice = (TextView) findViewById(R.id.textPrice);
        textInsurancePrice = (TextView) findViewById(R.id.textInsurancePrice);
        textScore = (TextView) findViewById(R.id.textScore);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        linearPayContainer = (LinearLayout) findViewById(R.id.linearPayContainer);
        initLinearCarCardContainer();
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });
        initData();
    }

    private void initData() {
        html = String.format(html, "1000", "10");
        textScore.setText(Html.fromHtml(html));
    }

    private void initLinearCarCardContainer() {
        payArray = new ArrayList<>();
        payArray.add("支付宝");
        payArray.add("微信");
        payArray.add("银联");
        for (int i = 0; i < payArray.size(); i++) {
            LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.layout_pay, null);
            linearLayout.setTag(i);
            linearLayout.setOnClickListener(this);
            if (selectPayWay == i) {
                CheckBox checkBox = (CheckBox) linearLayout.findViewById(R.id.checkBox);
                checkBox.setChecked(true);
            }
            linearPayContainer.addView(linearLayout);
        }
    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        Intent intent = new Intent(this, PayActivity.class);
    }

    @Override
    public void onClick(View v) {
        CheckBox preCheckBox = (CheckBox) linearPayContainer.getChildAt(selectPayWay).findViewById(R.id.checkBox);
        preCheckBox.setChecked(false);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        checkBox.setChecked(true);
        selectPayWay = linearPayContainer.indexOfChild(v);
    }
}
