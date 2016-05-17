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
import com.htlc.cjwl.util.Constant;
import util.LogUtil;
import util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/3.
 */
public class SettingPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;

    private TextView tv_confirm;//确认按钮
    private EditText et_password;//密码输入框
    private EditText et_check_password;//确认密码输入框
    String tel;//注册手机号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //上一个Activity传入用户的手机号
        tel = getIntent().getStringExtra(Constant.USER_TEL);

        setContentView(R.layout.activity_setting_password);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText("设置新密码");

        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        et_password = (EditText) findViewById(R.id.et_password);
        et_check_password = (EditText) findViewById(R.id.et_check_password);

        iv_back.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.tv_confirm:
                LogUtil.i(this, "提交修改的密码，重新登录");
                commitPassword();
                break;


        }
    }

    private void commitPassword() {
        String pwd = et_password.getText().toString().trim();
        String confirmPwd = et_check_password.getText().toString().trim();
        App.appAction.settingPassword(tel, pwd, confirmPwd, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"密码设置成功,请重新登录！");
                goLogin();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }
    private void goLogin() {
        finish();
        startActivity(new Intent(SettingPasswordActivity.this, LoginActivity.class));
    }


}
