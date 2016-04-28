package com.htlc.cjwl.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.htlc.cjwl.App;
import com.htlc.cjwl.MainActivity;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;

import util.LogUtil;
import util.ToastUtil;

import com.htlc.cjwl.util.MD5Util;
import com.htlc.cjwl.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.Api;
import core.ActionCallbackListener;
import core.ErrorEvent;
import model.PayChargeBean;
import model.PayOrderBean;
import util.pay.AliPayUtil;
import util.pay.PayResult;

/**
 * Created by sks on 2016/4/7.
 */
public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String OrderID = "OrderID";
    private static final int ALI_SDK_PAY_FLAG = 1000;//支付宝
    private static final int UNION_SDK_PAY_FLAG = 2000;//银联
    private static final int WX_SDK_PAY_FLAG = 3000;//微信
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "00";

    private TextView textTitle, textFromAddress, textToAddress,
            textFromTel, textToTel, textFromName, textToName,
            textCarTypeNameArray, textCarNumArray, textPrice,
            textInsurancePrice, textScore;
    private LinearLayout linearPayContainer;
    private CheckBox checkBox;

    private ArrayList<String> payArray;
    private int selectPayWay = 0;
    private int payPlantform;
    private String orderId;
    private String mScore;
    private String price;
    private String useScore;
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
        registerWXBroadcastReceiver();
    }

    private void registerWXBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(getApplication().getPackageName() + ".action.WX_PAY");
        registerReceiver(wxPayBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wxPayBroadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (payProgressDialog != null) {
            payProgressDialog.dismiss();
        }
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
        double insurancePrice = 0;
        for (int i = 0; i < data.order_carfnum.size(); i++) {
            PayOrderBean.OrderCarfNum orderCarfNum = data.order_carfnum.get(i);
            carTypeNameArrayStr.append(orderCarfNum.carname + "  ");
            carNumArrayStr.append(orderCarfNum.num + "  ");
            insurancePrice += Double.parseDouble(orderCarfNum.insureprice);
        }
        textCarTypeNameArray.setText(carTypeNameArrayStr.toString());
        textCarNumArray.setText(carNumArrayStr.toString());
        textInsurancePrice.setText("￥" + insurancePrice);
        textPrice.setText("￥" + data.order_price);
        price = data.order_price;

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
        useScore = checkBox.isChecked() ? mScore : "0";
        App.appAction.pay(orderId, channel, useScore, new ActionCallbackListener<PayChargeBean>() {
            @Override
            public void onSuccess(PayChargeBean data) {
                invokeOtherWidget(selectPayWay, data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                showTipsDialog(message, false);
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
            payPlantform = ALI_SDK_PAY_FLAG;
            aliPay(chargeBean);
        } else if ((selectPayWay == 1)) {
            //微信
            payPlantform = WX_SDK_PAY_FLAG;
            wxPay(chargeBean);
        } else if ((selectPayWay == 2)) {
            //银联
            payPlantform = UNION_SDK_PAY_FLAG;
            unionPay(chargeBean);
        } else if ((selectPayWay == 3)) {
            //现付
            showTipsDialog("支付成功", true);

        }
    }

    private void wxPay(PayChargeBean chargeBean) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        boolean b = msgApi.registerApp(Constant.WX_APP_ID);
        IWXAPI wxapi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
        try {
            JSONObject json = new JSONObject(chargeBean.tn);
            PayReq req = new PayReq();
            req.appId = Constant.WX_APP_ID;  // 测试用appId
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
//            String sign = MD5Util.MD5("appid=" + req.appId
//                    + "&noncestr=" + req.nonceStr + "&package="
//                    + req.packageValue + "&partnerid=" + req.partnerId
//                    + "&prepayid=" + req.prepayId + "&timestamp="
//                    + req.timeStamp + "&key=" + Constant.WX_APP_KEY).toUpperCase();
//            req.sign = sign;
            req.sign = json.getString("sign");
            //req.extData = "app data";
            wxapi.sendReq(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void aliPay(PayChargeBean chargeBean) {
        double aliPrice = Double.parseDouble(price) - Double.parseDouble(useScore);
//        aliPrice = 0.01;
        final String payInfo = AliPayUtil.getPayInfo("长久运车", "运车费用", aliPrice + "", orderId);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = ALI_SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void unionPay(PayChargeBean chargeBean) {
        if (TextUtils.isEmpty(chargeBean.tn)) {
            showTipsDialog("网络错误！", false);
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
        if (payPlantform == UNION_SDK_PAY_FLAG) {
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
            if (payProgressDialog != null) {
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
                showTipsDialog("支付成功！", true);
            }
        } else if (str.equalsIgnoreCase("fail")) {
            showTipsDialog("支付失败！", false);
        } else if (str.equalsIgnoreCase("cancel")) {
            showTipsDialog("支付取消！", false);
        }
    }


    /**
     * 银联验签
     *
     * @param data
     */
    private void verifyUnion(String data) {
        data = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
        App.appAction.verifyPay(data, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                showTipsDialog("支付成功！", true);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if (ErrorEvent.NETWORK_ERROR.equals(errorEvent)) {
                    showTipsDialog("网络出错！", false);
                } else {
                    showTipsDialog("支付失败！", false);
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
        if (payProgressDialog != null) {
            payProgressDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果");
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
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
        alertDialog.show();
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));

    }

    /**
     * 处理支付宝支付结果
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ALI_SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showTipsDialog("支付成功！", true);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showTipsDialog("支付结果确认中！", true);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showTipsDialog("支付失败！", false);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    /**
     * 处理微信支付结果
     */
    private BroadcastReceiver wxPayBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(WXPayEntryActivity.TAG, 1);
            switch (code) {
                case 0://支付成功后的界面
                    showTipsDialog("支付成功！",true);
                    break;
                case -1:
                    //支付错误
                    showTipsDialog("支付失败！",false);
                    break;
                case -2://用户取消支付后的界面
                    showTipsDialog("支付取消！",false);
                    break;
                default:
                    //支付错误
                    showTipsDialog("支付失败！",false);
                    break;
            }
        }
    };
}
