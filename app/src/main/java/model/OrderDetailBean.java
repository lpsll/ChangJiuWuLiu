package model;

import java.util.List;

/**
 * Created by sks on 2016/4/16.
 */
public class OrderDetailBean {

    public String order_no  ;
    public String to_name  ;
    public String to_address  ;
    public String order_price  ;
    public String from_name  ;
    public String from_mobile  ;
    public String order_date  ;
    public String order_status  ;
    public String to_cityname  ;
    public String to_mobile  ;
    public String from_address  ;
    public String from_cityname  ;
    public String evaluate  ;//评价
    public String cityaddress;//发车网点地址
    public String daodadate;//到达日期

    public List<OrderCarfNum> order_carfnum;
    public class OrderCarfNum  {

        public String price  ;
        public String carname  ;
        public String num  ;
        public String insureprice  ;

    }
}
