
package core;


import com.htlc.cjwl.bean.AddressInfo;
import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.bean.HomeBannerInfo;
import com.htlc.cjwl.bean.MessageInfoBean;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.bean.ServiceDetailInfoBean;
import com.htlc.cjwl.bean.ServiceInfoBean;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import model.AddressInfoBean;
import model.BillDetailBean;
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


public interface AppAction {

    void sendSmsCode(String username, ActionCallbackListener<Void> listener);

    void register(String username, String pwd, String confirmPwd, String smsCode, ActionCallbackListener<Void> listener);

    void checkSmsCode(String username, String smsCode, ActionCallbackListener<Void> listener);

    void settingPassword(String username, String pwd, String confirmPwd, ActionCallbackListener<Void> listener);

    void login(String username, String pwd, ActionCallbackListener<UserBean> listener);

    void getUserInfo(ActionCallbackListener<UserBean> listener);
    void getNewMessage(ActionCallbackListener<Void> listener);

    void checkPassword(String pwd, ActionCallbackListener<Void> listener);

    void updatePassword(String newPwd, String confirmNewPwd, ActionCallbackListener<Void> listener);

    void resetTel(String newTel, ActionCallbackListener<Void> listener);

    void messageCenter(int page, ActionCallbackListener<ArrayList<MessageInfoBean>> listener);

    void messageDelete(String msgID, ActionCallbackListener<Void> listener);
    void feedback(String feedbackStr, ActionCallbackListener<Void> listener);
    void scoreList(int page,ActionCallbackListener<ArrayList<ScoreBean>> listener);
    void orderList(String order_status, int page, ActionCallbackListener<ArrayList<OrderInfoBean>> listener);

    void orderDetail(String orderId, ActionCallbackListener<OrderDetailBean> listener);

    void cancelOrder(String orderId, ActionCallbackListener<Void> listener);

    void refundOrderList(String order_status, int page, ActionCallbackListener<ArrayList<RefundOrderBean>> listener);

    void submitRefundOrder(String orderIdArrayStr, ActionCallbackListener<Void> listener);

    void billOrderList(int page, ActionCallbackListener<ArrayList<BillOrderBean>> listener);

    void billOrderListHistory(int page, ActionCallbackListener<ArrayList<BillDetailBean>> listener);

    void submitBillOrder(String billHeader, String price, String billType, String address, String receiverName, String orderIdStr, ActionCallbackListener<Void> listener);
    void billOrderDetail(String billId,ActionCallbackListener<BillDetailBean> listener);
    void billOrderModify(String billId, String header, String address, String receiver, ActionCallbackListener<Void> listener);
    void evaluationOrder(String orderId, String comment, String grade,ActionCallbackListener<Void> listener);
    void traceOrder(String orderId, ActionCallbackListener<ArrayList<TraceBean>> listener);
    void payOrderDetail(String orderId, ActionCallbackListener<PayOrderBean> listener);

    void pay(String orderId, String channel, String score, ActionCallbackListener<PayChargeBean> listener);

    void verifyPay(String payResultData, ActionCallbackListener<Void> listener);

    void lastOrderDetail(ActionCallbackListener<AddressInfoBean> listener);

    void carTypeList(ActionCallbackListener<ArrayList<CarTypeInfoBean>> listener);

    void carNameList(String carType, ActionCallbackListener<ArrayList<CarTypeInfoBean>> listener);

    void transportWay(String cityName, ActionCallbackListener<String> listener);

    void calculatePrice(String fromCity, String toCity, String fromCityDetail, String toCityDetail,
                        String sendWay, String getWay, ArrayList<CarInfoBean> carInfo, ArrayList<InsuranceInfoBean> insure, ActionCallbackListener<CalculatePriceInfoBean> listener);

    void orderCreate(String fromCity, String toCity, String fromCityDetail, String toCityDetail,
                     String fromName, String toName, String fromTel, String toTel, String fromIdCard, String toIdCard,
                     ArrayList<VinInfoBean> vinnum, ArrayList<CarInfoBean> carsInfo, String price, ArrayList<InsuranceInfoBean> insure, ActionCallbackListener<Void> listener);

    void homeBanner(ActionCallbackListener<ArrayList<HomeBannerInfo>> listener);

    void serviceList(ActionCallbackListener<ArrayList<ServiceInfoBean>> listener);

    void serviceLocalDetail(String serviceId, ActionCallbackListener<ServiceDetailInfoBean> listener);

    void hotCity(ActionCallbackListener<ArrayList<CityInfoBean>> listener);

    void cityList(ActionCallbackListener<String> listener);
}
