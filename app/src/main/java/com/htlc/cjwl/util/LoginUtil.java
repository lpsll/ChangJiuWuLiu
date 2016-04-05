package com.htlc.cjwl.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Button;

import com.htlc.cjwl.App;
import com.htlc.cjwl.R;
import com.htlc.cjwl.activity.LoginActivity;

/**
 * Created by sks on 2015/12/21.
 */
public class LoginUtil {
    /**
     * 判断是否在线
     * @return
     */
    public static boolean isOnline(){
       return !TextUtils.isEmpty(App.app.getNode());
    }

    /**
     * context必须为Activity
     * @param context
     */
    public static void goLogin(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    /**
     *  context必须为Activity
     *  @param context
     */
    public static void showLoginDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");//设置对话框标题
        builder.setMessage("你未登录，是否去登录？");//设置显示的内容

        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                context.startActivity(new Intent(context, LoginActivity.class));
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
}
