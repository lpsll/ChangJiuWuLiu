package com.htlc.cjwl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.MainActivity;
import com.htlc.cjwl.R;
import com.htlc.cjwl.fragment.OrdersFragment;
import com.htlc.cjwl.util.CommonUtil;

import util.ToastUtil;

import java.util.ArrayList;

import core.ActionCallbackListener;
import model.CarInfoBean;
import model.InsuranceInfoBean;
import model.VinInfoBean;

/**
 * Created by Larno on 16/04/07.
 * 创建订单Activity
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
    public static final String OrderInsurePrice = "OrderInsurePrice";

    //标题，出发地，目的地，车辆型号数组，车辆数量数组，总价，投保价
    private TextView textTitle, textFromAddress, textToAddress,
            textCarTypeNameArray, textCarNumArray, textPrice,
            textInsurancePrice;
    //发货人，发货人电话，发货人身份证，收货人，收货人电话，收货人身份证
    private EditText editFromUsername, editFromTel, editFromUserCard,
            editToUsername, editToTel, editToUserCard;
    private LinearLayout linearCarCardContainer;//车架号的容器


    private ArrayList<CarInfoBean> carArray;//要运输的汽车数组
    public String fromCity = "";//出发地
    public String toCity = "";//目的地
    public String fromCityDetail = "";//出发地详情地址
    public String toCityDetail = "";//目的地详情地址
    public String fromTel = "";//发货人电话
    public String toTel = "";//收货人电话
    public String fromName = "";//发货人姓名
    public String toName = "";//收货人姓名

    public String orderPrice = "";//订单总价
    public String orderInsurePrice = "";//订单投保价
    public ArrayList<InsuranceInfoBean> orderInsure;//每辆车投保的价格数组
    public ArrayList<VinInfoBean> vinnumArray;//车架号数组
    private String orderId;//订单id

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
        orderInsurePrice = getIntent().getStringExtra(OrderInsurePrice);
        initView();
    }

    /*初始化view*/
    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED,new Intent());
                finish();
            }
        });
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("订单确认");

        textFromAddress = (TextView) findViewById(R.id.textFromAddress);
        if (TextUtils.isEmpty(fromCityDetail)) {
            textFromAddress.setText(fromCity);
        } else {
            textFromAddress.setText(fromCityDetail);
        }
        textToAddress = (TextView) findViewById(R.id.textToAddress);
        if (TextUtils.isEmpty(toCityDetail)) {
            textToAddress.setText(toCity);
        } else {
            textToAddress.setText(toCityDetail);
        }
        textCarTypeNameArray = (TextView) findViewById(R.id.textCarTypeNameArray);
        textCarNumArray = (TextView) findViewById(R.id.textCarNumArray);
        StringBuilder carName = new StringBuilder();
        StringBuilder carNum = new StringBuilder();
        for (int i = 0; i < carArray.size(); i++) {
            CarInfoBean bean = carArray.get(i);
            carName.append(bean.name + "  ");
            carNum.append(bean.num + "辆  ");
        }
        textCarTypeNameArray.setText(carName.toString());
        textCarNumArray.setText(carNum.toString());

        textPrice = (TextView) findViewById(R.id.textPrice);
        textInsurancePrice = (TextView) findViewById(R.id.textInsurancePrice);
        textPrice.setText("￥" + orderPrice);
//        double totalInsure = 0;
//        for(InsuranceInfoBean bean : orderInsure){
//            totalInsure += Float.parseFloat(bean.insurePrice);
//        }
        textInsurancePrice.setText("￥" + orderInsurePrice);

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

    /*初始化车架号列表*/
    private void initLinearCarCardContainer() {
        vinnumArray = new ArrayList<>();
        for (int i = 0; i < carArray.size(); i++) {
            CarInfoBean bean = carArray.get(i);
            String num = bean.num;
            for (int j = 0; j < Integer.parseInt(num); j++) {
                LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.layout_car_card, null);
                EditText editCarCard = (EditText) linearLayout.findViewById(R.id.editCarCard);
                editCarCard.setHint("请输入第" + (j + 1) + "辆" + bean.name + "车架号");
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
        fromName = editFromUsername.getText().toString().trim();
        toName = editToUsername.getText().toString().trim();
        fromTel = editFromTel.getText().toString().trim();
        toTel = editToTel.getText().toString().trim();
        for (int i = 0; i < linearCarCardContainer.getChildCount(); i++) {
            EditText editCarCard = (EditText) linearCarCardContainer.getChildAt(i).findViewById(R.id.editCarCard);
            String vinnum = editCarCard.getText().toString().trim();
            if (TextUtils.isEmpty(vinnum)) {
                ToastUtil.showToast(App.app, "请输入车架号");
                resetVinnumArray();
                return;
            }
            if (vinnumArray.contains(new VinInfoBean(vinnum))) {
                ToastUtil.showToast(App.app, "车架号不能重复");
                resetVinnumArray();
                return;
            }
            vinnumArray.get(i).vinnumId = vinnum;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "请稍等...", true);
        App.appAction.orderCreate(fromCity, toCity, fromCityDetail, toCityDetail, fromName, toName, fromTel, toTel, fromIdCard, toIdCard,
                vinnumArray, carArray, orderPrice, orderInsure, orderId, new ActionCallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        orderId = data;
                        resetVinnumArray();
                        showSuccessDialog();
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        resetVinnumArray();
                        ToastUtil.showToast(App.app, message);
                    }
                });
    }

    /*弹出生成订单成功，提示是否修改*/
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");//设置对话框标题
        builder.setMessage("您的订单已生成，请确认相关信息");//设置显示的内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                Intent intent = new Intent();
//              Intent intent = new Intent(OrderConfirmActivity.this, MainActivity.class);
//              startActivity(intent);
                intent.putExtra(MainActivity.SelectPosition, OrdersFragment.class.getSimpleName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
    }

    /*重置数组*/
    private void resetVinnumArray() {
        for (int i = 0; i < vinnumArray.size(); i++) {
            vinnumArray.get(i).vinnumId = null;
        }
    }

}
