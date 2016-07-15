package com.htlc.cjwl;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.htlc.cjwl.util.CommonUtil;
import com.htlc.cjwl.util.Constant;
import com.htlc.cjwl.util.SharedPreferenceUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import core.ActionCallbackListener;
import core.AppAction;
import core.AppActionImpl;
import model.UserBean;

/**
 * Created by sks on 2015/10/30.
 *
 *
 */
public class App extends Application{
    private String node;//登录状态
    public static App app;//全局上下文
    private static Handler mHandler;//全局的主线程handler
    private ArrayList<OnLoginListener> mLoginListeners = new ArrayList<OnLoginListener>();//登录状态监听
    public static  AppAction appAction;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appAction = new AppActionImpl(this);
        //在主线程创建handler
        mHandler = new Handler();
        initImageLoader(getApplicationContext());
        initJpush();
        //调用登录方法进行登录，如果 本地保存了 用户的帐号和密码
        CommonUtil.login();
        CrashHandler.getInstance().init(this);
    }

    /**
     * 初始化Jpush
     */
    private void initJpush() {
        JPushInterface.setDebugMode(util.Constant.isDebug);
        JPushInterface.init(this);
    }

    /**
     * 初始化ImageLoader
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        if(util.Constant.isDebug){
            config.writeDebugLogs(); // Remove for release app
        }


        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 获取全局的主线程handler
     * @return
     */
    public Handler getMainHandler(){
        return mHandler;
    }

    /*设置用户信息*/
    public UserBean getUser() {
        UserBean bean = new UserBean();
        bean.node = SharedPreferenceUtil.getString(app,Constant.USER_NODE,"");
        bean.user_mobile = SharedPreferenceUtil.getString(app,Constant.USERNAME,"");
        bean.pwd = SharedPreferenceUtil.getString(app,Constant.PASSWORD,"");
        bean.user_socre = SharedPreferenceUtil.getString(app,Constant.USER_SCORE,"0");
        bean.levelname = SharedPreferenceUtil.getString(app,Constant.USER_LEVEL,"");
        return bean;
    }
    /*获取用户信息*/
    public void setUser(UserBean bean){
        if(!TextUtils.isEmpty(bean.node)){
            SharedPreferenceUtil.putString(app,Constant.USER_NODE,bean.node);
        }
        if(!TextUtils.isEmpty(bean.user_mobile)){
            SharedPreferenceUtil.putString(app,Constant.USERNAME,bean.user_mobile);
        }
        if(!TextUtils.isEmpty(bean.pwd)){
            SharedPreferenceUtil.putString(app,Constant.PASSWORD,bean.pwd);
        }
        if(!TextUtils.isEmpty(bean.user_socre)){
            SharedPreferenceUtil.putString(app,Constant.USER_SCORE,bean.user_socre);
        }
        if(!TextUtils.isEmpty(bean.levelname)){
            SharedPreferenceUtil.putString(app,Constant.USER_LEVEL,bean.levelname);
        }
    }
    /*清除用户信息*/
    public void clearUser(){
        SharedPreferenceUtil.remove(app,Constant.USER_NODE);
        SharedPreferenceUtil.remove(app,Constant.USERNAME);
        SharedPreferenceUtil.remove(app,Constant.PASSWORD);
        SharedPreferenceUtil.remove(app,Constant.USER_SCORE);
        SharedPreferenceUtil.remove(app,Constant.USER_LEVEL);
    }
    /*获取用户id*/
    public String getNode() {
        return node;
    }
    /*设置用户id*/
    public void setNode(String node,@NonNull String msg) {
        this.node = node;
        if(TextUtils.isEmpty(node)){
            for (OnLoginListener listener : mLoginListeners) {
                listener.onLoginError(msg);
                clearUser();
            }
        }else{
            for (OnLoginListener listener : mLoginListeners) {
                listener.onLoginSuccess();
            }
        }
    }

    /**
     * 登录状态监听器
     */
    public interface OnLoginListener {
        public void onLoginSuccess();
        public void onLoginError(String msg);
    }

    /**
     * 添加登录状态监听
     * @param listener
     */
    public void addLoginListener(OnLoginListener listener){
        mLoginListeners.add(listener);
    }

    /**
     * 移除登录状态监听
     * @param listener
     */
    public void removeLoginListener(OnLoginListener listener){
        mLoginListeners.remove(listener);
    }
}
