package com.htlc.cjwl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.htlc.cjwl.R;


/**
 * Created by Larno on 16/04/07.
 * 编辑详情地址界面
 */
public class SetDetailWithNoLocationActivity extends Activity implements View.OnClickListener {
    TextView textTitle;
    EditText editLocation;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_detail_with_no_location);

        findViewById(R.id.iv_back).setOnClickListener(this);
        textTitle = (TextView) findViewById(R.id.tv_activity_title);
        textTitle.setText("详细地址");
        editLocation = (EditText) findViewById(R.id.edt_location);
        findViewById(R.id.tv_get_comfir).setOnClickListener(this);
        findViewById(R.id.iv_clean).setOnClickListener(this);

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
            case R.id.iv_clean:
                editLocation.setText("");
                break;
            case R.id.tv_get_comfir:
                String address = editLocation.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(this, "输入地址不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!TextUtils.isEmpty(address)) {
                    if(!address.contains(cityName+"市")){
                        Toast.makeText(this, "地址必须包含"+cityName+"市", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra(SetDetailWithLocationActivity.LocationAddress, address);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}
