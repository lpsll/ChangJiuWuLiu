package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;

import util.ToastUtil;

import api.Api;
import core.ActionCallbackListener;


/**
 * Created by Larno on 2016/04/05.
 * 设置发车或收车方式
 */
public class TransportWayActivity extends Activity implements View.OnClickListener {
    public static final String IsSendWay = "IsSendWay";
    public static final String CityName = "CityName";
    public static final String AddressDetail = "AddressDetail";
    public static final String WayID = "WayID";
    public static final String WayName = "WayName";
    public static final int RequestCode_LocationAddressDetail = 100;
    private boolean isSendWay;//是发车方式 true

    private String cityName,addressDetail;
    private TextView textTitle,textHall,textAddressDetail,textCityName, textSendToHall,textChangJiuGet;
    private LinearLayout linearChangJiuGet,linearSendToHall;
    private TextView textButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_way);

        Intent intent = getIntent();
        cityName = intent.getStringExtra(CityName);//发车或收车城市名字
        addressDetail = intent.getStringExtra(AddressDetail);//详细地址
        isSendWay = intent.getBooleanExtra(IsSendWay, true);//是发车方式true

        textTitle = (TextView) findViewById(R.id.tv_activity_title);//标题
        textCityName = (TextView) findViewById(R.id.textCityName);//城市text view(显示 当前城市名 或 “网点地址”)
        textSendToHall = (TextView) findViewById(R.id.textSendToHall);//送车到网点（网点取车）TextView
        textChangJiuGet = (TextView) findViewById(R.id.textChangJiuGet);//长久取车（长久送车）TextView
        String yes,no;
        if(isSendWay){
            textTitle.setText("发车方式");
            yes = getString(R.string.send_way_name_yes);
            no = getString(R.string.send_way_name_no);
            initData();
        }else {
            textCityName.setText(cityName);
            textTitle.setText("提车方式");
            yes = getString(R.string.get_way_name_yes);
            no = getString(R.string.get_way_name_no);
        }
        SpannableString styledText = new SpannableString(no);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWay), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWayTips), 1, styledText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textSendToHall.setText(styledText, TextView.BufferType.SPANNABLE);

        SpannableString styledTextGet = new SpannableString(yes);
        styledTextGet.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWay), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        styledTextGet.setSpan(new TextAppearanceSpan(this, R.style.TextLabelWayTips), 1, styledTextGet.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textChangJiuGet.setText(styledTextGet, TextView.BufferType.SPANNABLE);



        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        textAddressDetail = (TextView) findViewById(R.id.textAddressDetail);//发车（收车）详情地址
        textAddressDetail.setText(TextUtils.isEmpty(addressDetail)?"":addressDetail);

        textHall = (TextView) findViewById(R.id.textHall);//当前城市网点地址TextView
        linearSendToHall = (LinearLayout) findViewById(R.id.ll_send_to_hall);
        linearSendToHall.setOnClickListener(this);
        linearChangJiuGet = (LinearLayout) findViewById(R.id.ll_chang_jiu_get);
        linearChangJiuGet.setOnClickListener(this);
        textButton = (TextView) findViewById(R.id.confirm);
        textButton.setText("完成");
        textButton.setOnClickListener(this);


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

    /*设置发车（取车）方式*/
    private void setTransportWay() {
        if(TextUtils.isEmpty(addressDetail)){
            setSendToHall();
        }else {
            Intent result = new Intent();
            result.putExtra(WayID,Api.TransportWayArray[0]);
            result.putExtra(AddressDetail,addressDetail);
            result.putExtra(WayName,textChangJiuGet.getText().toString());
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    /**
     * 长久取车,去设置详细地址界面
     */
    private void setAddressDetail() {
        //如果是发车，去带定位的设置详细地址界面
        if(isSendWay){
            Intent intent = new Intent(this, SetDetailWithLocationActivity.class);
            intent.putExtra(AddressDetail,addressDetail);
            intent.putExtra(CityName,cityName);
            startActivityForResult(intent, RequestCode_LocationAddressDetail);
        }else {
            Intent intent = new Intent(this, SetDetailWithNoLocationActivity.class);
            intent.putExtra(AddressDetail,addressDetail);
            intent.putExtra(CityName,cityName);
            startActivityForResult(intent, RequestCode_LocationAddressDetail);
        }
    }

    /**
     * 送车到网点
     */
    private void setSendToHall() {
        Intent result = new Intent();
        result.putExtra(WayID, Api.TransportWayArray[1]);
        result.putExtra(AddressDetail,"");
        result.putExtra(WayName,textSendToHall.getText().toString());
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    /*设置详情地址，返回的结果*/
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
