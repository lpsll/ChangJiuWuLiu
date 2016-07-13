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
 * 订单详情
 */
public class OrderDetailActivity extends Activity{
    public static final String OrderId = "OrderId";

    private String orderId;//订单id

    //标题，出发地，目的地，发货人电话，收货人电话，发货人姓名，收货人姓名，汽车型号数组，汽车数量数组，总价，投保价
    private TextView textTitle, textFromAddress, textToAddress,
            textFromTel,textToTel,textFromName,textToName,
            textCarTypeNameArray, textCarNumArray, textPrice,
            textInsurancePrice;
    private TextView textOrderID;//订单id
    private TextView textFromCity;//出发地
    private TextView textToCity;//目的地
    private TextView textOrderTime;//订单创建时间
    private TextView textComment;//评论
    private TextView textHall;//网点地址
    private final String html = "<font color=\"#3c3c3c\">评价:  </font><font color=\"#acacac\">%1$s</font>" ;
    private final String htmlHall = "<font color=\"#acacac\">网点地址:  </font><font color=\"#acacac\">%1$s</font>" ;
    private TextView textDaoDaTime;//预计到达时间


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
        textDaoDaTime = (TextView) findViewById(R.id.textDaoDaTime);
        textHall = (TextView) findViewById(R.id.textHall);
        initData();
    }

    /*获取订单详细数据*/
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

    /*刷新界面*/
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
        textDaoDaTime.setText(getString(R.string.arrival_time, data.daodadate));
        if(!TextUtils.isEmpty(data.cityaddress)){
            textHall.setVisibility(View.VISIBLE);
            String comment = String.format(htmlHall, data.cityaddress);
            textHall.setText(Html.fromHtml(comment));
        }

    }
}
