package com.htlc.cjwl.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.MainActivity;
import com.htlc.cjwl.R;
import com.htlc.cjwl.adapter.SwipeCarAdapter;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;

import util.ToastUtil;

import java.util.ArrayList;

import api.Api;
import core.ActionCallbackListener;
import model.AddressInfoBean;
import model.CalculatePriceInfoBean;
import model.CarInfoBean;
import model.InsuranceInfoBean;

/**
 * Created by Larno on 16/04/05.
 * 预生成订单（我要运车Activity）
 */
public class OrderInfoActivity extends Activity implements View.OnClickListener {
    public static final int RequestCode_SelectFromAddress = 100;
    public static final int RequestCode_SelectToAddress = 200;
    public static final int RequestCode_SendWay = 300;
    public static final int RequestCode_GetWay = 400;
    public static final int RequestCode_SelectCarType = 500;
    public static final int RequestCode_SelectCarNum = 600;
    private TextView textTitle;//标题
    //出发城市，目的城市，选择汽车品牌车型，选择汽车数量，选择发车方式，选择收车方式，投保列表
    private LinearLayout linearFromAddress, linearToAddress,
            linearSelectCarType, linearCarNum, linearSendCarWay,
            linearGetCarWay, linearCarInsurance;
    //选择的汽车品牌列表
    private ListView swipeListViewCarContainer;
    private SwipeCarAdapter swipeCarAdapter;
    //出发城市，目的城市，汽车总数量, 发车方式，收车方式，价格
    private TextView textFromAddress, textToAddress, textCarNum,
            textSendCarWay, textGetCarWay, textPrice;
    private CheckBox checkBox;//同意协议按钮
    private TextView textButton;//计算价格或下一步，按钮
    private EditText currentEditEnsurance;

    private boolean state = false;//是否已经计算了价格，没计算false
    private String fromCityID, toCityID, fromCityDetail = "", toCityDetail = "",
            fromName = "", toName = "", fromTel = "", toTel = "",
            sendWayID = Api.TransportWayArray[1], getWayID = Api.TransportWayArray[1],
            orderPrice = "0.0", orderInsurePrice;
    private ArrayList<CarInfoBean> carArray = new ArrayList<>();
    private ArrayList<InsuranceInfoBean> insuranceArray = new ArrayList<>();
    private TextView textNeedTime;//预估需要的运输时间

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, new Intent());
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
//        findViewById(R.id.textService).setOnClickListener(this);
        findViewById(R.id.textInsuranceHtml).setOnClickListener(this);

        checkBox = (CheckBox) findViewById(R.id.cb_checkbox);
        textPrice = (TextView) findViewById(R.id.tv_calc_price);
        textNeedTime = (TextView) findViewById(R.id.textNeedTime);

        textButton = (TextView) findViewById(R.id.next_step);
        textButton.setOnClickListener(this);

        SpannableString styledText = new SpannableString(getString(R.string.send_way_name_no));
        styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWay), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWayTips), 1, styledText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textSendCarWay.setText(styledText, TextView.BufferType.SPANNABLE);

        SpannableString styledTextGet = new SpannableString(getString(R.string.get_way_name_no));
        styledTextGet.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWay), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        styledTextGet.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWayTips), 1, styledTextGet.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textGetCarWay.setText(styledTextGet, TextView.BufferType.SPANNABLE);

        initData();
    }

    /*获取最近订单的收货地址信息*/
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

    /*刷新地址信息*/
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
//            case R.id.textService:
//                showProtocol("服务协议", Api.ProtocolService);
//                break;
            case R.id.textInsuranceHtml:
                showProtocol("投保说明", Api.ProtocolInsurance);
                break;
            case R.id.next_step:
                v.setFocusable(true);
                v.requestFocus();
                nextStep();
                break;
        }
    }

    /**
     * 进行下一步，获取价格或下单操作
     */
    private void nextStep() {
        //如果已计算价格，去下单界面
        if (state) {
            if (sendWayID.equals(Api.TransportWayArray[1])) {
                fromCityDetail = "";
            }
            if (getWayID.equals(Api.TransportWayArray[1])) {
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
            intent.putExtra(OrderConfirmActivity.OrderInsurePrice, orderInsurePrice);
            intent.putParcelableArrayListExtra(OrderConfirmActivity.OrderInsure, insuranceArray);
            startActivityForResult(intent, MainActivity.RequestCode);
        } else {//计算价格
            getPrice();
        }
    }

    /*根据出发地，目的地等信息，计算运费*/
    private void getPrice() {
        if (!checkBox.isChecked()) {
            ToastUtil.showToast(App.app, "请阅读并同意相关协议！");
            return;
        }
        String fromCity = textFromAddress.getText().toString();
        String toCity = textToAddress.getText().toString();
        for (int i = 0; i < linearCarInsurance.getChildCount(); i++) {
            EditText editInsurancePrice = (EditText) linearCarInsurance.getChildAt(i).findViewById(R.id.et_insurance_price);
            String insurancePrice = editInsurancePrice.getText().toString();
            if (TextUtils.isEmpty(insurancePrice) || insurancePrice.equals("0")) {
                ToastUtil.showToast(App.app, "【请您阅读投保说明，填写相关车辆价值】");
                return;
            }
            insuranceArray.get(i).insurePrice = insurancePrice;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "请稍等...", true);
        App.appAction.calculatePrice(fromCity, toCity, fromCityDetail, toCityDetail, sendWayID, getWayID, carArray, insuranceArray, new ActionCallbackListener<CalculatePriceInfoBean>() {
            @Override
            public void onSuccess(CalculatePriceInfoBean data) {
                textPrice.setText(data.node);
                textNeedTime.setText(getString(R.string.need_time,data.days));
                orderPrice = data.node;
                orderInsurePrice = data.insure;
                state = true;
                checkBox.setEnabled(false);
                textButton.setText("下一步");
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if ("当前路线未开通价格计算失败".equals(message)) {
                    showTipsDialog();
                    return;
                }
                ToastUtil.showToast(App.app, message);
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
            checkBox.setEnabled(true);
            textButton.setText("计算价格");
            textPrice.setText("0.00");
            textNeedTime.setText("");
        }
        state = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //选择出发城市的返回结果
            case RequestCode_SelectFromAddress:
                if (resultCode == Activity.RESULT_OK) {
                    fromCityID = data.getStringExtra(SelectCityForAddressActivity.SelectCityID);
                    textFromAddress.setText(data.getStringExtra(SelectCityForAddressActivity.SelectCityName));
                    fromCityDetail = "";
                }
                break;
            //选择目的城市的返回结果
            case RequestCode_SelectToAddress:
                if (resultCode == Activity.RESULT_OK) {
                    toCityID = data.getStringExtra(SelectCityForAddressActivity.SelectCityID);
                    textToAddress.setText(data.getStringExtra(SelectCityForAddressActivity.SelectCityName));
                    toCityDetail = "";
                }
                break;
            //选择发货方式的返回结果
            case RequestCode_SendWay:
                if (resultCode == Activity.RESULT_OK) {
                    sendWayID = data.getStringExtra(TransportWayActivity.WayID);

                    SpannableString styledText = new SpannableString(data.getStringExtra(TransportWayActivity.WayName));
                    styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWay), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWayTips), 1, styledText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textSendCarWay.setText(styledText, TextView.BufferType.SPANNABLE);

//                    textSendCarWay.setText(data.getStringExtra(TransportWayActivity.WayName));
                    fromCityDetail = data.getStringExtra(TransportWayActivity.AddressDetail);
                }
                break;
            //选择收货方式的返回结果
            case RequestCode_GetWay:
                if (resultCode == Activity.RESULT_OK) {
                    getWayID = data.getStringExtra(TransportWayActivity.WayID);

                    SpannableString styledText = new SpannableString(data.getStringExtra(TransportWayActivity.WayName));
                    styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWay), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWayTips), 1, styledText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textGetCarWay.setText(styledText, TextView.BufferType.SPANNABLE);

//                    textGetCarWay.setText(data.getStringExtra(TransportWayActivity.WayName));
                    toCityDetail = data.getStringExtra(TransportWayActivity.AddressDetail);
                }
                break;
            //选择汽车种类（品牌车型）的返回结果
            case RequestCode_SelectCarType:
                if (resultCode == Activity.RESULT_OK) {
                    CarInfoBean temp = data.getParcelableExtra(CarTypeActivity.SelectCar);
                    carArray.add(temp);
                    swipeCarAdapter.notifyDataSetChanged();
                    refreshInsuranceLinearLayout();
                    refreshTextCarNum();
                }
                break;
            //选择汽车数量的返回结果
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
            //创建订单界面的返回结果
            case MainActivity.RequestCode:
                if(resultCode == Activity.RESULT_OK){
                    setResult(resultCode, data);
                    finish();
                }
                break;
        }
    }

    /*刷新投保列表*/
    public void refreshInsuranceLinearLayout() {
        linearCarInsurance.removeAllViews();
        insuranceArray.clear();
        for (int i = 0; i < carArray.size(); i++) {
            CarInfoBean bean = carArray.get(i);
            for (int j = 0; j < Integer.parseInt(bean.num); j++) {
                InsuranceInfoBean insuranceInfoBean = new InsuranceInfoBean();
                insuranceInfoBean.id = bean.id;
                insuranceArray.add(insuranceInfoBean);

                LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.layout_order_insure, null);
                TextView textLabelInsurance = (TextView) linearLayout.findViewById(R.id.textLabelInsurance);

                SpannableString styledText = new SpannableString(bean.name + "的车辆价值(用于车辆运输保险)");
                styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelInsurance), 0, styledText.length() - 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelInsuranceTips), styledText.length() - 10, styledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textLabelInsurance.setText(styledText, TextView.BufferType.SPANNABLE);

                final EditText editInsurancePrice = (EditText) linearLayout.findViewById(R.id.et_insurance_price);
                editInsurancePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            editInsurancePrice.setText("");
                            stateChange();
                        }
                    }
                });
                linearCarInsurance.addView(linearLayout);
            }
        }
    }

    /*刷新汽车总数量*/
    public void refreshTextCarNum() {
        int totalNum = 0;
        for (int i = 0; i < carArray.size(); i++) {
            int temp = Integer.parseInt(carArray.get(i).num);
            totalNum += temp;
        }
        textCarNum.setText(totalNum + "");
        stateChange();
    }

    /**
     * 路线为开通，提示拨打电话
     */
    private void showTipsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("未开通路线，请联系客服！\n400-8185959");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-8185959"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        ToastUtil.showToast(App.app, "请授予拨打电话权限");
                        return;
                    }
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
    }
}
