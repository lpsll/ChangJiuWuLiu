package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.htlc.cjwl.R;


/**
 * Created by luochuan on 15/11/5.
 */
public class GetGoodsAddressActivity extends Activity{

    private LocationManagerProxy mLocationManagerProxy;
    private String Desc;
    private int detail_address;// 详细地址信息
    private boolean isLoacOk = false;//是否定位成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_goods_address);
    }


}
