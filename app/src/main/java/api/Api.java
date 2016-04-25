
package api;

import api.net.okhttp.callback.ResultCallback;
import core.ActionCallbackListener;

public interface Api {
    String[] TransportWayArray = {"0","2"};//index=0 为 长久送车
    String[] PayWayArray = {"1","2","3","4"};//支付宝，微信，银联，现付

    String Host = "http://t2.damaimob.com/";
    String SendSmsCode = Host + "Home/CYUser/getMobileCode";
    String Register = Host + "Home/CYUser/user_register";
    String CheckSmsCode = Host + "Home/CYUser/reSetUserPwd";
    String ResetPassword = Host + "Home/CYUser/reSetUserPwdSecond";
    String Login = Host + "Home/CYUser/user_login";
    String GetUserInfo = Host + "Home/CYUser/getUserInfo";
    String CheckPassword = Host + "Home/CYUser/checkUserPwd";
    String UpdatePassword = Host + "Home/CYUser/user_updatepwd";
    String ResetTel = Host + "Home/CYUser/reSetUserMobile";
    String MessageCenter = Host + "Home/CYMessage/messageList";
    String MessageHtmlDetail = Host + "Home/CYMessage/info?mID=%1$s";
    String MessageDelete = Host + "Home/CYMessage/removeMessage";
    String Feedback = Host + "Home/CYFeedback/addFeedback";
    String ScoreList = Host + "Home/CYIntegral/getIntegrallist";

    String OrderList = Host + "Home/CYOrder/getOrderList";
    String OrderDetail = Host + "Home/CYOrder/getOrderinfo";
    String CancelOrder = Host + "Home/CYOrder/cancelOrder";
    String RefundOrderList = Host + "Home/CYOrder/getrepayList";
    String SubmitRefundOrder = Host + "Home/CYOrder/repayquest";
    String BillOrderList = Host + "Home/CYOrder/getbillList";
    String BillOrderListHistory = Host + "Home/CYEvaluation/getinvoicelist";
    String SubmitBillOrder = Host + "Home/CYOrder/rebillquest";
    String BillOrderDetail = Host + "Home/CYEvaluation/getinvoiceinfo";
    String BillOrderModify = Host + "Home/CYEvaluation/setrebill";
    String EvaluationOrder = Host + "Home/CYEvaluation/setEvaluation";
    String TraceOrder = Host + "Home/CYLogistics/getLogistics";

    String PayOrderDetail = Host + "Home/CYPay/getpayinfo";
    String Pay = Host + "Home/CYPay/pay";
    String VerifyPay = Host + "Home/CYYlpay/validateinfo";

    String LastOrderDetail = Host + "Home/CYTransport/transportCarInfo";
    String CarTypeList = Host + "Home/CYTransport/getbrandlist";
    String CarNameList = Host + "Home/CYTransport/getcarclasslist";
    String TransportWay = Host + "Home/CYTransport/transportMethod";
    String CalculatePrice = Host + "Home/CYTransport/transportCalculateprice";
    String ProtocolTransport = Host + "Home/CYHtml/gettransportagreement";
    String ProtocolRefund = Host + "Home/CYHtml/getrefundrule";
    String ProtocolService = Host + "Home/CYHtml/getserviceagreement";
    String ProtocolScoreRuler = Host + "Home/CYHtml/getintegralrule";
    String ProtocolAbout = Host + "Home/CYHtml/getrelatedsoftware";
    String ProtocolUser = Host + "Home/CYHtml/getuseragreement";
    String ProtocolInsurance = Host + "Home/CYHtml/getinsurance";
    String OrderCreate = Host + "Home/CYOrder/createOrder";

    String HomeBanner = Host + "Home/CYBanner/getBannerList";
    String ServiceList = Host + "Home/CYService/getServiceList";
    String ServiceLocalDetail = Host + "Home/CYService/getServiceInfo";
    String ServiceHtmlDetail = Host + "Home/CYService/info?service_ID=%1$s";
    String HotCity = Host + "Home/CYCitys/hotCitysList";
    String CityList = Host + "Home/CYCitys/moreHotCitysList";
    String CityHtmlDetail = Host + "Home/CYCitys/cityInfo?id=%1$s";



    /**
     * 获取验证码
     * @param username 手机号码
     * @return 成功时返回：{ "code": 0, "msg":"success" }
     */
    void sendSmsCode(String username, ResultCallback<String> callback);

    /**
     *
     * @param username 手机号
     * @param pwd   md5加密的密码
     * @param smsCode 验证码
     * @param callback 返回说明：{"code": "1","msg": "注册成功","data": []}
     */
    void register(String username, String pwd, String smsCode, ResultCallback<String> callback);

    /**
     *
     * @param username 手机号
     * @param smsCode 验证码
     * @param callback 返回说明：{"code": "1","msg": "请求成功","data": []}
     */
    void checkSmsCode(String username, String smsCode, ResultCallback<String> callback);

    /**
     *
     * @param username 手机号
     * @param pwd md5加密的密码
     * @param callback 返回说明：{"code": "1","msg": "请求成功","data": []}
     */
    void settingPassword(String username, String pwd, ResultCallback<String> callback);

    /**
     *
     * @param username 手机号
     * @param pwd md5加密的密码
     * @param callback 返回说明：{"code": "1","msg": "登录成功","data": {"node": "1"}}
     */
    void login(String username, String pwd, ResultCallback<String> callback);

    /**
     *
     * @param callback 返回说明：{"code": "1","msg": "请求成功","data": {"user_mobile": "13020017428","user_socre": "200","levelname": "2"}
     */
    void getUserInfo(ResultCallback<String> callback);

    /**
     *
     * @param pwd md5加密的密码
     * @param callback
     */
    void checkPassword(String pwd, ResultCallback<String> callback);

    /**
     *
     * @param newPwd md5加密的密码
     * @param callback
     */
    void updatePassword(String newPwd, ResultCallback<String> callback);

    /**
     *
     * @param newTel 新手机号
     * @param callback
     */
    void resetTel(String newTel, ResultCallback<String> callback);

    /**
     * 消息中心，
     * @param page page从1开始
     * @param callback
     */
    void messageCenter(String page, ResultCallback<String> callback);

    /**
     * 删除指定消息
     * @param msgID
     * @param callback
     */
    void messageDelete(String msgID, ResultCallback<String> callback);

    void feedback(String feedbackStr, ResultCallback<String> callback);

    void scoreList(String page,ResultCallback<String> callback);
    /**
     *
     * @param order_status 订单类型
     * @param page 页码 从1开始
     * @param callback
     */
    void orderList(String order_status, String page, ResultCallback<String> callback);

    void orderDetail(String orderId, ResultCallback<String> callback);

    void cancelOrder(String orderId, ResultCallback<String> callback);

    void refundOrderList(String order_status,String page, ResultCallback<String> callback);

    void submitRefundOrder(String orderIdArrayStr,ResultCallback<String> callback);

    void billOrderList(String page, ResultCallback<String> callback);

    void billOrderListHistory(String page, ResultCallback<String> callback);

    void submitBillOrder(String billHeader, String price,String billType, String address, String receiverName, String orderIdStr, ResultCallback<String> callback);
    void billOrderDetail(String billId, ResultCallback<String> callback);
    void billOrderModify(String billId, String header, String address, String receiver, ResultCallback<String> callback);
    void evaluationOrder(String orderId, String comment, String grade, ResultCallback<String> callback);
    void traceOrder(String orderId, ResultCallback<String> callback);
    void payOrderDetail(String orderId, ResultCallback<String> callback);

    void pay(String orderId, String channel, String score,ResultCallback<String> callback);
    void verifyPay(String payResultData, ResultCallback<String> callback);
    /**
     * 获取最后一笔订单的详细信息
     * @param callback
     */
    void lastOrderDetail(ResultCallback<String> callback);

    /**
     * 获取汽车品牌列表
     * @param callback
     */
    void carTypeList(ResultCallback<String> callback);

    /**
     * 根据汽车品牌，获取汽车型号列表
     * @param carType
     * @param callback
     */
    void carNameList(String carType, ResultCallback<String> callback);

    /**
     * 设置发车或取车方式时，用于获取当前城市营业网点详情
     * @param cityName 城市名称
     * @param callback
     */
    void transportWay(String cityName, ResultCallback<String> callback);

    /**
     * 计算运输价格
     * @param fromCity
     * @param toCity
     * @param fromCityDetail
     * @param toCityDetail
     * @param sendWay
     * @param getWay
     * @param carInfo
     * @param callback
     */
    void calculatePrice(String fromCity,String toCity, String fromCityDetail, String toCityDetail,
                        String sendWay, String getWay, String carInfo, String insure,
                        ResultCallback<String> callback);

    void orderCreate(String fromCity, String toCity, String fromCityDetail, String toCityDetail, String fromName, String toName,
                     String fromTel, String toTel, String fromIdCard, String toIdCard,
                     String vinnum, String carsInfo, String price, String insure, ResultCallback<String> callback);
    /**
     *
     * @param callback
     * 返回说明：{"code": "1","msg": "请求成功","data": [{"id": "3","image": "http://t2.IC5d.jpg",--图片地址
        "weburl": "http://t2.info?id=3.html"--web页面地址
        }]}
     */
    void homeBanner(ResultCallback<String> callback);

    /**
     * 返回说明：{"code": "1","msg": "请求成功","data": [{"id": "1",'service_type':"10" 10--显示下单按钮 9 不显示下单按钮
     * "service_image": "/upload/20151027/zjy.jpg"}]}
     * @param callback
     */
    void serviceList(ResultCallback<String> callback);

    /**
     *
     * @param serviceId
     * @param callback 返回说明：{"code": "1","msg": "请求成功","data": {"id": "1","service_name": "自驾游",
                    "service_subimages": "http://t2.d6373768.jpg",
                    "service_desc": "库布其沙漠是是击的中场不惬意"}}
     */
    void serviceLocalDetail(String serviceId, ResultCallback<String> callback);

    /**
     * 热门城市列表
     * @param callback 返回说明：{"code": "1","msg": "密码修改成功","data": [{"id": "1","cityname": "安阳"}]}
     */
    void hotCity(ResultCallback<String> callback);

    void cityList(ResultCallback<String> callback);
}
