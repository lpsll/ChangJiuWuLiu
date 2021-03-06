package util.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by sks on 2016/4/19.
 */
public class AliPayUtil {
    // 商户PID
    public static final String PARTNER = "2088121015509109";
    // 商户收款账号
    public static final String SELLER = "cjwlvxzfb@changjiulogistics.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALyFk+2gqFHH2joC" +
            "t3bbapZR9m4tAgD1T0shP/gR12lsSsGBv2mshBxGdh4PiosneR6pi+IydeX+Y9KZ" +
            "t8kmOWmj/Peupg06KGUejgFLMtkKEuGhPOazhv7Z07bMQpkrwaMLoxcFWZqNI7KY" +
            "UJcFe6oP9gwfl/h0gSnTDYj5m66BAgMBAAECgYB4Hqy49msJYMv9tolrpDbv/N2G" +
            "KgtizOtvNYpq6ANXWvbvAVsTXCb5VTQhx0lBjS8xiFpnEDvrBgJYyDVAZ6igOyxE" +
            "1/MgYhdAnYVGTdZIQ9OLlJeEU6zAJPmO6jMI5KXRNetjVhdBxUzVKlI6uJpBnmqy" +
            "BokKJ3KSW2PUtygLoQJBAPfYmvTy+xzHMENoI/lIARg69/p9o7YL2OeSYzLQSN1v" +
            "v3uyJfGDEbsqNNC6WUYrHAroFXSQu90A4BpVVpq+dgUCQQDCuVWyy/JQb/CkhniT" +
            "ZoIzthmj+0x8N7m08GqEVUwT6nFTtfuG9D53INJ473vIk/1VmI+B+LP5si3/2Ftn" +
            "CaNNAkAY3IN+urjxxBFVUab6JHznn1Ksr9myMzII2KhWuon0nHcKw24G8ezfZ3Zw" +
            "0aCO51gix/EPBk2x95qDSbgsQZq5AkEAneqqCneFiyBNG1CGppuiigdrYeZiD/ct" +
            "XwT+jVWTfi9KOuZCFx8rLXdr2XH2hCMod2tjLregVabf0TYIYlaZnQJBAKZ9FvOP" +
            "Mz1nWixQwM6mWX2+G1bgTf/QCOXzgtoOMzvlBalbXWVMMYMCQGiylIlxn8H/Lloc" +
            "5rOn9ymc+673kpQ=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";

    public static String getPayInfo(String name,String desc,String price, String orderId){
        String orderInfo = getOrderInfo(name, desc, price,orderId);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        return payInfo;
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private static String getOrderInfo(String subject, String body, String price,String orderId) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://t2.damaimob.com/Home/CYAlipay/ZfbNoticeURL" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private  static String getSignType() {
        return "sign_type=\"RSA\"";
    }
    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

}
