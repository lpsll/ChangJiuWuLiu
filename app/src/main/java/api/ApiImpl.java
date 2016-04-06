
package api;


import android.text.TextUtils;

import com.htlc.cjwl.App;

import java.util.HashMap;
import java.util.Map;

import api.net.okhttp.callback.ResultCallback;
import api.net.okhttp.request.OkHttpRequest;
import api.utils.EncryptUtil;

public class ApiImpl implements Api {
    @Override
    public void sendSmsCode(String username, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", username);
        String url = Api.SendSmsCode;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void register(String username, String pwd, String smsCode, ResultCallback<String> callback) {
        pwd = EncryptUtil.makeMD5(pwd);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_mobile", username);
        params.put("user_pwd", pwd);
        params.put("yzm", smsCode);
        String url = Api.Register;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void checkSmsCode(String username, String smsCode, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", username);
        params.put("yzm", smsCode);
        String url = Api.CheckSmsCode;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void settingPassword(String username, String pwd, ResultCallback<String> callback) {
        pwd = EncryptUtil.makeMD5(pwd);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_mobile", username);
        params.put("user_pwd", pwd);
        String url = Api.ResetPassword;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void login(String username, String pwd, ResultCallback<String> callback) {
        pwd = EncryptUtil.makeMD5(pwd);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_mobile", username);
        params.put("user_pwd", pwd);
        String url = Api.Login;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getUserInfo(ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        String userId = App.app.getUser().node;
        params.put("user_loginID", TextUtils.isEmpty(userId) ? "" : userId);
        String url = Api.GetUserInfo;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);

    }

    @Override
    public void checkPassword(String pwd, ResultCallback<String> callback) {
        pwd = EncryptUtil.makeMD5(pwd);
        Map<String, String> params = new HashMap<String, String>();
        String userId = App.app.getUser().node;
        params.put("user_loginID", TextUtils.isEmpty(userId) ? "" : userId);
        params.put("user_pwd", pwd);
        String url = Api.CheckPassword;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void updatePassword(String newPwd, ResultCallback<String> callback) {
        newPwd = EncryptUtil.makeMD5(newPwd);
        Map<String, String> params = new HashMap<String, String>();
        String userId = App.app.getUser().node;
        params.put("user_loginID", TextUtils.isEmpty(userId) ? "" : userId);
        params.put("user_newpwd", newPwd);
        String url = Api.UpdatePassword;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void resetTel(String newTel, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        String userId = App.app.getUser().node;
        params.put("user_loginID", TextUtils.isEmpty(userId) ? "" : userId);
        params.put("mobile", newTel);
        String url = Api.ResetTel;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void orderList(String order_status, String page, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        String userId = App.app.getUser().node;
        params.put("user_loginID", TextUtils.isEmpty(userId) ? "" : userId);
        params.put("order_status", order_status);
        params.put("page", order_status);
        String url = Api.OrderList;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void lastOrderDetail(ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        String userId = App.app.getUser().node;
        params.put("user_loginID", TextUtils.isEmpty(userId) ? "" : userId);
        String url = Api.LastOrderDetail;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void transportWay(String cityName, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        String userId = App.app.getUser().node;
        params.put("cityname", cityName);
        String url = Api.TransportWay;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void homeBanner(ResultCallback<String> callback) {
        String url = Api.HomeBanner;
        new OkHttpRequest.Builder().url(url).get(callback);
    }

    @Override
    public void serviceList(ResultCallback<String> callback) {
        String url = Api.ServiceList;
        new OkHttpRequest.Builder().url(url).get(callback);
    }

    @Override
    public void serviceLocalDetail(String serviceId, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("service_ID", serviceId);
        String url = Api.ServiceLocalDetail;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void hotCity(ResultCallback<String> callback) {
        String url = Api.HotCity;
        new OkHttpRequest.Builder().url(url).get(callback);
    }

    @Override
    public void cityList(ResultCallback<String> callback) {
        String url = Api.CityList;
        new OkHttpRequest.Builder().url(url).get(callback);
    }
}
