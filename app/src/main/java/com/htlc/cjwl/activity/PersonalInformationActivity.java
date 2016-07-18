package com.htlc.cjwl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.R;
import com.htlc.cjwl.util.CommonUtil;
import util.LogUtil;

/**
 * Created by sks on 2015/11/3.
 * 个人中心---个人信息
 */
public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_activity_title;
    private LinearLayout ll_modify_tel;//更换手机
    private LinearLayout ll_modify_password;//修改密码
    private TextView tv_tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.personal_info);

        ll_modify_tel = (LinearLayout) findViewById(R.id.ll_modify_tel);
        ll_modify_password = (LinearLayout) findViewById(R.id.ll_modify_password);
        tv_tel = (TextView) findViewById(R.id.tv_tel);

        iv_back.setOnClickListener(this);
        ll_modify_tel.setOnClickListener(this);
        ll_modify_password.setOnClickListener(this);

        tv_tel.setText(CommonUtil.getApplication().getUser().user_mobile);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                LogUtil.i(this, "返回，销毁该界面");
                finish();
                break;
            case R.id.ll_modify_tel:
                LogUtil.i(this, "修改手机号界面");
                startActivity(new Intent(this, ModifyTelActivity.class));
                finish();
                break;
            case R.id.ll_modify_password:
                LogUtil.i(this, "修改密码界面");
                startActivity(new Intent(this, PreModifyPasswordActivity.class));
                finish();
                break;

        }
    }
}
