package com.bitnei.util;

/*
@author 黄永雄
@create 2019/9/29 13:29
*/


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class InitPropertiesUtil {

    public static Properties init(String propertiesAbsPath) {

    Properties prop = new Properties();
    try {
        InputStream in = new BufferedInputStream(new FileInputStream(propertiesAbsPath));
        prop.load(in);
    }catch (Exception e) {
        e.printStackTrace();
    }
    return prop;
}

}
