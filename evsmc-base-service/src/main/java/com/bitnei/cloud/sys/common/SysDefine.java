package com.bitnei.cloud.sys.common;

/**
 * Created by chenp on 2015/6/11.
 * 全局定义，从sysDefine.properties读取加载
 */

public class SysDefine {



    //导出模板路径
    public static String impFileTemplatePath;

    public static final String kafkaDataCtrlTopic = "ds_ctrlreq";
    public static final String kafkaDataCtrlSPTopic = "us_ctrlrsp";


    public interface DIAGNBOSE_MODE {
        String NORMAL = "0";
        String EXCEPTION = "2";
        String FAULT = "1";
    }

    //服务器相对路径模板
    public static final String FTP_RELATIVE_PATH = "{userName}/{date}/{hour}/{minute}/{second}/";
    /********************************ftp服务器配置End***********************************/

    /**
     * 定时任务执行间隔吗，单位：分钟
     */
    public static final Integer INSTRUCT_EFFECTIVE_TIME_INTERVAL = 1;
    /**
     * 指令任务有效时长单位：1天数2小时数3分钟
     */
    public static final Integer INSTRUCT_EFFECTIVE_TIME_TYPE = 3;
    /**
     * 指令任务是否有效：1有效、其他无效
     */
    public static final Integer INSTRUCT_EFFECTIVE_STATUS = 1;
    /**
     * 指令类型：1远程控制
     */
    public static final int INSTRUCT_TYPE_CONTROL = 1;
    /**
     * 指令类型：2远程升级
     */
    public static final int INSTRUCT_TYPE_UPGRADE = 2;
}