
package core;


import com.htlc.cjwl.bean.CityInfoBean;
import com.htlc.cjwl.bean.HomeBannerInfo;
import com.htlc.cjwl.bean.MessageInfoBean;
import com.htlc.cjwl.bean.OrderInfoBean;
import com.htlc.cjwl.bean.ServiceDetailInfoBean;
import com.htlc.cjwl.bean.ServiceInfoBean;

import java.util.ArrayList;

import model.UserBean;


public interface AppAction {

    void sendSmsCode(String username, ActionCallbackListener<Void> listener);

    void register(String username, String pwd, String confirmPwd, String smsCode, ActionCallbackListener<Void> listener);

    void checkSmsCode(String username, String smsCode, ActionCallbackListener<Void> listener);

    void settingPassword(String username, String pwd, String confirmPwd, ActionCallbackListener<Void> listener);

    void login(String username, String pwd, ActionCallbackListener<UserBean> listener);

    void getUserInfo(ActionCallbackListener<UserBean> listener);

    void checkPassword(String pwd, ActionCallbackListener<Void> listener);

    void updatePassword(String newPwd, String confirmNewPwd, ActionCallbackListener<Void> listener);

    void resetTel(String newTel, ActionCallbackListener<Void> listener);

    void messageCenter(int page, ActionCallbackListener<ArrayList<MessageInfoBean>> listener);

    void messageDelete(String msgID, ActionCallbackListener<Void> listener);

    void orderList(String order_status, int page, ActionCallbackListener<ArrayList<OrderInfoBean>> listener);

    void lastOrderDetail(ActionCallbackListener<String> listener);

    void transportWay(String cityName, ActionCallbackListener<String> listener);

    void homeBanner(ActionCallbackListener<ArrayList<HomeBannerInfo>> listener);

    void serviceList(ActionCallbackListener<ArrayList<ServiceInfoBean>> listener);

    void serviceLocalDetail(String serviceId, ActionCallbackListener<ServiceDetailInfoBean> listener);

    void hotCity(ActionCallbackListener<ArrayList<CityInfoBean>> listener);

    void cityList(ActionCallbackListener<String> listener);
}
