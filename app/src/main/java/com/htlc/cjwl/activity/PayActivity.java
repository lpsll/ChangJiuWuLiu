package com.htlc.cjwl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.MainActivity;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.ToastUtil;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.Api;
import core.ActionCallbackListener;
import core.ErrorEvent;
import model.CarInfoBean;
import model.OrderDetailBean;
import model.PayChargeBean;
import model.PayOrderBean;

/**
 * Created by sks on 2016/4/7.
 */
public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String OrderID = "OrderID";
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "01";

    private TextView textTitle, textFromAddress, textToAddress,
            textFromTel, textToTel, textFromName, textToName,
            textCarTypeNameArray, textCarNumArray, textPrice,
            textInsurancePrice, textScore;
    private LinearLayout linearPayContainer;
    private CheckBox checkBox;

    private ArrayList<String> payArray;
    private int selectPayWay = 0;
    private int resultPayWay;
    private String orderId;
    private String mScore;
    private String html = "<font color=\"#3c3c3c\">本次可用积分</font>" +
            "<font color=\"#ff0000\">%1$s</font>" +
            "<font color=\"#3c3c3c\">分，可抵</font>" +
            "<font color=\"#ff0000\">%2$s</font>" +
            "<font color=\"#3c3c3c\">元</font>";
    private ProgressDialog payProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        orderId = getIntent().getStringExtra(OrderID);
        LogUtil.e(this, "orderId" + orderId);
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
        App.appAction.payOrderDetail(orderId, new ActionCallbackListener<PayOrderBean>() {
            @Override
            public void onSuccess(PayOrderBean data) {
                refreshView(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private void initLinearCarCardContainer() {
        payArray = new ArrayList<>();
        payArray.add("支付宝");
        payArray.add("微信");
        payArray.add("银联");
        payArray.add("现付");
        for (int i = 0; i < payArray.size(); i++) {
            LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.layout_pay, null);
            linearLayout.setTag(i);
            TextView textPayName = (TextView) linearLayout.findViewById(R.id.textPayName);
            textPayName.setText(payArray.get(i));
            linearLayout.setOnClickListener(this);
            if (selectPayWay == i) {
                CheckBox checkBox = (CheckBox) linearLayout.findViewById(R.id.checkBox);
                checkBox.setChecked(true);
            }
            linearPayContainer.addView(linearLayout);
        }
    }

    private void refreshView(PayOrderBean data) {
        if (TextUtils.isEmpty(data.from_address)) {
            textFromAddress.setText(data.from_cityname);
        } else {
            textFromAddress.setText(data.from_address);
        }
        if (TextUtils.isEmpty(data.to_address)) {
            textToAddress.setText(data.to_cityname);
        } else {
            textToAddress.setText(data.to_address);
        }
        textFromTel.setText(data.from_mobile);
        textToTel.setText(data.to_mobile);
        textFromName.setText(data.from_name);
        textToName.setText(data.to_name);
        StringBuilder carTypeNameArrayStr = new StringBuilder();
        StringBuilder carNumArrayStr = new StringBuilder();
        float insurancePrice = 0;
        for (int i = 0; i < data.order_carfnum.size(); i++) {
            PayOrderBean.OrderCarfNum orderCarfNum = data.order_carfnum.get(i);
            carTypeNameArrayStr.append(orderCarfNum.carname + "  ");
            carNumArrayStr.append(orderCarfNum.num + "  ");
            insurancePrice += Float.parseFloat(orderCarfNum.price);
        }
        textCarTypeNameArray.setText(carTypeNameArrayStr.toString());
        textCarNumArray.setText(carNumArrayStr.toString());
        textInsurancePrice.setText("￥" + insurancePrice);
        textPrice.setText("￥" + data.order_price);

        html = String.format(html, data.socre, data.socre);
        textScore.setText(Html.fromHtml(html));
        mScore = data.socre;

    }

    @Override
    public void onClick(View v) {
        CheckBox preCheckBox = (CheckBox) linearPayContainer.getChildAt(selectPayWay).findViewById(R.id.checkBox);
        preCheckBox.setChecked(false);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        checkBox.setChecked(true);
        selectPayWay = linearPayContainer.indexOfChild(v);
    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        payProgressDialog = ProgressDialog.show(this, "", "请稍等...", true);
        String channel = Api.PayWayArray[selectPayWay];
        String score = checkBox.isChecked() ? mScore : "0";
        App.appAction.pay(orderId, channel, score, new ActionCallbackListener<PayChargeBean>() {
            @Override
            public void onSuccess(PayChargeBean data) {
                invokeOtherWidget(selectPayWay, data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
               showTipsDialog(message,false);
            }
        });
    }

    /**
     * 调用支付控件
     *
     * @param selectPayWay
     * @param chargeBean
     */
    private void invokeOtherWidget(int selectPayWay, PayChargeBean chargeBean) {
        if (selectPayWay == 0) {
            //支付宝
        } else if ((selectPayWay == 1)) {
            //微信
        } else if ((selectPayWay == 2)) {
            //银联
            resultPayWay = selectPayWay;
            unionPay(chargeBean);
        } else if ((selectPayWay == 3)) {
            //现付
        }
    }

    private void unionPay(PayChargeBean chargeBean) {
        if (TextUtils.isEmpty(chargeBean.tn)) {
            showTipsDialog("网络错误！",false);
        }
        UPPayAssistEx.startPay(this, null, null, chargeBean.tn, mMode);
    }


    /**
     * 支付结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultPayWay == 2) {
            handleUnionResult(data);
        }
    }

    /**
     * 处理银联支付结果
     *
     * @param data
     */
    private void handleUnionResult(Intent data) {
        if (data == null) {
            if(payProgressDialog!=null){
                payProgressDialog.dismiss();
            }
            return;
        }
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                verifyUnion(result);
            } else {
                showTipsDialog("支付成功！",true);
            }
        } else if (str.equalsIgnoreCase("fail")) {
            showTipsDialog("支付失败！",false);
        } else if (str.equalsIgnoreCase("cancel")) {
            showTipsDialog("支付取消！",false);
        }
    }


    /**
     * 银联验签
     *
     * @param data
     */
    private void verifyUnion(String data) {
        data = Base64.encodeToString(data.getBytes(),Base64.DEFAULT);
        App.appAction.verifyPay(data, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                showTipsDialog("支付成功！",true);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(ErrorEvent.NETWORK_ERROR.equals(errorEvent)){
                    showTipsDialog("网络出错！",false);
                }else {
                    showTipsDialog("支付失败！",false);
                }
            }
        });
    }

    /**
     * 显示支付结果
     *
     * @param msg
     */
    private void showTipsDialog(String msg, final boolean paySuccess) {
        if(payProgressDialog!=null){
            payProgressDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
//         builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (paySuccess) {
                    startActivity(new Intent(PayActivity.this, MainActivity.class));
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
        alertDialog.show();
    }
}
