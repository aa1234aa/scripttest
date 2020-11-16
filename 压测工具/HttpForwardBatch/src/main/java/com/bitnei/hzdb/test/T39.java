package com.bitnei.hzdb.test;

import java.text.SimpleDateFormat;

public class T39 {


    public static void main(String [] a)throws  Exception{



        try {
            long l = System.currentTimeMillis();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 设置开始时间
             long m=sdf2.parse("2020-09-24 16:40:00").getTime();
            // 设置结束时间
            long time = sdf2.parse("2020-09-24 18:18:00").getTime();
            Thread.sleep(m-l);
            // 设置直播地址
            java.net.URI uri = java.net.URI.create("https://apppxggaqfw7749.h5.xiaoeknow.com/content_page/eyJ0eXBlIjoxMiwicmVzb3VyY2VfdHlwZSI6NCwicmVzb3VyY2VfaWQiOiJsXzVmNjA3YWI4ZTRiMGQ1OWM4N2I1ZmE1NCIsInByb2R1Y3RfaWQiOiIiLCJhcHBfaWQiOiJhcHBwWGdHYXFGVzc3NDkiLCJleHRyYV9kYXRhIjowfQ?app_id=apppXgGaqFW7749&entry=2&entry_type=2001");

            java.awt.Desktop dp = java.awt.Desktop.getDesktop();

            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {

                dp.browse(uri);
            }

            Thread.sleep(time-m);
            closeBrowse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeBrowse() throws  Exception{
        String command = "taskkill /f /im chrome.exe";
        Runtime.getRuntime().exec(command);

    }

}
