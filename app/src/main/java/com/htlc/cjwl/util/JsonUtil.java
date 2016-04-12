package com.htlc.cjwl.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htlc.cjwl.bean.AddressInfo;
import com.htlc.cjwl.bean.CarInfo;
import com.htlc.cjwl.bean.CarMessege;
import com.htlc.cjwl.bean.CarMessegeBean;
import com.htlc.cjwl.bean.CarSendWay;
import com.htlc.cjwl.bean.City;
import com.htlc.cjwl.bean.Citys;
import com.htlc.cjwl.bean.HotCity;
import com.htlc.cjwl.bean.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by sks
 * 用于将json的数据与java 对象的相互转换
 */
public class JsonUtil {

    /**
     * 把一个map变成json字符串
     *
     * @param map
     * @return
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
        }
        return null;
    }

    public static String parseObjectToJson(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 把一个json字符串变成对象
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 把json字符串变成map
     *
     * @param json
     * @return
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> map = null;
        try {
            map = gson.fromJson(json, type);
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static List<?> parseJsonToList(String json, Type type) {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    /**
     * 获取json串中某个字段的值，注意，只能获取同一层级的value
     *
     * @param json
     * @param key
     * @return
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json))
            return null;
        if (!json.contains(key))
            return "";
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 解析 车辆及地址信息
     *
     * @param json
     * @return
     */
    public static CarMessegeBean getCarMessegeBean(String json) {

        CarMessegeBean carMessegeBean = new CarMessegeBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            carMessegeBean.setCode(jsonObject.optString("code"));
            carMessegeBean.setMsg(jsonObject.optString("msg"));
            if("0".equals(jsonObject.optString("code"))){

            }else {
                CarMessege carMessege = new CarMessege();
                JSONObject data = jsonObject.optJSONObject("data");
                JSONObject jsonObject1 = data.optJSONObject("addressInfo");
                AddressInfo addressInfo = new AddressInfo();
                addressInfo.setId(jsonObject1.optString("id"));
                Log.e("c", jsonObject1.toString());
                addressInfo.setFrom_address(jsonObject1.optString("from_address"));
                addressInfo.setFrom_mobile(jsonObject1.optString("from_mobile"));
                addressInfo.setFrom_name(jsonObject1.optString("from_name"));
                addressInfo.setFrom_cityname(jsonObject1.getString("from_cityname"));
                addressInfo.setTo_cityname(jsonObject1.getString("to_cityname"));
                addressInfo.setTo_address(jsonObject1.optString("to_address"));
                addressInfo.setTo_mobile(jsonObject1.optString("to_mobile"));
                addressInfo.setTo_name(jsonObject1.optString("to_name"));
                carMessege.setAddressInfo(addressInfo);
                List<CarInfo> list = new ArrayList<CarInfo>();
                CarInfo carInfo = new CarInfo();
                JSONArray jsonArray = data.optJSONArray("carList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                    carInfo.setId(jsonObject2.optString("id"));
                    carInfo.setCar_desc(jsonObject2.optString("car_desc"));
                    carInfo.setCar_name(jsonObject2.optString("car_name"));
                    list.add(carInfo);
                }
                carMessege.setCarList(list);
                //carMessege.setAddressInfo(addressInfo);
                carMessegeBean.setData(carMessege);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return carMessegeBean;
    }

    /**
     * 解析常用地址信息
     *
     * @param json
     * @return
     */
    public static List<AddressInfo> getAddressInfo(String json) {
        List<AddressInfo> list = new ArrayList<AddressInfo>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject1 = data.getJSONObject(i);
                AddressInfo addressInfo = new AddressInfo();
                addressInfo.setId(jsonObject1.optString("id"));

                addressInfo.setFrom_address(jsonObject1.getString("from_address"));
                addressInfo.setFrom_mobile(jsonObject1.optString("from_mobile"));
                addressInfo.setFrom_name(jsonObject1.optString("from_name"));
                addressInfo.setFrom_cityname(jsonObject1.getString("from_cityname"));
                addressInfo.setTo_cityname(jsonObject1.getString("to_cityname"));
                addressInfo.setTo_address(jsonObject1.getString("to_address"));
                addressInfo.setTo_mobile(jsonObject1.optString("to_mobile"));
                addressInfo.setTo_name(jsonObject1.optString("to_name"));
                list.add(addressInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 解析 获取出发目的城市
     *
     * @param json
     * @return
     */
    public static List<HotCity> getHotList(String json) {
        List<HotCity> list = new ArrayList<HotCity>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("hotCitys");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                HotCity hotCity = new HotCity();
                hotCity.setId(jsonObject2.getString("id"));
                hotCity.setCityname(jsonObject2.getString("cityname"));
                list.add(hotCity);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Citys getCityList(String json) {
        Citys citys = new Citys();
        try {
            JSONObject joj = new JSONObject(json);
            JSONObject data = joj.getJSONObject("data");
            JSONObject jsonObject = data.getJSONObject("list");
            JSONArray a = jsonObject.getJSONArray("A");
            List<City> listA = new ArrayList<City>();
            for (int i = 0; i < a.length(); i++) {
                JSONObject jsonObject2 = a.getJSONObject(i);
                City city = new City();
                city.setId(jsonObject2.getString("id"));
                city.setName(jsonObject2.getString("name"));
                listA.add(city);
            }
            citys.setA(listA);
            JSONArray b = jsonObject.getJSONArray("B");
            List<City> listB = new ArrayList<City>();
            for (int i = 0; i < b.length(); i++) {
                JSONObject jsonObject2 = a.getJSONObject(i);
                City city = new City();
                city.setId(jsonObject2.getString("id"));
                city.setName(jsonObject2.getString("name"));
                listB.add(city);
            }
            citys.setB(listB);
            JSONArray c = jsonObject.getJSONArray("C");
            List<City> listC = new ArrayList<City>();
            for (int i = 0; i < c.length(); i++) {
                JSONObject jsonObject2 = a.getJSONObject(i);
                City city = new City();
                city.setId(jsonObject2.getString("id"));
                city.setName(jsonObject2.getString("name"));
                listC.add(city);
            }
            citys.setC(listC);
            JSONArray d = jsonObject.getJSONArray("D");
            List<City> listD = new ArrayList<City>();
            for (int i = 0; i < d.length(); i++) {
                JSONObject jsonObject2 = a.getJSONObject(i);
                City city = new City();
                city.setId(jsonObject2.getString("id"));
                city.setName(jsonObject2.getString("name"));
                listD.add(city);
            }
            citys.setD(listD);
            JSONArray s = jsonObject.getJSONArray("S");
            List<City> listS = new ArrayList<City>();
            for (int i = 0; i < s.length(); i++) {
                JSONObject jsonObject2 = a.getJSONObject(i);
                City city = new City();
                city.setId(jsonObject2.getString("id"));
                city.setName(jsonObject2.getString("name"));
                listS.add(city);
            }
            citys.setS(listS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return citys;
    }

    /**
     *
     */
    public static CarSendWay getCarSendWay(String json) {
        CarSendWay carSendWay = new CarSendWay();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("method");
            List<Method> list = new ArrayList<Method>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Method method = new Method();
                method.setBasekilometre(jsonObject2.getString("basekilometre"));
                method.setBaseprice(jsonObject2.getString("baseprice"));
                method.setId(jsonObject2.getString("id"));
                method.setOverstep(jsonObject2.getString("overstep"));
                method.setOverstepprice(jsonObject2.getString("overstepprice"));
                method.setType(jsonObject2.getString("type"));
                list.add(method);
            }
            carSendWay.setMethod(list);
            carSendWay.setAddress(jsonObject1.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return carSendWay;
    }

}
