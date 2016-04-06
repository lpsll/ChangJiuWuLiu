package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.ToastUtil;

import api.Api;
import core.ActionCallbackListener;


/**
 * Created by Larno on 2016/04/05.
 */
public class TransportWayActivity extends Activity implements View.OnClickListener {
    public static final String IsSendWay = "IsSendWay";
    public static final String CityName = "CityName";
    public static final String AddressDetail = "AddressDetail";
    public static final String WayID = "WayID";
    public static final String WayName = "WayName";
    public static final int RequestCode_LocationAddressDetail = 100;
    private boolean isSendWay;

    private String cityName,addressDetail;
    private TextView textTitle,textHall,textAddressDetail,textSendToHall,textChangJiuGet;
    private LinearLayout linearChangJiuGet,linearSendToHall;
    private View textButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_way);

        Intent intent = getIntent();
        cityName = intent.getStringExtra(CityName);
        addressDetail = intent.getStringExtra(AddressDetail);
        isSendWay = intent.getBooleanExtra(IsSendWay, true);
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textSendToHall = (TextView) findViewById(R.id.textSendToHall);
        textChangJiuGet = (TextView) findViewById(R.id.textChangJiuGet);
        if(isSendWay){
            textTitle.setText("发车方式");
        }else {
            textTitle.setText("提车方式");
            textSendToHall.setText("网点提车");
            textChangJiuGet.setText("长久送车");
        }
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        textAddressDetail = (TextView) findViewById(R.id.textAddressDetail);
        textAddressDetail.setText(TextUtils.isEmpty(addressDetail)?"":addressDetail);
        textHall = (TextView) findViewById(R.id.textHall);
        linearSendToHall = (LinearLayout) findViewById(R.id.ll_send_to_hall);
        linearSendToHall.setOnClickListener(this);
        linearChangJiuGet = (LinearLayout) findViewById(R.id.ll_chang_jiu_get);
        linearChangJiuGet.setOnClickListener(this);
        textButton = findViewById(R.id.confirm);
        textButton.setOnClickListener(this);

        initData();
    }

    private void initData() {
        App.appAction.transportWay(cityName, new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                textHall.setText(TextUtils.isEmpty(data)?"":data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_chang_jiu_get:
                setAddressDetail();
                break;
            case R.id.ll_send_to_hall:
                setSendToHall();
                break;
            case R.id.confirm:
                setTransportWay();
                break;
        }
    }

    private void setTransportWay() {
        if(TextUtils.isEmpty(addressDetail)){
            setSendToHall();
        }else {
            Intent result = new Intent();
            result.putExtra(WayID,Api.TransportWayArray[1]);
            result.putExtra(AddressDetail,addressDetail);
            result.putExtra(WayName,textChangJiuGet.getText().toString());
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    /**
     * 长久取车
     */
    private void setAddressDetail() {
        if(isSendWay){
            Intent intent = new Intent(this, SetDetailWithLocationActivity.class);
            startActivityForResult(intent, RequestCode_LocationAddressDetail);
        }else {
            Intent intent = new Intent(this, SetDetailWithNoLocationActivity.class);
            startActivityForResult(intent, RequestCode_LocationAddressDetail);
        }
    }

    /**
     * 送车到网点
     */
    private void setSendToHall() {
        Intent result = new Intent();
        result.putExtra(WayID, Api.TransportWayArray[0]);
        result.putExtra(AddressDetail,"");
        result.putExtra(WayName,textSendToHall.getText().toString());
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RequestCode_LocationAddressDetail:
                if(resultCode==Activity.RESULT_OK){
                    addressDetail = data.getStringExtra(SetDetailWithLocationActivity.LocationAddress);
                    textAddressDetail.setText(addressDetail);
                }
                break;
        }
    }
}
