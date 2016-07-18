package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;

import util.ToastUtil;

import core.ActionCallbackListener;

/**
 * Created by sks on 2015/11/3.
 * 验证密码（修改密码第一步）
 */
public class PreModifyPasswordActivity extends AppCompatActivity {
    private TextView tv_activity_title;
    private TextView tv_next;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_modify_password);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.modify_password);

        tv_next = (TextView) findViewById(R.id.tv_next);
        et_password = (EditText) findViewById(R.id.et_password);

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });

    }
    private void checkPassword() {
        String pwd = et_password.getText().toString().trim();
        App.appAction.checkPassword(pwd, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                next();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });

    }

    private void next() {
        startActivity(new Intent(PreModifyPasswordActivity.this, ModifyPasswordActivity.class));
        finish();
    }

}
