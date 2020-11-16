package com.bitnei.cloud.screen.protocol;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.sauronsoftware.base64.Base64;

public class StringUtil {


    /**
     * 判断字符串是否为空；true为空，false不为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        String cloneStr = str == null ? "" : str.trim();
        if (null != cloneStr && !"".equals(cloneStr)) {
            return false;
        }
        return true;
    }

    //将字符串根据,号转换为数组
    public static String[] split(String source) {
        if (source == null || source.trim().equals("")) {
            return null;
        }
        StringTokenizer commaToker = new StringTokenizer(source, ",");
        String[] result = new String[commaToker.countTokens()];
        int i = 0;
        while (commaToker.hasMoreElements()) {
            result[i] = commaToker.nextToken();
            i++;
        }
        return result;
    }

    /**
     * 一维数组转换成二维数组
     *
     * @param arr          一维数组
     * @param colsize      长度
     * @param defaultValue
     * @return 替换后的二维数组
     */
    public static String[][] transOneToTow(String[] arr, int colsize, String defaultValue) {
        int rows = (arr.length + colsize) / colsize;
        String[][] arr2 = new String[rows][colsize];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < colsize; col++) {
                if (row * colsize + col < arr.length) {
                    arr2[row][col] = arr[row * colsize + col];
                } else {
                    arr2[row][col] = defaultValue;
                }
            }
        }
        return arr2;
    }


    // 将 BASE64 编码的字符串 s 进行编码
    public static String getBASE64(String s) {
        if (s == null) {
            return null;
        }

        return Base64.encode(s, "UTF-8");
    }

    // 将 BASE64 编码的字符串 s 进行解码
    public static String getFromBASE64(String s) {
        String decode = null;
        try {
            if (s == null) {
                return null;
            }
            decode = Base64.decode(s, "UTF-8");
        } catch (Exception e) {

        }
        return decode;
    }

    public static String nullToEmpty(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}

