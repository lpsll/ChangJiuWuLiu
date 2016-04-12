package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.R;

import java.util.ArrayList;

import model.CarInfoBean;

/**
 * Created by Larno on 16/04/07.
 */
public class OrderConfirmActivity extends Activity {
    public static final String OrderDetail = "OrderDetail";
    private TextView textTitle,textFromAddress,textToAddress,
            textCarTypeNameArray,textCarNumArray,textPrice,
            textInsurancePrice;
    private EditText editFromUsername,editFromTel,editFromUserCard,
            editToUsername,editToTel,editToUserCard;
    private LinearLayout linearCarCardContainer;
    private ArrayList<CarInfoBean> carArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        getIntent().getStringExtra(OrderDetail);
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
        textTitle.setText("订单确认");
        textFromAddress = (TextView) findViewById(R.id.textFromAddress);
        textToAddress = (TextView) findViewById(R.id.textToAddress);
        textCarTypeNameArray = (TextView) findViewById(R.id.textCarTypeNameArray);
        textCarNumArray = (TextView) findViewById(R.id.textCarNumArray);
        textPrice = (TextView) findViewById(R.id.textPrice);
        textInsurancePrice = (TextView) findViewById(R.id.textInsurancePrice);

        editFromUsername = (EditText) findViewById(R.id.editFromUsername);
        editFromTel = (EditText) findViewById(R.id.editFromTel);
        editFromUserCard = (EditText) findViewById(R.id.editFromUserCard);

        editToUsername = (EditText) findViewById(R.id.editToUsername);
        editToTel = (EditText) findViewById(R.id.editToTel);
        editToUserCard = (EditText) findViewById(R.id.editToUserCard);

        linearCarCardContainer = (LinearLayout) findViewById(R.id.linearCarCardContainer);
        initLinearCarCardContainer();
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });
    }

    private void initLinearCarCardContainer() {
        carArray = new ArrayList<>();
        carArray.add(new CarInfoBean());
        carArray.add(new CarInfoBean());
        carArray.add(new CarInfoBean());
        carArray.add(new CarInfoBean());
        carArray.add(new CarInfoBean());
        for (int i = 0; i < carArray.size(); i++) {
            Log.e("Car", carArray.get(i).name + ";num=" + carArray.get(i).num);
            LinearLayout linearLayout = (LinearLayout) View.inflate(this,R.layout.layout_car_card,null);
            linearLayout.setTag(i);
            linearCarCardContainer.addView(linearLayout);
        }
    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        Intent intent = new Intent(this,PayActivity.class);
        intent.putExtra(PayActivity.PayDetail,"");
        startActivity(intent);
    }

}
