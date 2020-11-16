package com.bitnei.cloud.sys.service;

import java.util.regex.Pattern;

public class RegexTest {

    public static void main(String [] args) throws Exception {
        regex4();

    }

    public static void regex4() {
        String pattern = "^[a-zA-Z0-9]{18}|$";
        System.out.println(Pattern.matches(pattern, ""));
        System.out.println(Pattern.matches(pattern, "123456789"));
        System.out.println(Pattern.matches(pattern, "ZXZQ78987545874859"));

    }

    public static void regex3() {
        String pattern = "(([1-9]\\d{0,6})|([0]))(\\.(\\d){0,2})?";
        System.out.println(Pattern.matches(pattern, ""));
        System.out.println(Pattern.matches(pattern, "34"));
        System.out.println(Pattern.matches(pattern, "50"));

    }

    public static void regex2() {
        String s = "124x.a^3";
        String pattern = ".{0,5}";
        boolean matches = Pattern.matches(pattern, s);
        System.out.println("s = " + matches);
    }

    public static void regex1() {
        String content1 = "";
        String content2 = "ss";
        String content3 = "sssss";
        String content4 = "ssssssss";
        String content5 = "\ns\ns\ns\ns\ns\n";
        String pattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{4,6}|$";
//        String pattern = "(([1-9]\\d{1,10})|([0]))(\\.(\\d){0,2})?|";
        boolean isMatch1 = Pattern.matches(pattern, content1);
        boolean isMatch2 = Pattern.matches(pattern, content2);
        boolean isMatch3 = Pattern.matches(pattern, content3);
        boolean isMatch4 = Pattern.matches(pattern, content4);
        boolean isMatch5 = Pattern.matches(pattern, content5);
        System.out.println(" == " + isMatch1);
        System.out.println("ss== " + isMatch2);
        System.out.println("sssss== " + isMatch3);
        System.out.println("ssssssss== " + isMatch4);
        System.out.println("sssss== " + isMatch5);
    }

    public static void regexPrice() {
        String content1 = "";
        String content2 = "11111111111111111";
        String content3 = "0.1";
        String content4 = "9";
        String content5 = "-1";
        String pattern = "(([1-9]\\d{0,9})|([0]))(\\.(\\d){0,2})?|";
        boolean isMatch1 = Pattern.matches(pattern, content1);
        boolean isMatch2 = Pattern.matches(pattern, content2);
        boolean isMatch3 = Pattern.matches(pattern, content3);
        boolean isMatch4 = Pattern.matches(pattern, content4);
        boolean isMatch5 = Pattern.matches(pattern, content5);
        System.out.println("content1 == " + isMatch1);
        System.out.println("content2 == " + isMatch2);
        System.out.println("content3 == " + isMatch3);
        System.out.println("content4 == " + isMatch4);
        System.out.println("content5 == " + isMatch5);
    }

}
