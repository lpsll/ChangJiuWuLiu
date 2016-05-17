package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.ClickUtil;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import util.LogUtil;
import util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/3.
 */
public class ModifyTelActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;
    private TextView tv_tips;
    private EditText et_tel;
    private TextView tv_verification_code;
    private EditText et_verification_code;
    private TextView tv_next;
    private Handler handler = new Handler();//用于刷新倒计时的时间
    private int time = Constant.VERIFICATION_TIME;//获取验证码成功后，倒计时60s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tel);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.setting_new_tel);

        tv_tips = (TextView) findViewById(R.id.tv_tips);
        et_tel = (EditText) findViewById(R.id.et_tel);
        tv_verification_code = (TextView) findViewById(R.id.tv_verification_code);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        tv_next = (TextView) findViewById(R.id.tv_next);

        iv_back.setOnClickListener(this);
        tv_verification_code.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        String tips = String.format("你当前的手机号为：%1$s，\n手机号也是您的登录名，修改手机后，您将\n以新的方式登录", CommonUtil.getApplication().getUser().user_mobile);
        tv_tips.setText(tips);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.tv_verification_code:
                LogUtil.i(this, "请求，获取验证码");
                if(!ClickUtil.isFastDoubleClick()){
                    getVerificationCode();
                }
                break;
            case R.id.tv_next:
                LogUtil.i(this, "下一步，进行手机号设置");
                next();
                break;

        }
    }
    private void next() {
        String username = et_tel.getText().toString().trim();
        String smsCode = et_verification_code.getText().toString().trim();
        App.appAction.checkSmsCode(username, smsCode, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                nextStep();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }
    private void nextStep(){
        startActivity(new Intent(this, SettingTelActivity.class));
        finish();
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
                tv_verification_code.setText("(" + time+"s)");
                handler.postDelayed(this, 1000);
            }else{
                tv_verification_code.setText("获取验证码");
                tv_verification_code.setEnabled(true);
                time = Constant.VERIFICATION_TIME;
            }
        }
    };

}
