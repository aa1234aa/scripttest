package com.bitnei.cloud.compent.kafka.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
 * ----------------------------------------------------------------------------- <br>
 * 修改历史 <br>
 * <table width="432" border="1">
 * <tr>
 * <td>版本</td>
 * <td>时间</td>
 * <td>作者</td>
 * <td>改变</td>
 * </tr>
 * <tr>
 * <td>1.0</td>
 * <td>2019-04-02</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
public class KafkaMessageUtil {


    public static Map<String,String> parserMsg(String msg){

        try {
            String temp = msg.replaceAll(" ", "");
            String[] params =  temp.split(",");
            Map<String,String> map = new HashMap<>();

            for (String string : params) {
                String[] kv = string.split(":", 2);
                if (kv.length == 2){
                    map.put(kv[0],kv[1]);
                }
            }
            return map;
        }
        catch (Exception e){
            log.error("error",e);
            return null;
        }
    }
}
