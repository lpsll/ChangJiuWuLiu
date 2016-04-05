package com.htlc.cjwl.util;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.htlc.cjwl.App;

import core.ActionCallbackListener;
import model.UserBean;


/**
 * Created by luochuan on 15/10/29.
 */

public class CommonUtil {
    /**
     * dp转xp
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    /**
     * 获取application对象
     * @return
     */
    public static App getApplication() {
        return App.app;
    }

    /**
     * 获取当前应用版本号
     * @return
     */
    public static String getVersionName() {
        try {
            String packageName = getApplication().getPackageName();
            LogUtil.i(getApplication(),packageName);
            PackageInfo packageInfo = getApplication().getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            LogUtil.i(getApplication(), "获取版本版本名出错");
        }
        return "1.0";
    }

    /**
     * 将执行的代码放到主线程
     * @param r
     */
    public static void runOnUIThread(Runnable r) {
        getApplication().getMainHandler().post(r);
    }

    /**
     * 从本地文件取密码，进行登录操作
     * 在启动应用时，进行自动登录
     */
    public static void login() {
        UserBean user = App.app.getUser();
        //帐号密码都不为空，进行登录操作
        if(!(TextUtils.isEmpty(user.user_mobile)) && !(TextUtils.isEmpty(user.pwd))){
            login(user.user_mobile,user.pwd);
        }
    }

    /**
     * 传入帐号密码，进行登录请求
     * @param username
     * @param password
     */
    public static void login(final String username, final String password) {
        App.appAction.login(username, password, new ActionCallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                data.user_mobile = username;
                data.pwd = password;
                App.app.setUser(data);
                App.app.setNode(data.node,"登录成功！");
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                App.app.setNode("", message);
            }
        });
    }
    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值,或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 获得资源文件中定义的颜色
     * @param colorId
     * @return
     */
    public static int getResourceColor(int colorId){
       return getApplication().getResources().getColor(colorId);
    }
    public static String[] getStringArray(int arrayId){
        return getApplication().getResources().getStringArray(arrayId);
    }
}

