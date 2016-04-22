package com.htlc.cjwl.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.BillActivity;
import com.htlc.cjwl.activity.LoginActivity;
import com.htlc.cjwl.activity.MessageCenterActivity;
import com.htlc.cjwl.activity.PersonalInformationActivity;
import com.htlc.cjwl.activity.RefundActivity;
import com.htlc.cjwl.activity.ScoreActivity;
import com.htlc.cjwl.activity.SettingActivity;
import com.htlc.cjwl.util.CommonUtil;

import util.LogUtil;

import core.ActionCallbackListener;
import model.UserBean;

/**
 * Created by Administrator on 2015/10/28.
 */
public class MyFragment extends Fragment implements View.OnClickListener, App.OnLoginListener {
    private LinearLayout ll_account;//帐号条目
    private LinearLayout ll_account_tip;//帐号信息标题
    private LinearLayout ll_account_des;//帐号信息内容
    private ImageView iv_my_head;//个人头像
    private TextView tv_rank;//个人等级
    private TextView tv_tel;//个人电话



    private LinearLayout ll_money;//我的退款
    private LinearLayout ll_socre;//个人积分
    private TextView tv_score;//积分
    private LinearLayout ll_message_center;//消息中心
    private LinearLayout ll_setting;//设置中心

    private TextView tv_login;//登录或退出按钮
    private LinearLayout ll_bill;//我的发票


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加登录监听，监听登录是否成功
        CommonUtil.getApplication().addLoginListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_my_fragment, null);

        TextView tv_fragment_title = (TextView) view.findViewById(R.id.tv_fragment_title);
        tv_fragment_title.setText(R.string.my_fragment_title);

        ll_account = (LinearLayout) view.findViewById(R.id.ll_account);
        ll_account_tip = (LinearLayout) view.findViewById(R.id.ll_account_tip);
        ll_account_des = (LinearLayout) view.findViewById(R.id.ll_account_des);
        iv_my_head = (ImageView) view.findViewById(R.id.iv_my_head);
        tv_rank = (TextView) view.findViewById(R.id.tv_rank);
        tv_tel = (TextView) view.findViewById(R.id.tv_tel);


        ll_money = (LinearLayout) view.findViewById(R.id.ll_money);
        ll_bill = (LinearLayout) view.findViewById(R.id.ll_bill);
        ll_socre = (LinearLayout) view.findViewById(R.id.ll_socre);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        ll_message_center = (LinearLayout) view.findViewById(R.id.ll_message_center);
        ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);

        tv_login = (TextView) view.findViewById(R.id.tv_login);

        ll_account.setOnClickListener(this);
        iv_my_head.setOnClickListener(this);
        ll_money.setOnClickListener(this);
        ll_bill.setOnClickListener(this);
        ll_socre.setOnClickListener(this);
        ll_message_center.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtil.getApplication().removeLoginListener(this);
    }

    public void updateView() {
        // 未登录
        String node = CommonUtil.getApplication().getNode();
        if (TextUtils.isEmpty(node)) {
            ll_account_tip.setVisibility(View.INVISIBLE);
            ll_account_des.setVisibility(View.INVISIBLE);
            iv_my_head.setImageResource(R.drawable.my_head_default);
            tv_score.setText("0");
            tv_login.setText(R.string.login);
        } else {
            // 登录 请求个人信息进行展示
            getMyselfInfo();
            ll_account_tip.setVisibility(View.VISIBLE);
            ll_account_des.setVisibility(View.VISIBLE);
            iv_my_head.setImageResource(R.drawable.my_head);
            tv_login.setText(R.string.logout);

        }
    }

    /**
     * 请求网络获取个人信息
     */
    private void getMyselfInfo() {
        UserBean user = App.app.getUser();
        showMyselfInfo(user);
        App.appAction.getUserInfo(new ActionCallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                App.app.setUser(data);
                showMyselfInfo(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });
    }

    private void showMyselfInfo(UserBean bean) {
        if(bean==null){
            tv_rank.setText("");
            tv_tel.setText("");
            tv_score.setText("0");
        }else {
            tv_rank.setText(bean.levelname);
            tv_tel.setText(bean.user_mobile);
            tv_score.setText(bean.user_socre);
        }
    }

    @Override
    public void onClick(View v) {
        String node = CommonUtil.getApplication().getNode();
        switch (v.getId()) {
            case R.id.ll_account:
                LogUtil.i(this, "个人信息");
                if (TextUtils.isEmpty(node)) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), PersonalInformationActivity.class));
                }
                break;
            case R.id.iv_my_head:
                LogUtil.i(this, "头像详情界面");
                break;
            case R.id.ll_money:
                LogUtil.i(this, "我的退款");
                if (TextUtils.isEmpty(node)) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), RefundActivity.class));
                }
                break;
            case R.id.ll_bill:
                LogUtil.i(this, "我的发票");
                if (TextUtils.isEmpty(node)) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), BillActivity.class));
                }
                break;
            case R.id.ll_socre:
                LogUtil.i(this, "我的积分");
                if (TextUtils.isEmpty(node)) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), ScoreActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.ll_message_center:
                LogUtil.i(this, "消息中心");
//                if (TextUtils.isEmpty(node)) {
//                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
//                } else {
                    getActivity().startActivity(new Intent(getActivity(), MessageCenterActivity.class));
//                }
                break;
            case R.id.ll_setting:
                LogUtil.i(this, "设置");
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.tv_login:
                LogUtil.i(this, "登录或退出");
                if (TextUtils.isEmpty(node)) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    showDialog();
                }
                break;
        }
    }

    /**
     * 确认对话框
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出");//设置对话框标题
        builder.setMessage("您确认退出？");//设置显示的内容

        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                CommonUtil.getApplication().setNode("","退出成功");
                updateView();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.blue));
    }


    @Override
    public void onLoginSuccess() {
        updateView();
    }

    @Override
    public void onLoginError(String msg) {
        updateView();
    }
}
