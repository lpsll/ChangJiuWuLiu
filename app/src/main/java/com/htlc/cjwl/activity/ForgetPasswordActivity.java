package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.ClickUtil;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.LogUtil;
import com.htlc.cjwl.util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/3.
 */
public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;

    private EditText et_tel;//帐号输入框
    private TextView tv_verification_code;//获取验证码按钮
    private EditText et_verification_code;//验证码输入框
    private TextView tv_next;//下一步按钮
    private Handler handler = new Handler();//用于刷新倒计时的时间
    private int time = Constant.VERIFICATION_TIME;//获取验证码成功后，倒计时60s
    String tel;//注册手机号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText("忘记密码");


        et_tel = (EditText) findViewById(R.id.et_tel);
        tv_verification_code = (TextView) findViewById(R.id.tv_verification_code);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        tv_next = (TextView) findViewById(R.id.tv_next);

        iv_back.setOnClickListener(this);
        tv_verification_code.setOnClickListener(this);
        tv_next.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_verification_code:
                if(!ClickUtil.isFastDoubleClick()){
                    getVerificationCode();
                }
                break;
            case R.id.tv_next:
                next();
                break;

        }
    }

    private void next() {
        String username = et_tel.getText().toString().trim();
        tel = username;
        String smsCode = et_verification_code.getText().toString().trim();
        App.appAction.checkSmsCode(username, smsCode, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                nextStep();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }
    private void nextStep() {
        finish();
        Intent intent = new Intent(this, SettingPasswordActivity.class);
        intent.putExtra(Constant.USER_TEL,tel);
        startActivity(intent);
    }
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


    private void showTimer() {
        tv_verification_code.setEnabled(false);//不允许再次获取验证码
        handler.postDelayed(runnable,1000);

    }
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
