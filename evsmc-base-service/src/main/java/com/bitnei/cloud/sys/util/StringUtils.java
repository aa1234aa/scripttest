package com.bitnei.cloud.sys.util;

import java.text.DecimalFormat;

/**
 * @ClassName StringUtils
 * @Description
 * @Author zxz
 * @Date 2019/7/22 17:22
 **/
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String ts(Object d) {
        if(d == null) {
            return null;
        }
        if(d instanceof Double) {
            DecimalFormat df = new DecimalFormat("######.#######");
            return df.format(d);
        } else {
            return d.toString();
        }
    }
}
