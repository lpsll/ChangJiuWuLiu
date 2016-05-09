
package core;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.htlc.cjwl.App;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.bean.HomeBannerInfo;
import com.htlc.cjwl.bean.MessageInfoBean;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.bean.ServiceDetailInfoBean;
import com.htlc.cjwl.bean.ServiceInfoBean;
import com.htlc.cjwl.util.JsonUtil;

import model.BillDetailBean;
import util.DateFormatUtil;
import util.LogUtil;

import com.htlc.cjwl.util.SharedPreferenceUtil;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import api.Api;
import api.ApiImpl;
import api.net.okhttp.callback.ResultCallback;
import model.AddressInfoBean;
import model.BillOrderBean;
import model.CalculatePriceInfoBean;
import model.CarInfoBean;
import model.CarTypeInfoBean;
import model.InsuranceInfoBean;
import model.OrderDetailBean;
import model.PayChargeBean;
import model.PayOrderBean;
import model.RefundOrderBean;
import model.ScoreBean;
import model.TraceBean;
import model.UserBean;
import model.VinInfoBean;
import util.RegExUtil;


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
        if (!RegExUtil.matcherPhoneNumber(username)) {
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
                } catch (Exception e) {
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
        if (!RegExUtil.matcherPhoneNumber(username)) {
            listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        if (pwd.length() < 5) {
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
                } catch (Exception e) {
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
        if (!RegExUtil.matcherPhoneNumber(username)) {
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
                } catch (Exception e) {
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
        if (pwd.length() < 5) {
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
                } catch (Exception e) {
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
        if (!RegExUtil.matcherPhoneNumber(username)) {
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
                } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void getNewMessage(final ActionCallbackListener<Void> listener) {
        api.getNewMessage(new ResultCallback<String>() {
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
                } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void updatePassword(String newPwd, String confirmNewPwd, final ActionCallbackListener<Void> listener) {
        if (TextUtils.isEmpty(newPwd)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            return;
        }
        if (newPwd.length() < 5) {
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
                } catch (Exception e) {
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
        if (!RegExUtil.matcherPhoneNumber(newTel)) {
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
                } catch (Exception e) {
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
                        SharedPreferenceUtil.putString(App.app, Api.GetNewMessage, DateFormatUtil.getTimeBySecond(new Date(System.currentTimeMillis())));
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void feedback(String feedbackStr, final ActionCallbackListener<Void> listener) {
        if(TextUtils.isEmpty(feedbackStr)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "内容不能为空");
            return;
        }
        api.feedback(feedbackStr, new ResultCallback<String>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void scoreList(int page,final ActionCallbackListener<ArrayList<ScoreBean>> listener) {
        api.scoreList(page+"",new ResultCallback<String>() {
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
                        ArrayList<ScoreBean> array = (ArrayList<ScoreBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<ScoreBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void orderList(final String order_status, final int page, final ActionCallbackListener<ArrayList<OrderInfoBean>> listener) {
        LogUtil.e(this, "page:" + page + "------order_status:" + order_status);
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void orderDetail(String orderId, final ActionCallbackListener<OrderDetailBean> listener) {
        api.orderDetail(orderId, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    Log.e("OrderDetail", "onResponse= " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonObjStr = jsonObject.getString("data");
                        OrderDetailBean bean = JsonUtil.parseJsonToBean(jsonObjStr, OrderDetailBean.class);
                        listener.onSuccess(bean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void cancelOrder(String orderId, final ActionCallbackListener<Void> listener) {
        api.cancelOrder(orderId, new ResultCallback<String>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void refundOrderList(final String order_status, final int page, final ActionCallbackListener<ArrayList<RefundOrderBean>> listener) {
        api.refundOrderList(order_status, page + "", new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    Log.e("refundOrderList", "page:" + page + "------order_status:" + order_status);
                    Log.e("refundOrderList", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonArray = jsonObject.getString("data");
                        ArrayList<RefundOrderBean> array = (ArrayList<RefundOrderBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<RefundOrderBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void submitRefundOrder(String orderIdArrayStr, final ActionCallbackListener<Void> listener) {
        api.submitRefundOrder(orderIdArrayStr, new ResultCallback<String>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void billOrderList(final int page, final ActionCallbackListener<ArrayList<BillOrderBean>> listener) {
        api.billOrderList(page + "", new ResultCallback<String>() {
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
                        ArrayList<BillOrderBean> array = (ArrayList<BillOrderBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<BillOrderBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void billOrderListHistory(final int page, final ActionCallbackListener<ArrayList<BillDetailBean>> listener) {
        api.billOrderListHistory(page + "", new ResultCallback<String>() {
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
                        ArrayList<BillDetailBean> array = (ArrayList<BillDetailBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<BillDetailBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void submitBillOrder(String billHeader, String price, String billType, String address, String receiverName, String orderIdStr,String phone,  final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(billHeader)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "发票抬头不能为空");
            return;
        }
        if (TextUtils.isEmpty(billType)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入发票类型");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入寄送地址");
            return;
        }
        if (TextUtils.isEmpty(receiverName)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入收票人");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入收票人联系电话");
            return;
        }
        api.submitBillOrder(billHeader, price, billType, address, receiverName, orderIdStr, phone, new ResultCallback<String>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void billOrderDetail(String billId, final ActionCallbackListener<BillDetailBean> listener) {
        api.billOrderDetail(billId, new ResultCallback<String>() {
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
                        String jsonObjStr = jsonObject.getString("data");
                        BillDetailBean bean = JsonUtil.parseJsonToBean(jsonObjStr, BillDetailBean.class);
                        listener.onSuccess(bean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void billOrderModify(String billId, String header, String address, String receiver,String phone, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(header)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "发票抬头不能为空");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入寄送地址");
            return;
        }
        if (TextUtils.isEmpty(receiver)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入收票人");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入收票人联系电话");
            return;
        }
        api.billOrderModify(billId, header, address, receiver,phone, new ResultCallback<String>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void evaluationOrder(String orderId, String comment, String grade, final ActionCallbackListener<Void> listener) {
        if(TextUtils.isEmpty(comment)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "请填写评价内容");
            return;
        }
        api.evaluationOrder(orderId, comment, grade, new ResultCallback<String>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void traceOrder(String orderId, final ActionCallbackListener<ArrayList<TraceBean>> listener) {
        api.traceOrder(orderId, new ResultCallback<String>() {
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
                        ArrayList<TraceBean> array = (ArrayList<TraceBean>) JsonUtil.parseJsonToList(jsonArray,
                                new TypeToken<ArrayList<TraceBean>>() {
                                }.getType());
                        listener.onSuccess(array);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void payOrderDetail(String orderId, final ActionCallbackListener<PayOrderBean> listener) {
        api.payOrderDetail(orderId, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
            }

            @Override
            public void onResponse(String response) {
                try {
                    Log.e("OrderDetail", "onResponse= " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("1".equals(code)) {
                        String jsonObjStr = jsonObject.getString("data");
                        PayOrderBean bean = JsonUtil.parseJsonToBean(jsonObjStr, PayOrderBean.class);
                        listener.onSuccess(bean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void pay(String orderId, String channel, String score, final ActionCallbackListener<PayChargeBean> listener) {
        api.pay(orderId, channel, score, new ResultCallback<String>() {
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
                        JSONObject jsonObjStr = jsonObject.getJSONObject("data");
                        PayChargeBean bean = new PayChargeBean();
                        bean.tn = jsonObjStr.getString("tn");
                        listener.onSuccess(bean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void verifyPay(String payResultData, final ActionCallbackListener<Void> listener) {
        api.verifyPay(payResultData, new ResultCallback<String>() {
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
                } catch (Exception e) {
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
                        AddressInfoBean addressInfoBean = JsonUtil.parseJsonToBean(address, AddressInfoBean.class);
                        listener.onSuccess(addressInfoBean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
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
                        ArrayList<CarTypeInfoBean> list = (ArrayList<CarTypeInfoBean>) JsonUtil.parseJsonToList(jsonArray, new TypeToken<ArrayList<CarTypeInfoBean>>() {
                        }.getType());
                        listener.onSuccess(list);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
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
                        ArrayList<CarTypeInfoBean> list = (ArrayList<CarTypeInfoBean>) JsonUtil.parseJsonToList(jsonArray, new TypeToken<ArrayList<CarTypeInfoBean>>() {
                        }.getType());
                        listener.onSuccess(list);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void calculatePrice(String fromCity, String toCity, String fromCityDetail, String toCityDetail,
                               String sendWay, String getWay, ArrayList<CarInfoBean> carInfo, ArrayList<InsuranceInfoBean> insure, final ActionCallbackListener<CalculatePriceInfoBean> listener) {
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
        } else if (!Api.TransportWayArray[0].equals(sendWay)) {
            fromCityDetail = "";
        }
        if (Api.TransportWayArray[0].equals(getWay) && TextUtils.isEmpty(fromCityDetail)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请输入详细目的地址");
            return;
        } else if (!Api.TransportWayArray[0].equals(getWay)) {
            toCityDetail = "";
        }
        if (carInfo.size() <= 0) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请选择汽车");
            return;
        }
        String carsInfo = JsonUtil.parseObjectToJson(carInfo);
        String insureStr = JsonUtil.parseObjectToJson(insure);
        api.calculatePrice(fromCity, toCity, fromCityDetail, toCityDetail, sendWay, getWay, carsInfo, insureStr, new ResultCallback<String>() {
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
                        CalculatePriceInfoBean bean = JsonUtil.parseJsonToBean(jsonStr, CalculatePriceInfoBean.class);
                        listener.onSuccess(bean);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void orderCreate(String fromCity, String toCity, String fromCityDetail, String toCityDetail, String fromName, String toName,
                            String fromTel, String toTel, String fromIdCard, String toIdCard,
                            ArrayList<VinInfoBean> vinnum, ArrayList<CarInfoBean> carsInfo, String price, ArrayList<InsuranceInfoBean> insure, final ActionCallbackListener<Void> listener) {
        if (TextUtils.isEmpty(fromName)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请填写发货人");
            return;
        }
        if (TextUtils.isEmpty(toName)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请填写收货人");
            return;
        }
        if (TextUtils.isEmpty(fromTel)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请填写发货人联系电话");
            return;
        }
        if(!RegExUtil.matcherPhoneNumber(fromTel)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "发货人联系电话不正确");
            return;
        }
        if (TextUtils.isEmpty(toTel)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请填写收货人联系电话");
            return;
        }
        if(!RegExUtil.matcherPhoneNumber(toTel)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "收货人联系电话不正确");
            return;
        }
        if (TextUtils.isEmpty(fromIdCard)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请填写发货人身份证号");
            return;
        }
        if(!RegExUtil.matcherIdCard(fromIdCard)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "发货人身份证号不正确");
            return;
        }
        if (TextUtils.isEmpty(toIdCard)) {
            listener.onFailure(ErrorEvent.PARAM_NULL, "请填写收货人身份证号");
            return;
        }
        if(!RegExUtil.matcherIdCard(toIdCard)){
            listener.onFailure(ErrorEvent.PARAM_NULL, "收货人身份证号不正确");
            return;
        }
        String carsInfoStr = JsonUtil.parseObjectToJson(carsInfo);
        String vinnumStr = JsonUtil.parseObjectToJson(vinnum);
        String insureStr = JsonUtil.parseObjectToJson(insure);
        Log.e("AppAction", carsInfoStr + "\n" + vinnumStr + "\n" + insureStr);
        api.orderCreate(fromCity, toCity, fromCityDetail, toCityDetail, fromName, toName,
                fromTel, toTel, fromIdCard, toIdCard,
                vinnumStr, carsInfoStr, price, insureStr, new ResultCallback<String>() {
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
                        } catch (Exception e) {
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
                } catch (Exception e) {
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
                } catch (Exception e) {
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
                } catch (Exception e) {
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
                } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }

    @Override
    public void cityListForAddress(boolean isFrom, final ActionCallbackListener<String> listener) {
        String flag = "1";//出发城市
        if(!isFrom){
            flag = "2";//目的城市
        }
        api.cityListForAddress(flag, new ResultCallback<String>() {
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
                    String data = jsonObject.getString("data");
                    if ("1".equals(code)) {
                        listener.onSuccess(data);
                    } else {
                        String msg = jsonObject.getString("msg");
                        listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, "网络出错！");
                }
            }
        });
    }
}
