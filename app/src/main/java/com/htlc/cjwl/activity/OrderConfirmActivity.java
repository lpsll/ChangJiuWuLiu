package com.htlc.cjwl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import core.ActionCallbackListener;
import model.CarInfoBean;
import model.InsuranceInfoBean;
import model.VinInfoBean;

/**
 * Created by Larno on 16/04/07.
 */
public class OrderConfirmActivity extends Activity {
    public static final String CarArray = "CarArray";

    public static final String FromCity = "FromCity";
    public static final String ToCity = "ToCity";
    public static final String FromCityDetail = "FromCityDetail";
    public static final String ToCityDetail = "ToCityDetail";
    public static final String FromTel = "FromTel";
    public static final String ToTel = "ToTel";
    public static final String FromName = "FromName";
    public static final String ToName = "ToName";

    public static final String OrderPrice = "OrderPrice";
    public static final String OrderInsure = "OrderInsure";

    private TextView textTitle,textFromAddress,textToAddress,
            textCarTypeNameArray,textCarNumArray,textPrice,
            textInsurancePrice;
    private EditText editFromUsername,editFromTel,editFromUserCard,
            editToUsername,editToTel,editToUserCard;
    private LinearLayout linearCarCardContainer;


    private ArrayList<CarInfoBean> carArray;
    public  String fromCity = "";
    public  String toCity = "";
    public  String fromCityDetail = "";
    public  String toCityDetail = "";
    public  String fromTel = "";
    public  String toTel = "";
    public  String fromName = "";
    public  String toName = "";

    public  String orderPrice = "";
    public  ArrayList<InsuranceInfoBean> orderInsure;
    public  ArrayList<VinInfoBean> vinnumArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        carArray = getIntent().getParcelableArrayListExtra(CarArray);

        fromCity = getIntent().getStringExtra(FromCity);
        toCity = getIntent().getStringExtra(ToCity);
        fromCityDetail = getIntent().getStringExtra(FromCityDetail);
        toCityDetail = getIntent().getStringExtra(ToCityDetail);
        fromTel = getIntent().getStringExtra(FromTel);
        toTel = getIntent().getStringExtra(ToTel);
        fromName = getIntent().getStringExtra(FromName);
        toName = getIntent().getStringExtra(ToName);

        orderPrice = getIntent().getStringExtra(OrderPrice);
        orderInsure = getIntent().getParcelableArrayListExtra(OrderInsure);
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
        if(TextUtils.isEmpty(fromCityDetail)){
            textFromAddress.setText(fromCity);
        }else {
            textFromAddress.setText(fromCityDetail);
        }
        textToAddress = (TextView) findViewById(R.id.textToAddress);
        if(TextUtils.isEmpty(toCityDetail)){
            textToAddress.setText(toCity);
        }else {
            textToAddress.setText(toCityDetail);
        }
        textCarTypeNameArray = (TextView) findViewById(R.id.textCarTypeNameArray);
        textCarNumArray = (TextView) findViewById(R.id.textCarNumArray);
        StringBuilder carName = new StringBuilder();
        StringBuilder carNum = new StringBuilder();
        for(int i=0; i<carArray.size(); i++){
            CarInfoBean bean = carArray.get(i);
            carName.append(bean.name+"  ");
            carNum.append(bean.num+"辆  ");
        }
        textCarTypeNameArray.setText(carName.toString());
        textCarNumArray.setText(carNum.toString());

        textPrice = (TextView) findViewById(R.id.textPrice);
        textInsurancePrice = (TextView) findViewById(R.id.textInsurancePrice);
        textPrice.setText("￥"+orderPrice);
        float totalInsure = 0;
        for(InsuranceInfoBean bean : orderInsure){
            totalInsure += Float.parseFloat(bean.insurePrice);
        }
        textInsurancePrice.setText("￥"+totalInsure);

        editFromUsername = (EditText) findViewById(R.id.editFromUsername);
        editFromTel = (EditText) findViewById(R.id.editFromTel);
        editFromUserCard = (EditText) findViewById(R.id.editFromUserCard);
        editFromUsername.setText(fromName);
        editFromTel.setText(fromTel);

        editToUsername = (EditText) findViewById(R.id.editToUsername);
        editToTel = (EditText) findViewById(R.id.editToTel);
        editToUserCard = (EditText) findViewById(R.id.editToUserCard);
        editToUsername.setText(toName);
        editToTel.setText(toTel);

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
        vinnumArray = new ArrayList<>();
        for (int i = 0; i < carArray.size(); i++) {
            CarInfoBean bean = carArray.get(i);
            String num = bean.num;
            for(int j=0; j<Integer.parseInt(num); j++){
                LinearLayout linearLayout = (LinearLayout) View.inflate(this,R.layout.layout_car_card,null);
                EditText editCarCard = (EditText) linearLayout.findViewById(R.id.editCarCard);
                editCarCard.setHint("请输入第"+(j+1)+"辆"+bean.name+"车架号");
                linearCarCardContainer.addView(linearLayout);

                VinInfoBean vinInfoBean = new VinInfoBean();
                vinInfoBean.id = bean.id;
                vinnumArray.add(vinInfoBean);
            }
        }
    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        String fromIdCard = editFromUserCard.getText().toString().trim();
        String toIdCard = editToUserCard.getText().toString().trim();
        for(int i=0; i<linearCarCardContainer.getChildCount(); i++){
            EditText editCarCard = (EditText) linearCarCardContainer.getChildAt(i).findViewById(R.id.editCarCard);
            String vinnum = editCarCard.getText().toString().trim();
            if(TextUtils.isEmpty(vinnum)){
                ToastUtil.showToast(App.app,"请输入车架号");
                return;
            }
            vinnumArray.get(i).vinnumId = vinnum;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "请稍等...",true);
        App.appAction.orderCreate(fromCity, fromCityDetail, toCity, toCityDetail, fromName, toName, fromTel, toTel, fromIdCard, toIdCard,
                vinnumArray, carArray, orderPrice, orderInsure, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtil.showToast(App.app,"订单提交成功");
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtil.showToast(App.app,message);
            }
        });
//        Intent intent = new Intent(this,PayActivity.class);
//        intent.putExtra(PayActivity.PayDetail,"");
//        startActivity(intent);
    }

}
