package com.htlc.cjwl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.SwipeCarAdapter;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.ToastUtil;

import java.util.ArrayList;

import api.Api;
import core.ActionCallbackListener;
import model.AddressInfoBean;
import model.CalculatePriceInfoBean;
import model.CarInfoBean;
import model.InsuranceInfoBean;

/**
 * Created by Larno on 16/04/05.
 */
public class OrderInfoActivity extends Activity implements View.OnClickListener {
    public static final int RequestCode_SelectFromAddress = 100;
    public static final int RequestCode_SelectToAddress = 200;
    public static final int RequestCode_SendWay = 300;
    public static final int RequestCode_GetWay = 400;
    public static final int RequestCode_SelectCarType = 500;
    public static final int RequestCode_SelectCarNum = 600;
    private TextView textTitle;
    private LinearLayout linearFromAddress, linearToAddress,
            linearSelectCarType, linearCarNum, linearSendCarWay,
            linearGetCarWay,linearCarInsurance;
    private ListView swipeListViewCarContainer;
    private SwipeCarAdapter swipeCarAdapter;
    private TextView textFromAddress, textToAddress, textCarNum,
            textSendCarWay, textGetCarWay, textPrice;
    private CheckBox checkBox;
    private TextView textButton;

    private boolean state = false;
    private String fromCityID, toCityID, fromCityDetail = "", toCityDetail = "",
            fromName = "", toName = "", fromTel = "", toTel = "",
            sendWayID = Api.TransportWayArray[1], getWayID = Api.TransportWayArray[1],
            orderPrice = "0.0";
    private ArrayList<CarInfoBean> carArray = new ArrayList<>();
    private ArrayList<InsuranceInfoBean> insuranceArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("订单信息");

        linearFromAddress = (LinearLayout) findViewById(R.id.ll_from_address);
        linearToAddress = (LinearLayout) findViewById(R.id.ll_to_address);
        linearFromAddress.setOnClickListener(this);
        linearToAddress.setOnClickListener(this);
        textFromAddress = (TextView) findViewById(R.id.tv_from_address);
        textToAddress = (TextView) findViewById(R.id.tv_to_address);

        swipeListViewCarContainer = (ListView) findViewById(R.id.swipe_list_car_container);
        swipeCarAdapter = new SwipeCarAdapter(this, carArray);
        swipeListViewCarContainer.setAdapter(swipeCarAdapter);

        linearSelectCarType = (LinearLayout) findViewById(R.id.ll_select_car_type);
        linearSelectCarType.setOnClickListener(this);

        linearCarNum = (LinearLayout) findViewById(R.id.ll_select_car_num);
        linearCarNum.setOnClickListener(this);
        textCarNum = (TextView) findViewById(R.id.tv_car_num);

        linearSendCarWay = (LinearLayout) findViewById(R.id.ll_send_car_ways);
        linearSendCarWay.setOnClickListener(this);
        textSendCarWay = (TextView) findViewById(R.id.tv_send_car_way);

        linearGetCarWay = (LinearLayout) findViewById(R.id.ll_get_car_ways);
        linearGetCarWay.setOnClickListener(this);
        textGetCarWay = (TextView) findViewById(R.id.tv_get_car_way);

        linearCarInsurance = (LinearLayout) findViewById(R.id.linearCarInsurance);

        findViewById(R.id.textTransport).setOnClickListener(this);
        findViewById(R.id.textRefund).setOnClickListener(this);
        findViewById(R.id.textService).setOnClickListener(this);

        checkBox = (CheckBox) findViewById(R.id.cb_checkbox);
        textPrice = (TextView) findViewById(R.id.tv_calc_price);

        textButton = (TextView) findViewById(R.id.next_step);
        textButton.setOnClickListener(this);

        initData();
    }

    private void initData() {
        App.appAction.lastOrderDetail(new ActionCallbackListener<AddressInfoBean>() {
            @Override
            public void onSuccess(AddressInfoBean data) {
                refreshAddressView(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });
    }

    private void refreshAddressView(AddressInfoBean data) {
        textFromAddress.setText(data.from_cityname);
        textToAddress.setText(data.to_cityname);
        fromCityDetail = data.from_address;
        toCityDetail = data.to_address;
        fromName = data.from_name;
        toName = data.to_name;
        fromTel = data.from_mobile;
        toTel = data.to_mobile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_from_address:
                selectFromAddress();
                break;
            case R.id.ll_to_address:
                selectToAddress();
                break;
            case R.id.ll_select_car_type:
                selectCarType();
                break;
            case R.id.ll_select_car_num:
                setCarNum();
                break;
            case R.id.ll_send_car_ways:
                setSendCarWay();
                break;
            case R.id.ll_get_car_ways:
                setGetCarWay();
                break;
            case R.id.textTransport:
                showProtocol("运输协议", Api.ProtocolTransport);
                break;
            case R.id.textRefund:
                showProtocol("退款规则", Api.ProtocolRefund);
                break;
            case R.id.textService:
                showProtocol("服务协议", Api.ProtocolService);
                break;
            case R.id.next_step:
                nextStep();
                break;
        }
    }

    /**
     * 进行下一步，获取价格或下单操作
     */
    private void nextStep() {
        if (state) {
            if(sendWayID.equals(Api.TransportWayArray[1])){
                fromCityDetail = "";
            }
            if(getWayID.equals(Api.TransportWayArray[1])){
                toCityDetail = "";
            }
            Intent intent = new Intent(this, OrderConfirmActivity.class);
            intent.putParcelableArrayListExtra(OrderConfirmActivity.CarArray, carArray);
            intent.putExtra(OrderConfirmActivity.CarArray, carArray);
            intent.putExtra(OrderConfirmActivity.FromCity, textFromAddress.getText());
            intent.putExtra(OrderConfirmActivity.ToCity, textToAddress.getText());
            intent.putExtra(OrderConfirmActivity.FromCityDetail, fromCityDetail);
            intent.putExtra(OrderConfirmActivity.ToCityDetail, toCityDetail);
            intent.putExtra(OrderConfirmActivity.FromName, fromName);
            intent.putExtra(OrderConfirmActivity.FromTel, fromTel);
            intent.putExtra(OrderConfirmActivity.ToName, toName);
            intent.putExtra(OrderConfirmActivity.ToTel, toTel);
            intent.putExtra(OrderConfirmActivity.OrderPrice, orderPrice);
            intent.putParcelableArrayListExtra(OrderConfirmActivity.OrderInsure, insuranceArray);
            startActivity(intent);
        } else {
            getPrice();
        }
    }

    private void getPrice() {
        String fromCity = textFromAddress.getText().toString();
        String toCity = textToAddress.getText().toString();
        for(int i=0; i<linearCarInsurance.getChildCount(); i++){
            EditText editInsurancePrice = (EditText) linearCarInsurance.getChildAt(i).findViewById(R.id.et_insurance_price);
            String insurancePrice = editInsurancePrice.getText().toString();
            if(TextUtils.isEmpty(insurancePrice)){
                ToastUtil.showToast(App.app,"投保价值不能为空");
                return;
            }
            insuranceArray.get(i).insurePrice = insurancePrice;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "请稍等...", true);
        App.appAction.calculatePrice(fromCity, toCity, fromCityDetail, toCityDetail, sendWayID, getWayID, carArray, insuranceArray, new ActionCallbackListener<CalculatePriceInfoBean>() {
            @Override
            public void onSuccess(CalculatePriceInfoBean data) {
                textPrice.setText(data.node);
                orderPrice = data.node;
                state = true;
                textButton.setText("下一步");
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });


    }

    /**
     * 显示条款
     *
     * @param title
     * @param url
     */
    private void showProtocol(String title, String url) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constant.SERVICE_DETAIL_ID, url);
        intent.putExtra(Constant.SERVICE_DETAIL_TITLE, title);
        startActivity(intent);
    }

    /**
     * 设置收车方式
     */
    private void setGetCarWay() {
        stateChange();
        Intent intent = new Intent(this, TransportWayActivity.class);
        if (TextUtils.isEmpty(textToAddress.getText().toString())) {
            ToastUtil.showToast(App.app, "请填写目的城市");
        } else {
            intent.putExtra(TransportWayActivity.CityName, textToAddress.getText().toString());
            intent.putExtra(TransportWayActivity.IsSendWay, false);
            intent.putExtra(TransportWayActivity.AddressDetail, toCityDetail);
            startActivityForResult(intent, RequestCode_GetWay);
        }
    }

    /**
     * 设置发车方式
     */
    private void setSendCarWay() {
        stateChange();
        Intent intent = new Intent(this, TransportWayActivity.class);
        if (TextUtils.isEmpty(textFromAddress.getText().toString())) {
            ToastUtil.showToast(App.app, "请填写出发城市");
        } else {
            intent.putExtra(TransportWayActivity.CityName, textFromAddress.getText().toString());
            intent.putExtra(TransportWayActivity.AddressDetail, fromCityDetail);
            startActivityForResult(intent, RequestCode_SendWay);
        }
    }

    /**
     * 设置车型数量
     */
    private void setCarNum() {
        stateChange();
        Intent intent = new Intent(this, SelectCarNumActivity.class);
        if (carArray.size() < 1) {
            ToastUtil.showToast(App.app, "请选择车辆");
        } else {
            intent.putParcelableArrayListExtra(SelectCarNumActivity.SelectCarNumWithType, carArray);
            startActivityForResult(intent, RequestCode_SelectCarNum);
        }
    }

    /**
     * 选择车型
     */
    private void selectCarType() {
        stateChange();
        Intent intent = new Intent(this, CarTypeActivity.class);
        intent.putParcelableArrayListExtra(CarTypeActivity.SelectCar, carArray);
        startActivityForResult(intent, RequestCode_SelectCarType);
    }

    /**
     * 选择目的地
     */
    private void selectToAddress() {
        stateChange();
        Intent intent = new Intent(this, SelectCityForAddressActivity.class);
        intent.putExtra(SelectCityForAddressActivity.SelectAddressType, SelectCityForAddressActivity.SelectToAddress);
        startActivityForResult(intent, RequestCode_SelectToAddress);
    }

    /**
     * 选择发货地
     */
    private void selectFromAddress() {
        stateChange();
        Intent intent = new Intent(this, SelectCityForAddressActivity.class);
        intent.putExtra(SelectCityForAddressActivity.SelectAddressType, SelectCityForAddressActivity.SelectFromAddress);
        startActivityForResult(intent, RequestCode_SelectFromAddress);
    }

    //计算价格状态 设置方法
    public void stateChange() {
        if (state) {
            textButton.setText("计算价格");
            textPrice.setText("0.00");
        }
        state = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode_SelectFromAddress:
                if (resultCode == Activity.RESULT_OK) {
                    fromCityID = data.getStringExtra(SelectCityForAddressActivity.SelectCityID);
                    textFromAddress.setText(data.getStringExtra(SelectCityForAddressActivity.SelectCityName));
                }
                break;
            case RequestCode_SelectToAddress:
                if (resultCode == Activity.RESULT_OK) {
                    toCityID = data.getStringExtra(SelectCityForAddressActivity.SelectCityID);
                    textToAddress.setText(data.getStringExtra(SelectCityForAddressActivity.SelectCityName));
                }
                break;
            case RequestCode_SendWay:
                if (resultCode == Activity.RESULT_OK) {
                    sendWayID = data.getStringExtra(TransportWayActivity.WayID);
                    textSendCarWay.setText(data.getStringExtra(TransportWayActivity.WayName));
                    fromCityDetail = data.getStringExtra(TransportWayActivity.AddressDetail);
                }
                break;
            case RequestCode_GetWay:
                if (resultCode == Activity.RESULT_OK) {
                    getWayID = data.getStringExtra(TransportWayActivity.WayID);
                    textGetCarWay.setText(data.getStringExtra(TransportWayActivity.WayName));
                    toCityDetail = data.getStringExtra(TransportWayActivity.AddressDetail);
                }
                break;
            case RequestCode_SelectCarType:
                if (resultCode == Activity.RESULT_OK) {
                    CarInfoBean temp = data.getParcelableExtra(CarTypeActivity.SelectCar);
                    carArray.add(temp);
                    swipeCarAdapter.notifyDataSetChanged();
                    refreshInsuranceLinearLayout();
                    refreshTextCarNum();
                }
                break;
            case RequestCode_SelectCarNum:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<CarInfoBean> temp = data.getParcelableArrayListExtra(SelectCarNumActivity.SelectCarNumWithType);
                    carArray.clear();
                    carArray.addAll(temp);
                    swipeCarAdapter.notifyDataSetChanged();
                    refreshInsuranceLinearLayout();
                    refreshTextCarNum();
                }
                break;
        }
    }

    public void refreshInsuranceLinearLayout() {
        linearCarInsurance.removeAllViews();
        insuranceArray.clear();
        for(int i=0; i<carArray.size(); i++){
            CarInfoBean bean = carArray.get(i);
            for(int j=0; j<Integer.parseInt(bean.num); j++){
                InsuranceInfoBean insuranceInfoBean = new InsuranceInfoBean();
                insuranceInfoBean.id = bean.id;
                insuranceArray.add(insuranceInfoBean);

                LinearLayout linearLayout = (LinearLayout) View.inflate(this,R.layout.layout_order_insure,null);
                TextView textLabelInsurance = (TextView) linearLayout.findViewById(R.id.textLabelInsurance);
                textLabelInsurance.setText("投保第"+(j+1)+"辆"+bean.name+"价值");
                final EditText editInsurancePrice = (EditText) linearLayout.findViewById(R.id.et_insurance_price);
                editInsurancePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            editInsurancePrice.setText("");
                            stateChange();
                        }
                    }
                });
                linearCarInsurance.addView(linearLayout);
            }
        }
    }

    public void refreshTextCarNum() {
        int totalNum = 0;
        for (int i = 0; i < carArray.size(); i++) {
            int temp = Integer.parseInt(carArray.get(i).num);
            totalNum += temp;
        }
        textCarNum.setText(totalNum + "");
        stateChange();
    }
}
