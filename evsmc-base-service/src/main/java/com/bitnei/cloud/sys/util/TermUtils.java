package com.bitnei.cloud.sys.util;

import com.bitnei.cloud.common.Exception.BusinessException;
import com.bitnei.cloud.sys.common.Constant;
import com.bitnei.cloud.sys.model.InstructManagementModel;
import com.bitnei.cloud.sys.model.InstructSendRuleModel;
import com.bitnei.cloud.sys.model.VehicleModel;
import com.bitnei.cloud.sys.model.VehicleWithOnlineStatus;
import com.bitnei.cloud.sys.service.impl.InstructTaskService;
import it.sauronsoftware.base64.Base64;
import sun.misc.BASE64Encoder;

/**
 * Created by chenp on 2015-09-12.
 */
public class TermUtils extends org.apache.commons.lang.StringUtils {

    //    //SUBMIT  1  LVBV4J0B2AJ063987  CONTROL  {1001:20150623120000,1002:京A12345}
//    public static String getCtrlString(SysVehPojo vehicle, String ctrlCode, String argcs) {
//        String result = "";
//        if (isEmpty(argcs)) {
//            result = String.format("SUBMIT 1 %s CONTROL {VID:%s,VTYPE:%s,5401:%s,5402:%s}", vehicle.getVin(), vehicle.getUuid(), vehicle.getVehModelId(), DateUtil.getKafkaDataSyncTime(), ctrlCode);
//        } else {
//            String val = Base64.encode(argcs, "UTF-8");
//            result = String.format("SUBMIT 1 %s CONTROL {VID:%s,VTYPE:%s,5401:%s,5402:%s,5403:%s}", vehicle.getVin(),
//                    vehicle.getUuid(), vehicle.getVehModelId(), DateUtil.getKafkaDataSyncTime(), ctrlCode, val);
//        }
//        return result;
//    }
//

    public static final String KAI_MA_LOCK = "99";
    public static final String KAI_MA_UNLOCK = "100";

    public static long conversion(String input) {
        return Long.parseLong(input, 16);
    }

    public static String getLockCarString(VehicleModel vehicle, InstructSendRuleModel instructSendRuleModel) {
        try {
            return String.format("SUBMIT 0 %s TERMLOCK {VID:%s,VTYPE:%s,4710040:%s,4710041:%s}", vehicle.getVin(),
                    vehicle.getUuid(), vehicle.getVehModelId(), instructSendRuleModel.getSessionId(),
                    conversion(instructSendRuleModel.getStandardValue()));
        } catch (Exception e) {
            throw new BusinessException("命令数据有误，请检查命令数据是否合法！");
        }
    }

    //SUBMIT  1  LVBV4J0B2AJ063987  CONTROL  {1001:20150623120000,1002:京A12345}
    public static String getCtrlString(VehicleWithOnlineStatus vehicle, String ctrlCode, String argcs, byte[] alarmLevel) {
        String result = "";
        if (isEmpty(argcs)) {
            result = String.format("SUBMIT 1 %s CONTROL {VID:%s,VTYPE:%s,5401:%s,5402:%s}", vehicle.getVin(),
                    vehicle.getUuid(), vehicle.getVehModelId(), DateUtil.getKafkaDataSyncTime(), ctrlCode);
        } else {
            String val = "";
            // 判断若报警等级不为空则转为byte下发，若为空则为其他控制指令
            if (null != alarmLevel && alarmLevel.length > 0) {
                val = com.bitnei.cloud.sys.util.Base64.encode(alarmLevel);
            } else {
                val = Base64.encode(argcs, "UTF-8");
            }
            result = String.format("SUBMIT 1 %s CONTROL {VID:%s,VTYPE:%s,5401:%s,5402:%s,5403:%s}", vehicle.getVin(),
                    vehicle.getUuid(), vehicle.getVehModelId(), DateUtil.getKafkaDataSyncTime(), ctrlCode, val);
        }
        return result;
    }

    /**
     * @param sysVehicleEntity
     * @param instructSendRuleModel
     * @return
     */
//    public static String getArgcRead(SysVehPojo vehicle, String argcList) {
//        return String.format("SUBMIT 1 %s GETARG {VID:%s,VTYPE:%s,5001:%s,5002:%s}", vehicle.getVin(), vehicle.getUuid(), vehicle.getVehModelId(),
//                DateUtil.getKafkaDataSyncTime(), argcList);
//    }
//
//    public static String[] getArgcWrite(SysVehPojo vehicle, String argcList) {
//        SysDictService sysDictService = SpringContextUtil.getBean("sysDictService");
//        String[] pairs = argcList.split(",");
//        StringBuffer ids = new StringBuffer();
//        StringBuffer disply = new StringBuffer();
//        for (int i = 0; i < pairs.length; i++) {
//            String pair = pairs[i];
//            if (!isEmpty(pair)) {
//                String[] kv = pair.split(":");
//                String key = kv[0];
//                String val = kv[1];
//                if (!key.equals("17")) {
//                    SysDictEntity dict = sysDictService.getByHQL(String.format("from SysDictEntity where val='%s' and type=%d", key, SysDefine.dictTerminalArgc));
//                    if (vehicle.getRuleNo().equals("1")) {//国标车辆终端参数
//                        switch (key) {
//                            case "12":
//                                key = "14";
//                                break;
//                            case "14":
//                                key = "15";
//                                break;
//                            case "15":
//                                key = "16";
//                                break;
//                        }
//                        dict = sysDictService.getByHQL(String.format("from SysDictEntity where val='%s' and type=%d", key, SysDefine.dictTerminalArgcG));
//                    }
//                    disply.append(String.format("%s:%s,", dict.getName(), val));
//                    int intkey = Integer.valueOf(key).intValue() + 5201;
//                    if (key.equals("14") || key.equals("15") || key.equals("16")) {
//                        intkey = Integer.valueOf(key).intValue() + 5200;
//                    }
//                    if (intkey == 5207 || intkey == 5208) {
//                        val = Base64.encode(val, "UTF-8");
//                    }
//                    if (vehicle.getRuleNo().equals("1")) {//国标车辆终端参数
//                        if (intkey == 5215 || intkey == 5205) {
//                            val = Base64.encode(val, "UTF-8");
//                        }
//                    }
//                    ids.append(String.format("%d:%s,", intkey, val));
//                }
//            }
//        }
//        disply.setLength(disply.length() - 1);
//        ids.setLength(ids.length() - 1);
//        String[] ret = new String[2];
//        ret[0] = String.format("SUBMIT 1 %s SETARG {VID:%s,VTYPE:%s,5201:%s,%s}", vehicle.getVin(), vehicle.getUuid(), vehicle.getVehModelId(),
//                DateUtil.getKafkaDataSyncTime(), ids.toString());
//        ret[1] = disply.toString();
//        return ret;
//    }
//

    /**
     * 写死凯马的锁车和解锁
     *
     * @return
     */
    public static String getCanControlArgs(VehicleWithOnlineStatus sysVehicleEntity,
                                           InstructSendRuleModel instructSendRuleModel,
                                           String instructType) {
        String roadFlag = "00";
        String sendFrameType = "1";
        Long high = Long.parseLong(roadFlag + sendFrameType + "00000000000000000000000000000", 2);

        String cmd = "SUBMIT 0 %s SETCAN {VID:%s,VTYPE:%s,4410021:%s,4410022:%s,4410023:%s}";
        BASE64Encoder base64Encoder = new BASE64Encoder();
        StringBuilder sb = new StringBuilder();

        String canId;
        String canData;

        switch (instructType) {
            case KAI_MA_LOCK: {
                canId = "1801F4F9";
                canData = "0102000000000000";
                break;
            }
            case KAI_MA_UNLOCK: {
                canId = "1801F4F9";
                canData = "0101000000000000";
                break;
            }
            default: throw new BusinessException("指令类型不支持");
        }

        String canIdInfoOrg = String.format("%8s", canId)
                .replaceAll(" ", "0");
        Integer canIdIntOrg = Integer.parseInt(canIdInfoOrg, 16);
        Long newCanId = high | canIdIntOrg;
        String canInfo = Long.toHexString(newCanId) + canData;
        byte[] newCanInfo = hexToBinary(canInfo);
        sb.append(base64Encoder.encode(newCanInfo));

        return String.format(cmd, sysVehicleEntity.getVin(), sysVehicleEntity.getUuid(),
                sysVehicleEntity.getVehModelId(),
                instructSendRuleModel.getSessionId().length(),
                instructSendRuleModel.getSessionId(), sb.toString());
    }

    /**
     * 吉利 远程升级内部通讯
     *
     * @param vehicle
     * @param ctrlCode
     * @param argcs
     * @param protocolType 协议类型 ;
     * @return
     */
    //SUBMIT  1  LVBV4J0B2AJ063987  CONTROL  {1001:20150623120000,1002:京A12345}
    public static String getCtrlStringKuozhan(VehicleModel vehicle, String ctrlCode, String argcs, String protocolType) {
        String result = "";
        if (Constant.PROTOCOL_TYPE_CUSTOM.equals(protocolType) || Constant.PROTOCOL_TYPE_CUSTOM.equals(ctrlCode)) {
            ctrlCode = "1";
            protocolType = "1";
        }
        if (isEmpty(argcs)) {
            result = String.format("SUBMIT 1 %s CONTROL {VID:%s,VTYPE:%s,5401:%s,5402:%s}", vehicle.getVin(),
                    vehicle.getUuid(), vehicle.getVehModelId(), DateUtil.getKafkaDataSyncTime(), ctrlCode);
        } else {
            String val = Base64.encode(argcs, "UTF-8");
            result = String.format("SUBMIT 1 %s CONTROL {VID:%s,VTYPE:%s,5401:%s,5402:%s,5403:%s,5404:%s}"
                    , vehicle.getVin(), vehicle.getUuid(), vehicle.getVehModelId(),
                    DateUtil.getKafkaDataSyncTime(), ctrlCode,
                    val, protocolType);
        }
        return result;
    }

    private static byte[] hexToBinary(String hex) {
        hex = hex.replaceAll(" ", "").toUpperCase();
        hex = String.format("%24s", hex).replaceAll(" ", "0");
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
}
