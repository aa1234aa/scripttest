package com.bitnei.cloud.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenp
 */
public class StringUtilExs {


    /**
     * 分割字符串，转为list
     * @param str
     * @param reg
     * @return
     */
    public static List<String> spiltString(final String str, final String reg){
        List<String> stringList = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(str)){
            String[] vals = str.trim().split(reg);

            for (String v: vals){
                stringList.add(v);
            }
        }
        return stringList;
    }
}
