package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.ClickUtil;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.MD5Util;
import com.htlc.cjwl.util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/2.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;

    private EditText et_tel;//手机号输入框
    private TextView tv_verification_code;//获取验证码按钮
    private EditText et_verification_code;//验证码输入框
    private EditText et_password;//密码输入框
    private EditText et_check_password;//确认密码输入框
    private CheckBox cb_checkbox;//注册协议选择按钮
    private TextView tv_register;//注册按钮
    private Handler handler = new Handler();//用于刷新倒计时的时间
    private int time = Constant.VERIFICATION_TIME;//获取验证码成功后，倒计时60s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);

        et_tel = (EditText) findViewById(R.id.et_tel);
        tv_verification_code = (TextView) findViewById(R.id.tv_verification_code);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);

        et_password = (EditText) findViewById(R.id.et_password);
        et_check_password = (EditText) findViewById(R.id.et_check_password);

        cb_checkbox = (CheckBox) findViewById(R.id.cb_checkbox);
        tv_register = (TextView) findViewById(R.id.tv_register);

        tv_activity_title.setText(R.string.register);

        iv_back.setOnClickListener(this);
        tv_verification_code.setOnClickListener(this);
        tv_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.tv_verification_code:
                LogUtil.i(this, "获取验证码");
                if(!ClickUtil.isFastDoubleClick()){
                    getVerificationCode();
                }
                break;
            case R.id.tv_register:
                LogUtil.i(this, "进行注册操作");
                register();
                break;
        }
    }

    /**
     * 注册方法，并判注册是否合法
     */
    private void register() {
        if(!cb_checkbox.isChecked()){
            ToastUtil.showToast(this, "请同意用户协议！");
            return;
        }
        String username = et_tel.getText().toString().trim();
        String pwd = et_password.getText().toString().trim();
        String confirmPwd = et_check_password.getText().toString().trim();
        String smsCode = et_verification_code.getText().toString().trim();
        App.appAction.register(username, pwd, confirmPwd, smsCode, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"注册成功！");
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
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    /**
     * 获取验证码操作
     */
    private void getVerificationCode() {
        String username = et_tel.getText().toString().trim();
        tv_verification_code.setEnabled(false);
        App.appAction.sendSmsCode(username, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                showTimer();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
                tv_verification_code.setEnabled(true);
            }
        });

    }

    /**
     * 显示倒计时
     */
    private void showTimer() {
        tv_verification_code.setEnabled(false);//不允许再次获取验证码
        handler.postDelayed(runnable,1000);

    }

    /**
     * 用于更新倒计时
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(time>0) {
                time--;
                tv_verification_code.setText("(" + time+"s)后重新获取");
                handler.postDelayed(this, 1000);
            }else{
                tv_verification_code.setText("重新获取验证码");
                tv_verification_code.setEnabled(true);
                time = Constant.VERIFICATION_TIME;
            }
        }
    };

}
