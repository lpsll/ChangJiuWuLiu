package com.htlc.cjwl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luochuan on 15/11/19.
 */
public class PhoneUtil {
    // 电话号码正则表达式
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
        }
}
