package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
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
 * Created by Larno on 15/11/5.
 */
public class SetDetailWithLocationActivity extends Activity implements AMapLocationListener, View.OnClickListener {
    public static final String LocationAddress = "LocationAddress";
    TextView textTitle;
    EditText editLocation;
    TextView textMapLocation;
    LinearLayout linearAmapLocation;


    private LocationManagerProxy mLocationManagerProxy;
    private String detail_address;// 详细地址信息
    private boolean isLoacOk = false;//是否定位成功
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_detail_with_location);

        findViewById(R.id.iv_back).setOnClickListener(this);
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("详细地址");
        editLocation = (EditText) findViewById(R.id.edt_location);
        textMapLocation = (TextView) findViewById(R.id.tv_amap_location);
        linearAmapLocation = (LinearLayout) findViewById(R.id.ll_amap_location);
        linearAmapLocation.setOnClickListener(this);
        findViewById(R.id.tv_get_comfir).setOnClickListener(this);
        findViewById(R.id.iv_clean).setOnClickListener(this);
        init();

        String addressDetail = getIntent().getStringExtra(TransportWayActivity.AddressDetail);
        cityName = getIntent().getStringExtra(TransportWayActivity.CityName);
        if(!TextUtils.isEmpty(addressDetail)){
            editLocation.setText(addressDetail);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.ll_amap_location:
                if (isLoacOk) {
                    next();
                } else {
                    Toast.makeText(this, "请手动输入地址！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_clean:
                editLocation.setText("");
                break;
            case R.id.tv_get_comfir:
                String address = editLocation.getText().toString().trim();
                if (TextUtils.isEmpty(address) && !isLoacOk) {
                    Toast.makeText(this, "输入地址不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!TextUtils.isEmpty(address)){
                    if(!address.contains(cityName+"市")){
                        Toast.makeText(this, "地址必须包含"+cityName+"市", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra(LocationAddress, address);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    next();
                }
                break;
        }
    }

    public void next() {
        if(!detail_address.contains(cityName+"市")){
            Toast.makeText(this, "请手动输入地址，必须包含"+cityName+"市", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(LocationAddress, detail_address);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void init() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
        mLocationManagerProxy.setGpsEnable(false);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            Double geoLat = amapLocation.getLatitude();
            Double geoLng = amapLocation.getLongitude();
//            textMapDesc.setText(amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict() + amapLocation.getRoad());
//            textMapLocation.setText(amapLocation.getPoiName());
//            Desc = amapLocation.getAddress();
            detail_address = amapLocation.getAddress();
            textMapLocation.setText(detail_address);
            isLoacOk = true;
            mLocationManagerProxy.removeUpdates(this);
        } else {
            Log.e("AmapErr", "Location ERR:" + amapLocation.getAMapException().getErrorCode() + amapLocation.getAMapException().getErrorMessage());
            textMapLocation.setText("定位失败，请手动输入！");
            isLoacOk = false;
            Toast.makeText(this, "定位失败，请手动输入！", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        // 移除定位请求
        mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
