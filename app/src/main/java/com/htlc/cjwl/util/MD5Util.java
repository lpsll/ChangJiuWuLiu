package com.htlc.cjwl.util;

import java.security.MessageDigest;

/**
 * Created by sks on 2015/11/2.
 */
public class MD5Util {

    /**
     * 对一个字符串进行md5加密
     * @param s
     * @return
     */
        public final static String MD5(String s) {
            char hexDigits[] = { '0', '1', '2', '3', '4',
                    '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            try {
                byte[] btInput = s.getBytes();
                //获得MD5摘要算法的 MessageDigest 对象
                MessageDigest mdInst = MessageDigest.getInstance("MD5");
                //使用指定的字节更新摘要
                mdInst.update(btInput);
                //获得密文
                byte[] md = mdInst.digest();
                //把密文转换成十六进制的字符串形式
                int j = md.length;
                char str[] = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    /**
     * 测试加密算法
     * @param args
     */
        public static void main(String[] args) {
            String str = MD5Util.MD5("123456");
            System.out.println(str);
            System.out.println(str.equals("e10adc3949ba59abbe56e057f20f883e"));
        }

}
