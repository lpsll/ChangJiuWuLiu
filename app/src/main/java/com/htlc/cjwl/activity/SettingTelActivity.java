package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.CommonUtil;
import util.LogUtil;

import util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/3.
 * 设置新手机号
 */
public class SettingTelActivity  extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;

    private EditText et_tel;//输入手机号控件
    private TextView tv_next;//完成按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_tel);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.setting_new_tel);

        tv_next = (TextView) findViewById(R.id.tv_next);
        et_tel = (EditText) findViewById(R.id.et_tel);


        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.tv_next:
                LogUtil.i(this, "手机号设置完成");
                commit();
                break;

        }
    }

    /**
     * 手机号设置完成操作，提交服务器，判断设置是否成功
     */
    private void commit() {
        String newTel = et_tel.getText().toString().trim();
        App.appAction.resetTel(newTel, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"手机号修改成功，请重新登录!");
                CommonUtil.getApplication().setNode("", "退出成功");
                goLogin();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /**
     * 设置手机号成功的操作
     */
    private void goLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
