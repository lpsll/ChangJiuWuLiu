
package core;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htlc.cjwl.App;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.bean.HomeBannerInfo;
import com.htlc.cjwl.bean.MessageInfoBean;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.bean.ServiceDetailInfoBean;
import com.htlc.cjwl.bean.ServiceInfoBean;
import com.htlc.cjwl.util.JsonUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.Api;
import api.ApiImpl;
import api.net.okhttp.callback.ResultCallback;
import model.AddressInfoBean;
import model.CalculatePriceInfoBean;
import model.CarInfoBean;
import model.CarTypeInfoBean;
import model.UserBean;


public class AppActionImpl implements AppAction {

    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void sendSmsCode(final String username, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        api.sendSmsCode(username, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        listener.onSuccess(null);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });

    }

    @Override
    public void register(String username, String pwd, String confirmPwd, String smsCode, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        if (pwd.length()<5) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码至少为5位");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不一致");
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            return;
        }
        api.register(username, pwd, smsCode, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        listener.onSuccess(null);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void checkSmsCode(String username, String smsCode, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            return;
        }
        api.checkSmsCode(username, smsCode, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        listener.onSuccess(null);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void settingPassword(String username, String pwd, String confirmPwd, final ActionCallbackListener<Void> listener) {
        if (TextUtils.isEmpty(pwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        if (pwd.length()<5) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码至少为5位");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不一致");
            return;
        }
        api.settingPassword(username, pwd, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        listener.onSuccess(null);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void login(String username, String pwd, final ActionCallbackListener<UserBean> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        api.login(username, pwd, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonObj = jsonObject.getString("data");
                        UserBean userBean = JsonUtil.parseJsonToBean(jsonObj, UserBean.class);
                        listener.onSuccess(userBean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void getUserInfo(final ActionCallbackListener<UserBean> listener) {
        api.getUserInfo(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonObj = jsonObject.getString("data");
                        UserBean userBean = JsonUtil.parseJsonToBean(jsonObj, UserBean.class);
                        listener.onSuccess(userBean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void checkPassword(String pwd, final ActionCallbackListener<Void> listener) {
        if (TextUtils.isEmpty(pwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        api.checkPassword(pwd, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        listener.onSuccess(null);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void updatePassword(String newPwd,String confirmNewPwd, final ActionCallbackListener<Void> listener) {
        if (TextUtils.isEmpty(newPwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        if (newPwd.length()<5) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码至少为5位");
            return;
        }
        if (!newPwd.equals(confirmNewPwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不一致");
            return;
        }
        api.updatePassword(newPwd, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        listener.onSuccess(null);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void resetTel(String newTel, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(newTel)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "手机号不能为空");
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(newTel);
        if (!matcher.matches()) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            return;
        }
        api.resetTel(newTel, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        listener.onSuccess(null);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void messageCenter(int page, final ActionCallbackListener<ArrayList<MessageInfoBean>> listener) {
        api.messageCenter(page + "", new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<MessageInfoBean> array = (ArrayList<MessageInfoBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<MessageInfoBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void messageDelete(String msgID, final ActionCallbackListener<Void> listener) {
       api.messageDelete(msgID, new ResultCallback<String>() {
           @Override
           public void onError(Request request, Exception e) {
               e.printStackTrace();
               listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
           }

           @Override
           public void onResponse(String response) {
               try {
                   JSONObject jsonObject = new JSONObject(response);
                   String code = jsonObject.getString("code");
                   if ("1".equals(code)) {
                       listener.onSuccess(null);
                   } else {
                       String msg = jsonObject.getString("msg");
                       listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
                   listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
               }
           }
       });
    }

    @Override
    public void orderList(String order_status, int page, final ActionCallbackListener<ArrayList<OrderInfoBean>> listener) {
        api.orderList(order_status, page + "", new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<OrderInfoBean> array = (ArrayList<OrderInfoBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<OrderInfoBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void lastOrderDetail(final ActionCallbackListener<AddressInfoBean> listener) {
        api.lastOrderDetail(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String address = jsonObject.getJSONObject("data").getString("addressInfo");
                        AddressInfoBean addressInfoBean = JsonUtil.parseJsonToBean(address,AddressInfoBean.class);
                        listener.onSuccess(addressInfoBean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void carTypeList(final ActionCallbackListener<ArrayList<CarTypeInfoBean>> listener) {
        api.carTypeList(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<CarTypeInfoBean> list = (ArrayList<CarTypeInfoBean>) JsonUtil.parseJsonToList(jsonArray,new TypeToken<ArrayList<CarTypeInfoBean>>(){}.getType());
                        listener.onSuccess(list);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void carNameList(String carType, final ActionCallbackListener<ArrayList<CarTypeInfoBean>> listener) {
        api.carNameList(carType, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<CarTypeInfoBean> list = (ArrayList<CarTypeInfoBean>) JsonUtil.parseJsonToList(jsonArray,new TypeToken<ArrayList<CarTypeInfoBean>>(){}.getType());
                        listener.onSuccess(list);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void transportWay(String cityName, final ActionCallbackListener<String> listener) {
        api.transportWay(cityName, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String address = jsonObject.getJSONObject("data").getString("address");
                        listener.onSuccess(address);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void calculatePrice(String fromCity, String toCity, String fromCityDetail, String toCityDetail,
                               String sendWay, String getWay, ArrayList<CarInfoBean> carInfo, final ActionCallbackListener<CalculatePriceInfoBean> listener) {
        if (TextUtils.isEmpty(fromCity)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择出发城市");
            return;
        }
        if (TextUtils.isEmpty(toCity)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择目的城市");
            return;
        }
        if (Api.TransportWayArray[0].equals(sendWay) && TextUtils.isEmpty(fromCityDetail)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入详细出发地址");
            return;
        }else if(!Api.TransportWayArray[0].equals(sendWay)){
            fromCityDetail = "";
        }
        if (Api.TransportWayArray[0].equals(getWay) && TextUtils.isEmpty(fromCityDetail)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入详细目的地址");
            return;
        }else if(!Api.TransportWayArray[0].equals(getWay)){
            toCityDetail = "";
        }
        if(carInfo.size()<=0){
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择汽车");
            return;
        }
        String carsInfo = JsonUtil.parseObjectToJson(carInfo);
        Log.e("AppAction",carsInfo);
        api.calculatePrice(fromCity, toCity, fromCityDetail, toCityDetail, sendWay, getWay, carsInfo, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonStr = jsonObject.getString("data");
                        CalculatePriceInfoBean bean = JsonUtil.parseJsonToBean(jsonStr,CalculatePriceInfoBean.class);
                        listener.onSuccess(bean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void homeBanner(final ActionCallbackListener<ArrayList<HomeBannerInfo>> listener) {
        api.homeBanner(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<HomeBannerInfo> array = (ArrayList<HomeBannerInfo>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<HomeBannerInfo>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void serviceList(final ActionCallbackListener<ArrayList<ServiceInfoBean>> listener) {
        api.serviceList(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<ServiceInfoBean> array = (ArrayList<ServiceInfoBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<ServiceInfoBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void serviceLocalDetail(String serviceId, final ActionCallbackListener<ServiceDetailInfoBean> listener) {
        api.serviceLocalDetail(serviceId, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonObj = jsonObject.getString("data");
                        ServiceDetailInfoBean bean = JsonUtil.parseJsonToBean(jsonObj, ServiceDetailInfoBean.class);
                        listener.onSuccess(bean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void hotCity(final ActionCallbackListener<ArrayList<CityInfoBean>> listener) {
        api.hotCity(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<CityInfoBean> array = (ArrayList<CityInfoBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<CityInfoBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void cityList(final ActionCallbackListener<String> listener) {
        api.cityList(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                       listener.onSuccess(response);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }
}
