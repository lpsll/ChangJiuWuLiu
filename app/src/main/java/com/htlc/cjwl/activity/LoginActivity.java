package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.CommonUtil;

import util.ToastUtil;

/**
 * Created by sks on 2015/10/30.
 */
public class LoginActivity  extends AppCompatActivity implements View.OnClickListener,App.OnLoginListener{
    private TextView tv_register_title;//注册按钮
    private EditText et_username;//用户名输入框
    private EditText et_password;//密码输入框
    private TextView tv_login;//登录按钮
    private TextView tv_forget_password;//忘记密码按钮

    private String usernane;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_register_title = (TextView) findViewById(R.id.tv_register_title);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);

        tv_register_title.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        //添加登录监听，监听登录是否成功
        CommonUtil.getApplication().addLoginListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register_title:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                finish();
                break;
        }
    }

    /*登录操作*/
    private void login() {
        String username = et_username.getText().toString().trim();
        String pwd = et_password.getText().toString().trim();
        CommonUtil.login(username,pwd);
    }

    /**
     * 监听到登录成功，保存帐号密码到本地文件，并关闭登录界面
     */
    @Override
    public void onLoginSuccess() {
        ToastUtil.showToast(App.app,"登录成功！");
        finish();

    }

    /**
     * 监听到登录失败，提示用户
     */
    @Override
    public void onLoginError(String msg) {
        ToastUtil.showToast(App.app,msg);
    }

    /**
     * activity关闭时，移除登录监听
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtil.getApplication().removeLoginListener(this);
    }
}
