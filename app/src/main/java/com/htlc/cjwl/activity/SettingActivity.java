package com.htlc.cjwl.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.UpdateUtil;

import util.LogUtil;
import util.ToastUtil;

import api.Api;

/**
 * Created by sks on 2015/11/2.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_activity_title;
    private ImageView iv_back;
    private LinearLayout ll_clear_cache;//清除缓存
    private LinearLayout ll_about;//关于软件
    private LinearLayout ll_version;//当前版本
    private LinearLayout ll_version_update;//软件更新
    private LinearLayout ll_feedback;//意见反馈
    private TextView tv_version;//显示版本号的控件
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(R.string.setting);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        ll_clear_cache = (LinearLayout) findViewById(R.id.ll_clear_cache);
        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        ll_version = (LinearLayout) findViewById(R.id.ll_version);
        tv_version = (TextView) findViewById(R.id.tv_version);
        ll_version_update = (LinearLayout) findViewById(R.id.ll_version_update);
        ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);

        iv_back.setOnClickListener(this);
        ll_clear_cache.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_version.setOnClickListener(this);
        ll_version_update.setOnClickListener(this);
        ll_feedback.setOnClickListener(this);

        //获取当前应用版本号，并显示
        String versionName = CommonUtil.getVersionName();
        tv_version.setText("V"+versionName);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_clear_cache:
                LogUtil.i(this,"清除缓存");
                showDialog();
                break;
            case R.id.ll_about:
                LogUtil.i(this, "关于界面");
                Intent intent_rute_query = new Intent(this, WebActivity.class);
                intent_rute_query.putExtra(Constant.SERVICE_DETAIL_ID, Api.ProtocolAbout);
                intent_rute_query.putExtra(Constant.SERVICE_DETAIL_TITLE, "关于软件");
                startActivity(intent_rute_query);
//                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.ll_version:
                LogUtil.i(this, "当前版本界面");
                break;
            case R.id.ll_version_update:
                LogUtil.i(this, "检测更新界面");
                new UpdateUtil(this).canUpdate(true);
                break;
            case R.id.ll_feedback:
                LogUtil.i(this, "意见反馈界面");
                String node = App.app.getNode();
                if (TextUtils.isEmpty(node)) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    startActivity(new Intent(this,FeedbackActivity.class));
                }
                break;

        }

    }

    /**
     * 弹出确认对话框
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View view = View.inflate(getActivity(),R.layout.layout_dialog_confirm,null);
//        builder.setView(view);
//        builder.show();
        builder.setTitle("清除缓存");//设置对话框标题
        builder.setMessage("您确认清除缓存吗？");//设置显示的内容

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                ToastUtil.showToast(SettingActivity.this, "清除缓存成功！");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        });//在按键响应事件中显示此对话框
        alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));


    }

    @Override
    protected void onPause() {
        if(alertDialog!=null){
            alertDialog.dismiss();
        }
        LogUtil.i(this,"onPause");
        super.onPause();
    }
}
