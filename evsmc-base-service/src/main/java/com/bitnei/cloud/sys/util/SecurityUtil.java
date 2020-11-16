package com.bitnei.cloud.sys.util;

import jodd.util.RandomString;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by chenp on 2015/6/11.
 */
public class SecurityUtil {

    /**
     * 获取MD5码
     * 生成的是32位全小写的加密字符串
     *
     * @param s
     * @return
     */
    public static final String getMd5(String s) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成随机密码
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        return RandomString.getInstance().randomNumeric(length);
    }

    public static void main(String[] args) {
        System.out.println(getMd5("1"));
    }
}
