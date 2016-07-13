package com.htlc.cjwl.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import util.LogUtil;
import util.ToastUtil;

/**
 * Created by sks on 2015/11/4.
 * 首页---一键客服Activity
 */
public class CallServiceTelActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;
    private TextView tv_call_tel;//拨打电话按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_service_tel);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);

        tv_call_tel = (TextView) findViewById(R.id.tv_call_tel);

        tv_activity_title.setText(R.string.call_service_tel);

        iv_back.setOnClickListener(this);
        tv_call_tel.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.tv_call_tel:
                LogUtil.i(this, "呼叫客服");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-818-5959"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        ToastUtil.showToast(App.app, "请授权拨打电话权限");
                        return;
                    }
                }
                startActivity(intent);
                break;

        }
    }
}
