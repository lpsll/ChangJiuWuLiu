package com.htlc.cjwl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import util.ToastUtil;

import core.ActionCallbackListener;
import model.OrderDetailBean;

/**
 * Created by sks on 2016/4/7.
 */
public class OrderDetailActivity extends Activity{
    public static final String OrderId = "OrderId";

    private String orderId;

    private TextView textTitle, textFromAddress, textToAddress,
            textFromTel,textToTel,textFromName,textToName,
            textCarTypeNameArray, textCarNumArray, textPrice,
            textInsurancePrice;
    private TextView textOrderID;
    private TextView textFromCity;
    private TextView textToCity;
    private TextView textOrderTime;
    private TextView textComment;
    private TextView textHall;
    private final String html = "<font color=\"#3c3c3c\">评价:  </font><font color=\"#acacac\">%1$s</font>" ;
    private final String htmlHall = "<font color=\"#acacac\">网点地址:  </font><font color=\"#acacac\">%1$s</font>" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderId = getIntent().getStringExtra(OrderId);
        Log.e("OrderDetail", "getIntent orderId:"+orderId);
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
        textTitle.setText("订单详情");

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

        textOrderID = (TextView) findViewById(R.id.order_no);
        textFromCity = (TextView) findViewById(R.id.order_departure);
        textToCity = (TextView) findViewById(R.id.order_destination);
        textOrderTime = (TextView) findViewById(R.id.order_date);

        textComment = (TextView) findViewById(R.id.textComment);
        textHall = (TextView) findViewById(R.id.textHall);
        initData();
    }

    private void initData() {
        App.appAction.orderDetail(orderId, new ActionCallbackListener<OrderDetailBean>() {
            @Override
            public void onSuccess(OrderDetailBean data) {
                refreshView(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void refreshView(OrderDetailBean data) {
        if(TextUtils.isEmpty(data.from_address)){
            textFromAddress.setText(data.from_cityname);
            textFromCity.setText(data.from_cityname);
        }else {
            textFromAddress.setText(data.from_address);
            textFromCity.setText(data.from_address);
        }
        if(TextUtils.isEmpty(data.to_address)){
            textToAddress.setText(data.to_cityname);
            textToCity.setText(data.to_cityname);
        }else {
            textToAddress.setText(data.to_address);
            textToCity.setText(data.to_address);
        }
        textFromTel.setText(data.from_mobile);
        textToTel.setText(data.to_mobile);
        textFromName.setText(data.from_name);
        textToName.setText(data.to_name);
        StringBuilder carTypeNameArrayStr = new StringBuilder();
        StringBuilder carNumArrayStr = new StringBuilder();
        double insurancePrice = 0;
        for (int i=0; i<data.order_carfnum.size(); i++){
            OrderDetailBean.OrderCarfNum orderCarfNum = data.order_carfnum.get(i);
            carTypeNameArrayStr.append(orderCarfNum.carname+"  ");
            carNumArrayStr.append(orderCarfNum.num+"  ");
            insurancePrice += Double.parseDouble(orderCarfNum.insureprice);
        }
        textCarTypeNameArray.setText(carTypeNameArrayStr.toString());
        textCarNumArray.setText(carNumArrayStr.toString());
        textInsurancePrice.setText("￥"+insurancePrice);
        textPrice.setText("￥"+data.order_price);

        textOrderID.setText(data.order_no);
        textOrderTime.setText(data.order_date);

        if(!TextUtils.isEmpty(data.evaluate)){
            textComment.setVisibility(View.VISIBLE);
            String comment = String.format(html, data.evaluate);
            textComment.setText(Html.fromHtml(comment));
        }
        if(!TextUtils.isEmpty(data.cityaddress)){
            textHall.setVisibility(View.VISIBLE);
            String comment = String.format(htmlHall, data.cityaddress);
            textHall.setText(Html.fromHtml(comment));
        }

    }
}
