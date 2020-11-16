package com.bitnei.cloud.sys.util;

import com.bitnei.cloud.sys.util.translate.ITranslate;
import com.bitnei.cloud.sys.util.translate.RuleTypeEnum;
import com.bitnei.cloud.sys.util.translate.TranslateFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import static com.bitnei.cloud.sys.util.translate.DataFormatter.TRANSLATE_POSTFIX;

/**
 * Created by 鹏 on 2015/10/19.
 */
@Slf4j
public class MessageUtils extends org.apache.commons.lang.StringUtils {

    public static List uploadPacketDataG6(byte[] packetData, int size, boolean onlyData) {
        List<MessageBean> msgs = new ArrayList<MessageBean>();

        try {
            int nSize = size;

            byte[] resvData = packetData;

            int dataIndex = 0;

            if ((resvData[dataIndex] == 0x23) && (resvData[(dataIndex + 1)] == 0x23)) {

                msgs.add(getDescMM("头部", "起始符", resvData, dataIndex, 2, onlyData));

                dataIndex += 2;

                int cmd = byteToInt(resvData[(dataIndex)]);

                msgs.add(getSrcMM("头部", "命令单元", resvData, dataIndex, 1, onlyData));
                dataIndex += 1;

                Boolean isCar = true;
                if (resvData[dataIndex] == -2) {
                    dataIndex += 1;
                    isCar = false;
                }

                msgs.add(getDescMM("头部", "VIN", resvData, dataIndex, 17, onlyData));

                dataIndex += 17;

                if (isCar) {
                    msgs.add(getSrcMM("头部", "终端软件版本号", resvData, dataIndex, 1, onlyData));
                    dataIndex += 1;
                }

                msgs.add(getSrcMM("头部", "数据加密方式", resvData, dataIndex, 1, onlyData));
                dataIndex += 1;

                MessageBean lenMM = getDataMM("头部", "数据单元长度", resvData, dataIndex,
                        2, 0, 1, 0, 65534, onlyData, null);

                long packLen = dataIndex + lenMM.getLongValue();
                msgs.add(lenMM);
                dataIndex += 2;

                byte bcc = getBCC(resvData, 0, 24 + (int) lenMM.getLongValue());

                if (bcc != resvData[nSize - 1]) {
                    MessageBean bccBean = new MessageBean();
                    bccBean.setName("校验码");
                    bccBean.setState(2);
                    bccBean.setGroup("尾部");
                    bccBean.setValue(String.format("报文校验码:%02X,正确校验码:%02X", resvData[nSize - 1], bcc));
                    msgs.add(bccBean);

                }
                //解析命令单元
                switch (cmd) {
                    case 1: {//车辆登入
                        msgs.add(getTime("主部", "数据采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主部", "登入流水号", resvData, dataIndex, 2,
                                0, 1, 1, 65531, onlyData, "1020"));
                        dataIndex += 2;
                        msgs.add(getDescMM("主部", "SIM卡号", resvData, dataIndex, 20, onlyData));
                        dataIndex += 20;
                        break;
                    }
                    case 2:
                    case 3: {//实时信息上报,补发信息上报
                        msgs.add(getTime("主部", "数据采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主部", "信息流水号", resvData, dataIndex, 2, 0, 1, 1, 65531, onlyData, "60010"));
                        dataIndex += 2;
                        packLen -= 66;

                        //类型set，去重用
                        Set<Integer> typeSet = new HashSet<>();
                        Map<String, Integer> timeAndPackIdx = new HashMap<>();

                        while (dataIndex < packLen) {
                            //消息体分组

                            MessageBean typeBean = getDataMM("主部", "信息类型", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null);
                            byte dataType = (byte) typeBean.getLongValue();
                            typeBean.setValue(getG6FlagDesStation((byte) typeBean.getLongValue(), null));
                            if (typeSet.add((int) dataType)) {
                                msgs.add(typeBean);
                            }
                            dataIndex += 1;

                            //获取信息体时间，认为同一个时间的信息体是同一个包
                            MessageBean timeBean = getTime(getG6FlagDesStation(dataType, null), "信息体时间", resvData, dataIndex, 6, onlyData);
                            String time = timeBean.getValue();
                            if (!timeAndPackIdx.containsKey(time)) {
                                timeAndPackIdx.put(time, timeAndPackIdx.size() + 1);
                            }


                            //OBD信息
                            if (dataType == 0x01) {

                                msgs.add(getTime(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "信息体时间", resvData, dataIndex, 6, onlyData));
                                dataIndex += 6;
                                msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "OBD诊断协议", resvData, dataIndex, 1, onlyData));
                                dataIndex += 1;
                                msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "MIL状态", resvData, dataIndex, 1, onlyData));
                                dataIndex += 1;
                                msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "诊断支持状态", resvData, dataIndex, 2, onlyData));
                                dataIndex += 2;
                                msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "诊断就绪状态", resvData, dataIndex, 2, onlyData));
                                dataIndex += 2;
                                msgs.add(getDescMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "VIN", resvData, dataIndex, 17, onlyData));
                                dataIndex += 17;
                                msgs.add(getDescMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "软件标定识别号", resvData, dataIndex, 18, onlyData));
                                dataIndex += 18;
                                msgs.add(getDescMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "标定验证码(CVN)", resvData, dataIndex, 18, onlyData));
                                dataIndex += 18;
                                msgs.add(getDescMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "IUPR值", resvData, dataIndex, 36, onlyData));
                                dataIndex += 36;
                                int faultCount = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "故障码总数", resvData, dataIndex,
                                        1, 0, 1, 0, 252, onlyData, "60020"));
                                dataIndex += 1;
                                //如果数据在正常范围
                                if (faultCount <= 252 && faultCount >= 0) {
                                    for (int n = 0; n < faultCount; n++) {
                                        msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "故障码信息列表" + (n + 1), resvData, dataIndex, 4, onlyData));
                                        dataIndex += 4;
                                    }
                                }
                            }
                            //数据流信息
                            else if (dataType == 0x02) {

                                msgs.add(getTime(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "信息体时间", resvData, dataIndex, 6, onlyData));
                                dataIndex += 6;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "车速", resvData, dataIndex,
                                        2, 0, 1/256d, 0, 250.996, onlyData, "2201"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "大气压力", resvData, dataIndex,
                                        1, 0, 0.5d, 0, 125, onlyData, "60026"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "发动机净输出扭矩", resvData, dataIndex,
                                        1, 125, 1d, -125, 125, onlyData, "60027"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "摩擦扭矩", resvData, dataIndex,
                                        1, 125, 1d, -125, 125, onlyData, "60028"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "发动机转速", resvData, dataIndex,
                                        2, 0, 0.125d, 0, 8031.875, onlyData, "60029"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "发动机燃料流量", resvData, dataIndex,
                                        2, 0, 0.05d, 0, 3212.75, onlyData, "60030"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "SCR上游NOx传感器输出值", resvData, dataIndex,
                                        2, 200, 0.05d, -200, 3012.75, onlyData, "60031"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "SCR下游NOx传感器输出值", resvData, dataIndex,
                                        2, 200, 0.05d, -200, 3012.75, onlyData, "60032"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "反应剂余量", resvData, dataIndex,
                                        1, 0, 0.4d, 0, 100, onlyData, "60033"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "进气量", resvData, dataIndex,
                                        2, 0, 0.05d, 0, 3212.75, onlyData, "60034"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "SCR入口温度", resvData, dataIndex,
                                        2, 273, 0.03125d, -273, 1734.96875, onlyData, "60035"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "SCR出口温度", resvData, dataIndex,
                                        2, 273, 0.03125d, -273, 1734.96875, onlyData, "60036"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "DPF压差", resvData, dataIndex,
                                        2, 0, 0.1d, 0, 6425.5, onlyData, "60037"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "发动机冷却液温度", resvData, dataIndex,
                                        1, 40, 1, -40, 210, onlyData, "60038"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "油箱液位", resvData, dataIndex,
                                        1, 0, 0.4d, 0, 100, onlyData, "60039"));
                                dataIndex += 1;
                                msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "定位状态", resvData, dataIndex, 1, onlyData));
                                dataIndex += 1;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "经度", resvData, dataIndex,
                                        4, 0, 0.000001d, 0, 180, onlyData, "2502"));
                                dataIndex += 4;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "纬度", resvData, dataIndex,
                                        4, 0, 0.000001d, 0, 90, onlyData, "2503"));
                                dataIndex += 4;
                                msgs.add(getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "累计里程", resvData, dataIndex,
                                        4, 0, 0.1d, 0, 999999.9, onlyData, "2202"));
                                dataIndex += 4;
                            }
                            //预留
                            else if (dataType >= 0x03 && dataType <= 0x7F) {

                                msgs.add(getTime(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "信息体时间", resvData, dataIndex, 6, onlyData));
                                dataIndex += 6;
                                MessageBean userdefineLenMM = getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "预留数据长度",
                                        resvData, dataIndex, 2, 0, 1, 0, 65534, onlyData, null);
                                msgs.add(userdefineLenMM);
                                dataIndex += 2;

                                long userdefineLen = userdefineLenMM.getLongValue();
                                msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "预留数据", resvData, dataIndex, (int) userdefineLen, onlyData));
                                dataIndex += userdefineLen;
                            }
                            //用户自定义
                            else if (dataType >= 0x80 && dataType <= -2) {

                                msgs.add(getTime(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "信息体时间", resvData, dataIndex, 6, onlyData));
                                dataIndex += 6;
                                MessageBean userdefineLenMM = getDataMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "自定义数据长度",
                                        resvData, dataIndex, 2, 0, 1, 0, 65534, onlyData, null);
                                msgs.add(userdefineLenMM);
                                dataIndex += 2;

                                long userdefineLen = userdefineLenMM.getLongValue();
                                msgs.add(getSrcMM(getG6FlagDesStation(dataType, timeAndPackIdx.get(time)), "自定义数据", resvData, dataIndex, (int) userdefineLen, onlyData));
                                dataIndex += userdefineLen;
                            }
                        }
                        msgs.add(getSrcMM("主部", "签名信息", resvData, dataIndex, 0, onlyData));

                        int signRLen = byteToInt(resvData[(dataIndex)]);
                        msgs.add(getDataMM("签名信息", "签名R值长度", resvData, dataIndex, 1, 0, 1, 1, 253, onlyData, null));
                        dataIndex += 1;

                        msgs.add(getDescMM("签名信息", "签名R值", resvData, dataIndex, signRLen, onlyData));
                        dataIndex += signRLen;

                        int signSLen = byteToInt(resvData[(dataIndex)]);
                        msgs.add(getDataMM("签名信息", "签名S值长度", resvData, dataIndex, 1, 0, 1, 1, 253, onlyData, null));
                        dataIndex += 1;

                        msgs.add(getDescMM("签名信息", "签名S值", resvData, dataIndex, signSLen, onlyData));
                        dataIndex += signSLen;
                        break;
                    }
                    //车辆登出
                    case 4: {
                        msgs.add(getTime("主部", "登出时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主部", "登出流水号", resvData, dataIndex, 2, 0, 1, 1, 65531, onlyData, "1033"));
                        dataIndex += 2;
                        break;
                    }
                    //终端校时
                    case 5: {
                        break;
                    }
                    //车辆拆除报警信息
                    case 6: {
                        msgs.add(getTime("主部", "数据采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主部", "登入流水号", resvData, dataIndex, 2, 0, 1, 1, 65531, onlyData, "60051"));
                        dataIndex += 2;
                        msgs.add(getSrcMM("主部", "车辆状态", resvData, dataIndex, 1, onlyData));
                        dataIndex += 1;
                        msgs.add(getDataMM("主部", "经度", resvData, dataIndex, 4, 0, 0.000001d, 0, 180, onlyData, "2502"));
                        dataIndex += 4;
                        msgs.add(getDataMM("主部", "纬度", resvData, dataIndex, 4, 0, 0.000001d, 0, 90, onlyData, "2503"));
                        dataIndex += 4;
                        break;
                    }
                    //数据防篡改备案信息
                    case 7: {
                        msgs.add(getTime("主部", "数据采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDescMM("头部", "芯片ID", resvData, dataIndex, 16, onlyData));
                        dataIndex += 16;
                        msgs.add(getDescMM("头部", "公钥", resvData, dataIndex, 64, onlyData));
                        dataIndex += 64;
                        msgs.add(getDescMM("头部", "VIN", resvData, dataIndex, 17, onlyData));
                        dataIndex += 17;

                        msgs.add(getSrcMM("头部", "签名信息", resvData, dataIndex, 0, onlyData));

                        int signRLen = byteToInt(resvData[(dataIndex)]);
                        msgs.add(getDataMM("签名信息", "签名R值长度", resvData, dataIndex, 1, 0, 1, 1, 253, onlyData, null));
                        dataIndex += 1;

                        msgs.add(getDescMM("签名信息", "签名R值", resvData, dataIndex, signRLen, onlyData));
                        dataIndex += signRLen;

                        int signSLen = byteToInt(resvData[(dataIndex)]);
                        msgs.add(getDataMM("签名信息", "签名S值长度", resvData, dataIndex, 1, 0, 1, 1, 253, onlyData, null));
                        dataIndex += 1;

                        msgs.add(getDescMM("签名信息", "签名S值", resvData, dataIndex, signSLen, onlyData));
                        dataIndex += signSLen;
                        break;
                    }
                    //备案结果应答
                    case 8: {
                        msgs.add(getSrcMM("头部", "车辆状态", resvData, dataIndex, 1, onlyData));
                        dataIndex += 1;
                        msgs.add(getSrcMM("头部", "充电状态", resvData, dataIndex, 1, onlyData));
                        dataIndex += 1;
                        break;
                    }
//                    case 8: {//平台登入
//                        msgs.add(getTime("主体", "平台登入时间", resvData, dataIndex, 6));
//                        dataIndex += 6;
//                        msgs.add(getSrcMM("主体", "登入流水号", resvData, dataIndex, 2));
//                        dataIndex += 2;
//                        msgs.add(getSrcMM("主体", "平台用户名", resvData, dataIndex, 12));
//                        dataIndex += 12;
//                        msgs.add(getSrcMM("主体", "平台密码", resvData, dataIndex, 20));
//                        dataIndex += 20;
//                        msgs.add(getSrcMM("主体", "加密规则", resvData, dataIndex, 1));
//                        dataIndex += 1;
//                        break;
//                    }
//                    case 8: {//平台登出
//                        msgs.add(getTime("主体", "登出时间", resvData, dataIndex, 6));
//                        dataIndex += 6;
//                        msgs.add(getSrcMM("主体", "登出流水号", resvData, dataIndex, 2));
//                        dataIndex += 2;
//                        break;
//                    }
                    default:
                        break;


                }
            } else {
                MessageBean bccBean = new MessageBean();
                bccBean.setName("报文错误");
                bccBean.setValue("未知错误");
                msgs.add(bccBean);
            }

        } catch (Exception e) {

            MessageBean bccBean = new MessageBean();
            bccBean.setName("报文错误");
            bccBean.setValue("未知错误");
            bccBean.setSrc(e.getMessage());
            msgs.add(bccBean);
        }

        return msgs;
    }

    /////解析国标数据包
    public static List uploadPacketDataStation(byte[] packetData, int size, boolean onlyData) {
        List<MessageBean> msgs = new ArrayList<MessageBean>();
        try {
            int nSize = size;

            byte[] resvData = packetData;
            byte[] rtuID = new byte[17];

            int dataIndex = 0;
            int findIndex = 0;
            if ((resvData[dataIndex] == 0x23) && (resvData[(dataIndex + 1)] == 0x23)) {
                msgs.add(getDescMM("头部", "起始符", resvData, dataIndex, 2, onlyData));
                dataIndex += 2;
                int cmd = byteToInt(resvData[(dataIndex)]);
                msgs.add(getSrcMM("头部", "命令单元", resvData, dataIndex, 2, onlyData));
                dataIndex += 2;
                msgs.add(getDescMM("头部", "VIN", resvData, dataIndex, 17, onlyData));
                dataIndex += 17;
                msgs.add(getSrcMM("头部", "数据加密方式", resvData, dataIndex, 1, onlyData));
                dataIndex += 1;

                MessageBean lenMM = getDataMM("头部", "长度", resvData, dataIndex, 2, 0, 1, 0, 65534, onlyData, null);
                long packLen = dataIndex + lenMM.getLongValue();
                msgs.add(lenMM);
                dataIndex += 2;

                byte bcc = getBCC(resvData, 0, 24 + (int) lenMM.getLongValue());

                if (bcc != resvData[nSize - 1]) {
                    MessageBean bccBean = new MessageBean();
                    bccBean.setName("校验码");
                    bccBean.setState(2);
                    bccBean.setGroup("尾部");
                    bccBean.setValue(String.format("报文校验码:%02X,正确校验码:%02X", resvData[nSize - 1], bcc));
                    msgs.add(bccBean);

                }
                //解析命令单元
                switch (cmd) {
                    case 197:{ // 密钥上报
                        StringBuffer srcString = new StringBuffer();
                        MessageBean messageBean = new MessageBean();
                        for(int j=0;j<16;j++){
                            messageBean = getDataMM("主部","密钥", resvData, dataIndex, 1, 0, 1,0,65534, onlyData, null);
                            srcString.append(messageBean.getSrc());
                            dataIndex += 1;
                        }
                        messageBean.setValue(srcString.toString());
                        messageBean.setSrc(srcString.toString());
                        msgs.add(messageBean);
                        break;
                    }
                    case 196:{//升级结果应答
                        msgs.add(getTime("主部", "时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主部","流水号", resvData, dataIndex, 2, 0, 1,0,65534, onlyData, null));
                        dataIndex += 2;
                        msgs.add(getDataMM("主部","升级结果", resvData, dataIndex, 1, 0, 1,0,65534, onlyData, null));
                        break;
                    }
                    case 195:{//日志上传结果应答
                        msgs.add(getSrcMM("主部", "日志id长度", resvData, dataIndex, 1, onlyData));
                        dataIndex += 1;
                        msgs.add(getDescMM("主部", "日志id", resvData, dataIndex, 32, onlyData));
                        dataIndex += 32;
                        msgs.add(getTime("主部", "日志请求时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        MessageBean fileNameBean = getDataMM("头部","文件名长度", resvData, dataIndex, 1, 0, 1,0,65534, onlyData, null);
                        msgs.add(fileNameBean);
                        dataIndex += 1;
                        msgs.add(getDescMM("主部", "文件名", resvData, dataIndex, (int) fileNameBean.getLongValue(), onlyData));
                        break;
                    }
                    case 193:{//车辆邀请
                        msgs.add(getDescMM("主部","ICCID", resvData, dataIndex, 20, onlyData));
                        break;
                    }
                    case 1: {//车辆登入
                        msgs.add(getTime("主部", "数据采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主部", "登入流水号", resvData, dataIndex, 2, 0, 1, 1, 65531, onlyData, null));
                        dataIndex += 2;
                        msgs.add(getDescMM("主部", "ICCID", resvData, dataIndex, 20, onlyData));
                        dataIndex += 20;
                        int sysCount = byteToInt(resvData[(dataIndex)]);
                        msgs.add(getDataMM("主部", "可充电储能子系统数", resvData, dataIndex, 1, 0, 1, 0, 250, onlyData, null));
                        dataIndex += 1;
                        int longCount = byteToInt(resvData[(dataIndex)]);
                        msgs.add(getDataMM("主部", "可充电储能系统编码长度", resvData, dataIndex, 1, 0, 1, 0, 50, onlyData, null));
                        dataIndex += 1;
                        for (int n = 0; n < sysCount; n++) {
                            msgs.add(getDescMM("主部", "可充电储能系统编码", resvData, dataIndex, longCount, onlyData));
                            dataIndex += longCount;
                        }
                        break;
                    }
                    case 2:
                    case 3: {//实时信息上报,补发信息上报
                        msgs.add(getTime("主部", "数据采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        while (dataIndex < packLen) {
                            MessageBean typeBean = getDataMM("头部", "信息类型", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null);
                            typeBean.setValue(getFlagDesStation((byte) typeBean.getLongValue()));
                            msgs.add(typeBean);
                            dataIndex += 1;
                            long x;
                            int minvpackNO;
                            int minvNO;
                            byte dataType = (byte) typeBean.getLongValue();
                            //整车数据
                            if (dataType == 0x01) {
                                MessageBean vsStatusBean = getSrcMM(getFlagDesStation(dataType), "车辆状态", resvData, dataIndex, 1, "3201", RuleTypeEnum.GB, onlyData);

                                msgs.add(vsStatusBean);
                                dataIndex += 1;
                                MessageBean chgStatusBean = getSrcMM(getFlagDesStation(dataType), "充电状态", resvData, dataIndex, 1, "2301", RuleTypeEnum.GB, onlyData);
                                msgs.add(chgStatusBean);
                                dataIndex += 1;

                                MessageBean runMode = getSrcMM(getFlagDesStation(dataType), "运行模式", resvData, dataIndex, 1, "2213", RuleTypeEnum.GB, onlyData);
                                msgs.add(runMode);
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "车速", resvData, dataIndex, 2, 0, 0.1d, 0, 220, onlyData, "2201"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "累计里程", resvData, dataIndex, 4, 0, 0.1d, 0, 999999.9, onlyData, "2202"));
                                dataIndex += 4;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "总电压", resvData, dataIndex, 2, 0, 0.1d, 0, 1000, onlyData, "2613"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "总电流", resvData, dataIndex, 2, 1000, 0.1d, -1000, 1000, onlyData, "2614"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "SOC", resvData, dataIndex, 1, 0, 1, 0, 100, onlyData, "7615"));
                                dataIndex += 1;
                                MessageBean dcdc = getSrcMM(getFlagDesStation(dataType), "DC-DC状态", resvData, dataIndex, 1, "2214", RuleTypeEnum.GB, onlyData);
                                msgs.add(dcdc);
                                dataIndex += 1;
                                msgs.add(getSrcMM(getFlagDesStation(dataType), "档位", resvData, dataIndex, 1, "2203", RuleTypeEnum.GB, onlyData));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "绝缘电阻", resvData, dataIndex, 2, 0, 1, 0, 60000, onlyData, "2617"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "加速踏板行程值", resvData, dataIndex, 1, 0, 1, 0, 100, onlyData, "2208"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "制动踏板状态", resvData, dataIndex, 1, 0, 1, 0, 100, onlyData, "2209"));
                                dataIndex += 1;


//                                msgs.add(getSrcMM(getFlagDesStation(dataType), "预留", resvData, dataIndex, 2));
//                                dataIndex += 2;

                            } else if (dataType == 0x02) {//驱动电机数据
                                int driveCount = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getFlagDesStation(dataType), "驱动电机个数", resvData, dataIndex, 1, 0, 1, 1, 253, onlyData, "2307"));
                                dataIndex += 1;
                                for (int n = 0; n < driveCount; n++) {
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "驱动电机序号", resvData, dataIndex, 1, 0, 1, 1, 253, onlyData, "2309"));
                                    dataIndex += 1;

                                    MessageBean driveStatus = getSrcMM(getFlagDesStation(dataType), "驱动电机状态", resvData, dataIndex, 1, "2310", RuleTypeEnum.GB, onlyData);
                                    msgs.add(driveStatus);
                                    dataIndex += 1;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "驱动电机控制器温度", resvData, dataIndex, 1, 40, 1, -40, 210, onlyData, "2302"));
                                    dataIndex += 1;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "驱动电机转速", resvData, dataIndex, 2, 20000, 1, -20000, 45531, onlyData, "2303"));
                                    dataIndex += 2;
                                    msgs.add(getDataMM2(getFlagDesStation(dataType), "驱动电机转矩", resvData, dataIndex, 2, 20000, 0.1d, -2000, 4553.1));
                                    dataIndex += 2;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "驱动电机温度", resvData, dataIndex, 1, 40, 1, 0, 250, onlyData, "2304"));
                                    dataIndex += 1;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "电机控制器输入电压", resvData, dataIndex, 2, 0, 0.1d, 0, 60000, onlyData, "2305"));
                                    dataIndex += 2;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "电机控制器直流母线电流", resvData, dataIndex, 2, 1000, 0.1d, -1000, 1000, onlyData, "2306"));
                                    dataIndex += 2;
                                }


                            } else if (dataType == 0x03) {//燃料电池数据
                                msgs.add(getDataMM(getFlagDesStation(dataType), "燃料电池电压", resvData, dataIndex, 2, 0, 0.1d, 0, 20000, onlyData, "2110"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "燃料电池电流", resvData, dataIndex, 2, 0, 0.1d, 0, 20000, onlyData, "2111"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "燃料消耗率", resvData, dataIndex, 2, 0, 0.01d, 0, 60000, onlyData, "2112"));//??
                                dataIndex += 2;
                                MessageBean temCountBB = getDataMM(getFlagDesStation(dataType), "燃料电池温度探针总数", resvData, dataIndex, 2, 0, 1, 0, 65531, onlyData, "2113");
                                //msgs.add(getDataMM(getFlagDesStation(dataType), "燃料电池温度探针总数", resvData, dataIndex, 2, 0, 1,0,65531));
                                msgs.add(temCountBB);
                                dataIndex += 2;
                                if (!temCountBB.getSrc().equals("FF FF")) {//燃料电池温度探针总数 非无效
                                    double tempCount = Double.parseDouble(temCountBB.getValue());
                                    //int  tempCount=Integer.parseInt(msgs.get(dataIndex).getValue());

                                    // int tempCount = byteToInt(resvData[(dataIndex,dataIndex+1)]);

                                    for (int n = 0; n < (int) tempCount; n++) {
                                        msgs.add(getDataMM(getFlagDesStation(dataType), "探针温度值", resvData, dataIndex, 1, 40, 1, -40, 200, onlyData, null));
//                                        msgs.add(getDataMM(getFlagDesStation(dataType), "探针温度值", resvData, dataIndex, 1, 40, 1, -40, 200, onlyData, "2114"));
                                        dataIndex += 1;
                                    }
                                }


                                msgs.add(getDataMM(getFlagDesStation(dataType), "氢系统中最高温度", resvData, dataIndex, 2, 40, 0.1d, -40, 200, onlyData, "2115"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "氢系统中最高温度探针代号", resvData, dataIndex, 1, 0, 1, 1, 252, onlyData, "2116"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "氢气最高浓度", resvData, dataIndex, 2, 0, 1, 0, 60000, onlyData, "2117"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "氢气最高浓度传感器代号", resvData, dataIndex, 1, 0, 1, 1, 252, onlyData, "2118"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "氢气最高压力", resvData, dataIndex, 2, 0, 0.1d, 0, 1000, onlyData, "2119"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "氢气最高压力传感器代号", resvData, dataIndex, 1, 0, 1, 1, 252, onlyData, "2120"));
                                dataIndex += 1;
                                msgs.add(getSrcMM(getFlagDesStation(dataType), "高压DC/DC状态", resvData, dataIndex, 1, "2121", RuleTypeEnum.GB,onlyData));
                                dataIndex += 1;
                            } else if (dataType == 0x04) {///7.2.3.4　发动机数据
                                msgs.add(getSrcMM(getFlagDesStation(dataType), "发动机状态", resvData, dataIndex, 1, "2401", RuleTypeEnum.GB, onlyData));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "曲轴转速", resvData, dataIndex, 2, 0, 1, 0, 60000, onlyData, "2411"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "燃料消耗率", resvData, dataIndex, 2, 0, 0.01d, 0, 60000, onlyData, "2413"));
                                dataIndex += 2;
                            } else if (dataType == 0x05) {//7.2.3.5　车辆位置数据
                                msgs.add(getDataMM(getFlagDesStation(dataType), "定位状态", resvData, dataIndex, 1, 0, 1, 0, 7, onlyData, "2501"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "经度", resvData, dataIndex, 4, 0, 0.000001d, 0, 180, onlyData, "2502"));
                                dataIndex += 4;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "纬度", resvData, dataIndex, 4, 0, 0.000001d, 0, 90, onlyData, "2503"));
                                dataIndex += 4;
                            } else if (dataType == 0x06) {//7.2.3.6　极值数据
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最高电压电池子系统号", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2601"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最高电压电池单体代号", resvData, dataIndex, 1, 0, 1, 0, 250, onlyData, "2602"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "电池单体电压最高值", resvData, dataIndex, 2, 0, 0.001d, 0, 15000, onlyData, "2603"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最低电压电池子系统号", resvData, dataIndex, 1, 0, 1, 0, 250, onlyData, "2604"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最低电压电池单体代号", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2605"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "电池单体电压最低值", resvData, dataIndex, 2, 0, 0.001d, 0, 15000, onlyData, "2606"));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最高温度子系统号", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2607"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最高温度探针单体代号", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2608"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最高温度值", resvData, dataIndex, 1, 40, 1, -40, 210, onlyData, "2609"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最低温度子系统号", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2610"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最低温度探针", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2611"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "最低温度值", resvData, dataIndex, 1, 40, 1, -40, 210, onlyData, "2612"));
                                dataIndex += 1;

                            } else if (dataType == 0x07) {//7.2.3.7　报警数据

                                msgs.add(getDataMM(getFlagDesStation(dataType), "最高报警等级", resvData, dataIndex, 1, 0, 1, 0, 255, onlyData, "2920"));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDesStation(dataType), "通用报警标志", resvData, dataIndex, 4, 0, 1, 0, 65535, onlyData, "3801"));
                                dataIndex += 4;
                                int faultCount = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能装置故障总数N1", resvData, dataIndex, 1, 0, 1, 0, 252, onlyData, "2921"));
                                dataIndex += 1;
                                if (faultCount <= 252 && faultCount >= 0) {//如果数据在正常范围
                                    for (int n = 0; n < faultCount; n++) {
                                        msgs.add(getSrcMM(getFlagDesStation(dataType), "可充电储能装置故障代码列表" + (n + 1), resvData, dataIndex, 4, onlyData));
                                        dataIndex += 4;
                                    }
                                }
                                int faultCount2 = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getFlagDesStation(dataType), "驱动电机故障总数N2", resvData, dataIndex, 1, 0, 1, 0, 252, onlyData, "2804"));
                                dataIndex += 1;
                                if (faultCount2 <= 252 && faultCount2 >= 0) {//如果数据在正常范围
                                    for (int n = 0; n < faultCount2; n++) {
                                        msgs.add(getSrcMM(getFlagDesStation(dataType), "驱动电机故障代码列表" + (n + 1), resvData, dataIndex, 4, onlyData));
                                        dataIndex += 4;
                                    }
                                }
                                int faultCount3 = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getFlagDesStation(dataType), "发动机故障总数N3", resvData, dataIndex, 1, 0, 1, 0, 252, onlyData, "2923"));
                                dataIndex += 1;
                                if (faultCount3 <= 252 && faultCount3 >= 0) {
                                    for (int n = 0; n < faultCount3; n++) {
                                        msgs.add(getSrcMM(getFlagDesStation(dataType), "发动机故障列表" + (n + 1), resvData, dataIndex, 4, onlyData));
                                        dataIndex += 4;
                                    }
                                }

                                int faultCount4 = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getFlagDesStation(dataType), "其他故障总数N4", resvData, dataIndex, 1, 0, 1, 0, 252, onlyData, "2808"));
                                dataIndex += 1;
                                if (faultCount4 <= 252 && faultCount4 >= 0) {
                                    for (int n = 0; n < faultCount4; n++) {
                                        msgs.add(getSrcMM(getFlagDesStation(dataType), "其他故障代码列表" + (n + 1), resvData, dataIndex, 4, onlyData));
                                        dataIndex += 4;
                                    }
                                }

                            } else if (dataType == 0x08) {//可充电储能装置电压数据
                                int VoltageCount = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能子系统个数", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2002"));
                                dataIndex += 1;
                                for (int n = 0; n < VoltageCount; n++) {
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能子系统号", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, null));
                                    dataIndex += 1;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能装置电压", resvData, dataIndex, 2, 0, 0.1d, 0, 10000, onlyData, null));
                                    dataIndex += 2;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能装置电流", resvData, dataIndex, 2, 1000, 0.1D, -1000, 1000, onlyData, null));
                                    dataIndex += 2;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "单体电池总数", resvData, dataIndex, 2, 0, 1, 1, 65531, onlyData, null));
                                    dataIndex += 2;
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "本帧起始电池序号", resvData, dataIndex, 2, 0, 1, 1, 65531, onlyData, null));
                                    dataIndex += 2;
                                    int battCount = byteToInt(resvData[(dataIndex)]);
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "本帧单体电池总数", resvData, dataIndex, 1, 0, 1, 1, 200, onlyData, null));
                                    dataIndex += 1;
                                    for (int j = 0; j < battCount; j++) {
                                        msgs.add(getDataMM(getFlagDesStation(dataType), "单体电池电压" + "-" + (n + 1) + "-" + (j + 1), resvData, dataIndex, 2, 0, 0.001, 0, 60.000, onlyData, null));
                                        dataIndex += 2;
                                    }
                                }


                            } else if (dataType == 0x09) {//可充电储能装置温度数据
                                int VoltageCount = byteToInt(resvData[(dataIndex)]);
                                msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能子系统个数", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, "2102"));
                                dataIndex += 1;
                                for (int n = 0; n < VoltageCount; n++) {
                                    msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能子系统号", resvData, dataIndex, 1, 0, 1, 1, 250, onlyData, null));
                                    dataIndex += 1;
                                    //int temCount = byteToInt(resvData[(dataIndex)]);
                                    MessageBean mm = getDataMM(getFlagDesStation(dataType), "可充电储能温度探针个数", resvData, dataIndex, 2, 0, 1, 1, 65531, onlyData, null);
                                    double temCount = Double.parseDouble(mm.getValue());
                                    //msgs.add(getDataMM(getFlagDesStation(dataType),"可充电储能温度探针个数", resvData, dataIndex, 2, 0, 1,1,65531));
                                    msgs.add(mm);
                                    dataIndex += 2;

                                    for (int j = 0; j < (int) temCount; j++) {
                                        msgs.add(getDataMM(getFlagDesStation(dataType), "可充电储能子系统各温度探针检测到的温度值" + "-" + (n + 1) + "-" + (j + 1), resvData, dataIndex, 1, 40, 1, -40, 210, onlyData, null));
                                        dataIndex += 1;
                                    }
                                }
                            } else if (dataType >= 0x90 || dataType <= -1) {//自定义数据？？
                                MessageBean userdefineLenMM = getDataMM(getFlagDesStation(dataType), "自定义数据长度", resvData, dataIndex, 2, 0, 1, 0, 65534, onlyData, null);
                                msgs.add(userdefineLenMM);
                                dataIndex += 2;

                                long userdefineLen = userdefineLenMM.getLongValue();
                                msgs.add(getSrcMM(getFlagDesStation(dataType), "自定义数据", resvData, dataIndex, (int) userdefineLen, onlyData));
                                dataIndex += userdefineLen;
                            }
                        }
                        break;
                    }
                    case 4: {//车辆登出
                        msgs.add(getTime("主体", "登出时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主体", "登出流水号", resvData, dataIndex, 2,0,1,1,65531, onlyData, null));
                        dataIndex += 2;
                        break;
                    }
                    case 5: {//平台登入
                        msgs.add(getTime("主体", "平台登入时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getSrcMM("主体", "登入流水号", resvData, dataIndex, 2, onlyData));
                        dataIndex += 2;
                        msgs.add(getSrcMM("主体", "平台用户名", resvData, dataIndex, 12, onlyData));
                        dataIndex += 12;
                        msgs.add(getSrcMM("主体", "平台密码", resvData, dataIndex, 20, onlyData));
                        dataIndex += 20;
                        msgs.add(getSrcMM("主体", "加密规则", resvData, dataIndex, 1, onlyData));
                        dataIndex += 1;
                        break;
                    }
                    case 6: {//平台登出
                        msgs.add(getTime("主体", "登出时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getSrcMM("主体", "登出流水号", resvData, dataIndex, 2, onlyData));
                        dataIndex += 2;
                        break;
                    }
                    case 7://心跳为空
                    case 8://终端校时为空
                    {
                        break;
                    }
                    case 128://查询命令
                        msgs.add(getTime("主体", "参数查询时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        MessageBean me = getDataMM("主体", "参数总数N", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null);
                        double can_count = Double.parseDouble(me.getValue());
                        msgs.add(getDataMM("主体", "参数总数N", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null));
                        dataIndex += 1;
                        for (int i = 0; i < can_count; i++) {
                            msgs.add(getTime("主体", "返回查询参数时间", resvData, dataIndex, 6, onlyData));
                            dataIndex += 6;
                            MessageBean mevalue = getDataMM("主体", "参数总数", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null);
                            msgs.add(getDataMM("主体", "参数总数", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null));
                            dataIndex += 1;
                            int lenId = Integer.parseInt(mevalue.getValue());
                            int yuming_count = 0;//远程服务与管理平台域名长度 M。
                            int yumingchangdu = 0;//公共平台域名长度N。
                            switch (lenId) {
                                case 1:
                                    msgs.add(getDataMM("参数值", "车载终端本地存储时间周期", resvData, dataIndex, 2, 0, 0, 0, 60000, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 2:
                                    msgs.add(getDataMM("参数值", "正常时，信息上报时间周期", resvData, dataIndex, 2, 0, 0, 1, 600, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 3:
                                    msgs.add(getDataMM("参数值", "出现报警时，信息上报时间周期", resvData, dataIndex, 2, 0, 0, 0, 60000, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 4:
                                    MessageBean pingtaiComLen = getSrcMM("参数值", "远程服务与管理平台域名长度 M", resvData, dataIndex, 1, onlyData);
                                    msgs.add(getSrcMM("参数值", "远程服务与管理平台域名长度 M", resvData, dataIndex, 1, onlyData));
                                    dataIndex += 1;
                                    yuming_count = (int) Double.parseDouble(pingtaiComLen.getValue());
                                    break;
                                case 5:
                                    for (int j = 0; j < yuming_count; j++) {
                                        msgs.add(getSrcMM("参数值", "远程服务与管理平台域名", resvData, dataIndex, 1, onlyData));
                                        dataIndex += 1;
                                    }
                                    break;

                                case 6:
                                    msgs.add(getDataMM("参数值", "正常时，信息上报时间周期", resvData, dataIndex, 2, 0, 0, 0, 65531, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 7:
                                    msgs.add(getSrcMM("参数值", "硬件版本", resvData, dataIndex, 5, onlyData));
                                    dataIndex += 5;
                                    break;
                                case 8:
                                    msgs.add(getSrcMM("参数值", "固件版本", resvData, dataIndex, 1, onlyData));
                                    dataIndex += 1;
                                    break;
                                case 9:
                                    msgs.add(getDataMM("参数值", "车载终端心跳发送周期", resvData, dataIndex, 1, 0, 0, 1, 240, onlyData, null));
                                    dataIndex += 1;
                                    break;
                                case 10:
                                    msgs.add(getDataMM("参数值", "终端应答超时时间", resvData, dataIndex, 2, 0, 0, 1, 600, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 11:
                                    msgs.add(getDataMM("参数值", "平台应答超时时间", resvData, dataIndex, 2, 0, 0, 1, 600, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 12:
                                    msgs.add(getDataMM("参数值", "连续三次登入失败后，到下一次登入的间隔时间", resvData, dataIndex, 1, 0, 0, 1, 240, onlyData, null));
                                    dataIndex += 1;
                                    break;
                                case 13:
                                    MessageBean temp = getSrcMM("参数值", "公共平台域名长度N", resvData, dataIndex, 1, onlyData);
                                    msgs.add(getSrcMM("参数值", "公共平台域名长度N", resvData, dataIndex, 1, onlyData));
                                    yumingchangdu = (int) Double.parseDouble(temp.getValue());
                                    break;
                                case 14:
                                    msgs.add(getSrcMM("参数值", "公共平台域名", resvData, dataIndex, yumingchangdu, onlyData));
                                    dataIndex += yumingchangdu;
                                    break;
                                case 15:
                                    msgs.add(getDataMM("参数值", "公共平台端口", resvData, dataIndex, 2, 0, 0, 0, 65531, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 16:
                                    msgs.add(getSrcMM("参数值", "是否处于抽样监测中", resvData, dataIndex, 1, onlyData));
                                    dataIndex += 1;
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case 129: {//设置命令
                        msgs.add(getTime("主体", "参数设置时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        MessageBean teSum = getDataMM("主体", "参数总数", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null);
                        double tempSum = Double.parseDouble(teSum.getValue());
                        msgs.add(getDataMM("主体", "参数总数", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null));
                        dataIndex += 1;
                        for (int i = 0; i < tempSum; i++) {
                            msgs.add(getTime("主体", "返回查询参数时间", resvData, dataIndex, 6, onlyData));
                            dataIndex += 6;
                            MessageBean mevalue = getDataMM("主体", "参数总数", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null);
                            msgs.add(getDataMM("主体", "参数总数", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null));
                            dataIndex += 1;
                            int lenId = Integer.parseInt(mevalue.getValue());
                            int yuming_count = 0;//远程服务与管理平台域名长度 M。
                            int yumingchangdu = 0;//公共平台域名长度N。
                            switch (lenId) {
                                case 1:
                                    msgs.add(getDataMM("参数值", "车载终端本地存储时间周期", resvData, dataIndex, 2, 0, 0, 0, 60000, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 2:
                                    msgs.add(getDataMM("参数值", "正常时，信息上报时间周期", resvData, dataIndex, 2, 0, 0, 1, 600, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 3:
                                    msgs.add(getDataMM("参数值", "出现报警时，信息上报时间周期", resvData, dataIndex, 2, 0, 0, 0, 60000, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 4:
                                    MessageBean pingtaiComLen = getSrcMM("参数值", "远程服务与管理平台域名长度 M", resvData, dataIndex, 1, onlyData);
                                    msgs.add(getSrcMM("参数值", "远程服务与管理平台域名长度 M", resvData, dataIndex, 1, onlyData));
                                    dataIndex += 1;
                                    yuming_count = (int) Double.parseDouble(pingtaiComLen.getValue());
                                    break;
                                case 5:
                                    for (int j = 0; j < yuming_count; j++) {
                                        msgs.add(getSrcMM("参数值", "远程服务与管理平台域名", resvData, dataIndex, 1, onlyData));
                                        dataIndex += 1;
                                    }
                                    break;

                                case 6:
                                    msgs.add(getDataMM("参数值", "正常时，信息上报时间周期", resvData, dataIndex, 2, 0, 0, 0, 65531, onlyData, null));
                                    dataIndex += 2;
                                    break;
//                                case 7:
//                                    msgs.add(getSrcMM("参数值", "硬件版本", resvData, dataIndex, 5));
//                                    dataIndex += 5;
//                                    break;
//                                case 8:
//                                    msgs.add(getSrcMM("参数值", "固件版本", resvData, dataIndex, 1));
//                                    dataIndex += 1;
//                                    break;
                                case 9:
                                    msgs.add(getDataMM("参数值", "车载终端心跳发送周期", resvData, dataIndex, 1, 0, 0, 1, 240, onlyData, null));
                                    dataIndex += 1;
                                    break;
                                case 10:
                                    msgs.add(getDataMM("参数值", "终端应答超时时间", resvData, dataIndex, 2, 0, 0, 1, 600, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 11:
                                    msgs.add(getDataMM("参数值", "平台应答超时时间", resvData, dataIndex, 2, 0, 0, 1, 600, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 12:
                                    msgs.add(getDataMM("参数值", "连续三次登入失败后，到下一次登入的间隔时间", resvData, dataIndex, 1, 0, 0, 1, 240, onlyData, null));
                                    dataIndex += 1;
                                    break;
                                case 13:
                                    MessageBean temp = getSrcMM("参数值", "公共平台域名长度N", resvData, dataIndex, 1, onlyData);
                                    msgs.add(getSrcMM("参数值", "公共平台域名长度N", resvData, dataIndex, 1, onlyData));
                                    yumingchangdu = (int) Double.parseDouble(temp.getValue());
                                    break;
                                case 14:
                                    msgs.add(getSrcMM("参数值", "公共平台域名", resvData, dataIndex, yumingchangdu, onlyData));
                                    dataIndex += yumingchangdu;
                                    break;
                                case 15:
                                    msgs.add(getDataMM("参数值", "公共平台端口", resvData, dataIndex, 2, 0, 0, 0, 65531, onlyData, null));
                                    dataIndex += 2;
                                    break;
                                case 16:
                                    msgs.add(getSrcMM("参数值", "是否处于抽样监测中", resvData, dataIndex, 1, onlyData));
                                    dataIndex += 1;
                                    break;
                                default:
                                    break;
                            }
                        }

                        break;
                    }
                    case 130: {//车载终端控制命令
                        msgs.add(getTime("主体", "时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        if (dataIndex >= size - 1) {
                            break;
                        }
                        MessageBean cmdd = getSrcMM("主体", "命令ID", resvData, dataIndex, 1, onlyData);
                        msgs.add(getSrcMM("主体", "命令ID", resvData, dataIndex, 1, onlyData));
                        dataIndex += 1;
                        int cmddNum = (int) Double.parseDouble(cmdd.getValue());
                        switch (cmddNum) {
                            case 1:
                                msgs.add(getSrcMM("控制命令", "远程升级", resvData, dataIndex, size - dataIndex, onlyData));
                                //MessageBean yuancheng = getSrcMM("控制命令", "远程升级", resvData, dataIndex, resvData.length-dataIndex);
                                dataIndex += (size - dataIndex);
                                break;
                            case 2:
                                msgs.add(getSrcMM("控制命令", "车载终端关机", resvData, dataIndex, size - dataIndex, onlyData));
                                //  dataIndex+=(resvData.length-dataIndex);
                                dataIndex += (size - dataIndex);
                                break;
                            case 3:
                                msgs.add(getSrcMM("控制命令", "车载终端复位", resvData, dataIndex, size - dataIndex, onlyData));
                                break;
                            case 4:
                                msgs.add(getSrcMM("控制命令", "车载终端恢复出厂设置", resvData, dataIndex, size - dataIndex, onlyData));
                                break;
                            case 5:
                                msgs.add(getSrcMM("控制命令", "断开数据通信链路", resvData, dataIndex, size - dataIndex, onlyData));
                                break;
                            case 6:
                                msgs.add(getSrcMM("控制命令", "车载终端报警/预警", resvData, dataIndex, size - dataIndex, onlyData));
                                break;
                            case 7:
                                msgs.add(getSrcMM("控制命令", "开启抽样监测链路", resvData, dataIndex, size - dataIndex, onlyData));
                                break;

                            default:
                                //非国标部分 原样显示
                                msgs.add(getSrcMM("控制命令", "未解析", resvData, dataIndex, size - dataIndex, onlyData));
                                dataIndex += (size - dataIndex);
                                break;


                        }
                        if (dataIndex >= resvData.length || dataIndex > (size - 1)) {
                            break;
                        }
                        MessageBean teSum = getDataMM("主体", "命令ID", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null);
                        double tempSum = Double.parseDouble(teSum.getValue());
                        msgs.add(getDataMM("主体", "命令ID", resvData, dataIndex, 1, 0, 0, 0, 252, onlyData, null));
                        break;

                    }
                    default:
                        break;


                }
            } else {
                MessageBean bccBean = new MessageBean();
                bccBean.setName("报文错误");
                bccBean.setValue("未知错误");
                msgs.add(bccBean);
            }


        } catch (Exception localException) {

            MessageBean bccBean = new MessageBean();
            bccBean.setName("报文错误");
            bccBean.setValue("未知错误");
            bccBean.setSrc(localException.getMessage());
            msgs.add(bccBean);
        }

        return msgs;
    }


    public static int byteToInt(byte val) {
        return val & 0xFF;
    }

    //偏移量           //保留小数位         最大最小值
    public static MessageBean getDataMM(String group, String name, byte[] data, int index,
                                        int len, double offset, double factor, double down,
                                        double up, boolean onlyData, String seqNo) {
        MessageBean mm = new MessageBean();
        mm.setName(name);
        mm.setFactor(factor);
        mm.setOffset(offset);
        mm.setGroup(group);
        mm.setSeqNo(seqNo);
        replaceAllUint(name, mm);
        BigDecimal fac = new BigDecimal(String.valueOf(factor));
        factor = fac.doubleValue();


        int res = 0;
        for (int i = 0; i < len; i++) {
            res += byteToInt(data[index + i]) << ((len - i - 1) * 8);
        }
        BigDecimal b1 = new BigDecimal(Double.toString(res));
        BigDecimal b2 = new BigDecimal(Double.toString(factor));

        BigDecimal offset1 = BigDecimal.valueOf(offset);
        BigDecimal val2 = (b1.multiply(b2).subtract(offset1));

        //double val = (b1.multiply(b2).doubleValue()) - offset;
        double val = val2.doubleValue();
        boolean valid=true;
        if(len==1){
            if (res==0xff||res==0xfe){
                valid=false;
            }
        }
        else if(len==2){
            if (res==0xffff||res==0xfffe){
                valid=false;
            }
        }
        else if (len==4){
            if (res==0xffffffff||res==0xfffffffe){
                valid=false;
            }
        }
        if (valid) {

            if (val > up || val < down) {
                mm.setState(1);
                mm.setTips(String.format("数值范围异常,正常范围为:%s - %s", String.valueOf(down), String.valueOf(up)));
            }
        }
        else {
            mm.setState(1);
            mm.setTips(String.format("数值异常或无效"));
            mm.setValue(String.valueOf(res));
        }
        mm.setValue(String.valueOf(val));
        mm.setLongValue((long) val);
        mm.setLen(len);
        mm.setIndex(index);
        mm.setUp(up);
        mm.setDown(down);
//        if (onlyData) {
//            return mm;
//        }
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < len; j++) {
            sb.append(String.format("%02X ", data[index + j]));
        }
        mm.setSrc(sb.toString());

        return mm;


    }

    //偏移量先减后乘
    public static MessageBean getDataMM2(String group, String name, byte[] data, int index, int len, double offset, double factor, double down, double up) {
        MessageBean mm = new MessageBean();
        mm.setName(name);
        mm.setFactor(factor);
        mm.setOffset(offset);
        mm.setGroup(group);
        replaceAllUint(name, mm);
        BigDecimal fac = new BigDecimal(String.valueOf(factor));
        factor = fac.doubleValue();


        int res = 0;
        for (int i = 0; i < len; i++) {
            res += byteToInt(data[index + i]) << ((len - i - 1) * 8);
        }
        BigDecimal b1 = new BigDecimal(Double.toString(res));
        BigDecimal b2 = new BigDecimal(Double.toString(factor));

        BigDecimal aa = BigDecimal.valueOf(b1.doubleValue() - offset);
        BigDecimal val2 = aa.multiply(b2);
        double val = val2.doubleValue();
        if (val > up || val < down) {
            mm.setState(1);
        }
        mm.setValue(String.valueOf(val));
        mm.setLongValue((long) val);
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < len; j++) {
            sb.append(String.format("%02X ", data[index + j]));
        }
        mm.setSrc(sb.toString());
        mm.setLen(len);
        mm.setIndex(index);
        mm.setUp(up);
        mm.setDown(down);

        return mm;


    }


    /**
     * 获取源码解析
     *
     * @param group
     * @param name
     * @param data
     * @param index
     * @param len
     * @return
     */
    public static MessageBean getSrcMM(String group, String name, byte[] data, int index,
                                       int len, boolean onlyData) {
        return getSrcMM(group, name, data, index, len, "0", RuleTypeEnum.GB, onlyData);

    }

    /**
     * 获取源码解析
     *
     * @param group
     * @param name
     * @param data
     * @param index
     * @param len
     * @param seqNo
     * @return
     */
    public static MessageBean getSrcMM(String group, String name, byte[] data, int index, int len,
                                       final String seqNo, RuleTypeEnum ruleCode, boolean onlyData) {
        MessageBean mm = new MessageBean();
        mm.setName(name);
        mm.setGroup(group);
        if (onlyData) {
            return mm;
        }

        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < len; j++) {
            sb.append(String.format("%02X ", data[index + j]));
        }

        if (sb.length() <= 20) {
            mm.setValue(sb.toString().trim());
        } else {
            mm.setValue("");
        }
        if (len == 1) {
            String curVal = byteToInt(data[index]) + "";
            ITranslate translate = TranslateFactory.getInstance().getTranslate(ruleCode.getCode(), seqNo);
            if (translate != null) {
                if (StringUtils.isNotEmpty(curVal)) {
                    try {
                        Map<String, String> tmpMap = translate.translate(seqNo, curVal, RuleTypeEnum.getRuleEnum(ruleCode.getCode()));
                        String descKey = seqNo + TRANSLATE_POSTFIX;
                        if (tmpMap.containsKey(descKey)) {
                            mm.setValue(tmpMap.get(descKey));
                        }
                    } catch (Exception e) {
                    }

                }

            }
        }


        mm.setSrc(sb.toString().trim());
        mm.setLen(len);
        mm.setIndex(index);

        return mm;

    }


    public static MessageBean getTime(String group, String name, byte[] data, int index, int len, boolean onlyData) {
        MessageBean mm = new MessageBean();
        mm.setName(name);
        mm.setGroup(group);
        mm.setLen(len);
        mm.setIndex(index);
        if (onlyData) {
            return mm;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(String.format("%02d-%02d-%02d %02d:%02d:%02d", data[index + 0], data[index + 1], data[index + 2], data[index + 3], data[index + 4], data[index + 5]));

        if (sb.length() <= 20) {
            mm.setValue(sb.toString());
        } else {
            mm.setValue("");
        }
        StringBuffer st = new StringBuffer();
        for (int j = 0; j < len; j++) {
            st.append(String.format("%02X ", data[index + j]));
        }
        mm.setSrc(st.toString());

        return mm;
    }


    public static MessageBean getDescMM(String group, String name, byte[] data, int index,
                                        int len, boolean onlyData) {
        MessageBean mm = new MessageBean();
        mm.setName(name);
        mm.setFactor(1);
        mm.setOffset(0);
        mm.setGroup(group);
        if (onlyData) {
            return mm;
        }

        byte[] desc = new byte[len];
        System.arraycopy(data, index, desc, 0, len);

        for (int i = 0; i < desc.length; i++) {
            if (desc[i] <= 0x1F || desc[i] == 0x7f) {
                //不可显示字符
                desc[i] = 0X20;
            }
        }

        String val = "";
        try {
            val = new String(desc, "gbk");
        } catch (UnsupportedEncodingException e) {
            log.error("error", e);
        }

        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < len; j++) {
            sb.append(String.format("%02X ", data[index + j]));
        }

        //判断是否为vin,并且为空
        if (mm.getName().equals("VIN") && desc[0] == 0x00 && desc[len-1] == 0x00){
            val = "00000000000000000";
        }

        mm.setValue(val);
        mm.setSrc(sb.toString());
        mm.setLen(len);
        mm.setIndex(index);

        return mm;
    }

    private static void replaceAllUint(String name, MessageBean mm) {
        if ("燃料电池数据".equals(mm.getGroup())){
            if ("燃料消耗率".equals(name)){
                mm.setUnit("kg/km");
            }
        }
        if ("发动机数据".equals(mm.getGroup())){
            if ("燃料消耗率".equals(name)){
                mm.setUnit("L/km");
            }
        }
        switch (name){
            case "车速":
                mm.setUnit("km/h");
                break;
            case "累计里程":
                mm.setUnit("km");
                break;
            case "总电压":
                mm.setUnit("V");
                break;
            case "总电流":
                mm.setUnit("A");
                break;
            case "SOC":
                mm.setUnit("%");
                break;
            case "绝缘电阻":
                mm.setUnit("Ω");
                break;
            case "加速踏板行程值":
                mm.setUnit("%");
                break;
            case "制动踏板状态":
                mm.setUnit("%");
                break;
            case "驱动电机控制器温度":
                mm.setUnit("℃");
                break;
            case "驱动电机转速":
                mm.setUnit("r/min");
                break;
            case "驱动电机转矩":
                mm.setUnit("N*m");
                break;
            case "驱动电机温度":
                mm.setUnit("℃");
                break;
            case "电机控制器输入电压":
                mm.setUnit("V");
                break;
            case "电机控制器直流母线电流":
                mm.setUnit("A");
                break;
            case "燃料电池电压":
                mm.setUnit("V");
                break;
            case "燃料电池电流":
                mm.setUnit("A");
                break;
            case "探针温度值":
                mm.setUnit("℃");
                break;
            case "氢系统中最高温度":
                mm.setUnit("℃");
                break;
            case "氢气最高浓度":
                mm.setUnit("ppm");
                break;
            case "氢气最高压力":
                mm.setUnit("MPa");
                break;
            case "曲轴转速":
                mm.setUnit("rpm");
                break;
            case "经度":
                mm.setUnit("°");
                break;
            case "纬度":
                mm.setUnit("°");
                break;
            case "电池单体电压最高值":
                mm.setUnit("V");
                break;
            case "电池单体电压最低值":
                mm.setUnit("V");
                break;
            case "最高温度值":
                mm.setUnit("℃");
                break;
            case "最低温度值":
                mm.setUnit("℃");
                break;
            default:
                break;
        }
    }

    public static String getCmdDesc(int cmd) {
        if (cmd == 0x01) {
            return "注册";
        } else if (cmd == 0x02) {
            return "实时信息上报";
        } else if (cmd == 0x03) {
            return "状态信息上报";
        } else if (cmd == 0x04) {
            return "心跳";
        } else if (cmd == 0x05) {
            return "补发信息上报";
        } else if (cmd >= 0x06 && cmd <= 0x7F) {
            return "系统预留";
        } else if (cmd == 0x80) {
            return "查询命令";
        } else if (cmd == 0x81) {
            return "设置命令";
        } else if (cmd == 0x82) {
            return "终端控制命令";
        } else if (cmd >= 0x83 && cmd <= 0xBF) {
            return "系统预留";
        } else if (cmd >= 0xC0 && cmd >= 0xFe) {
            return "平台交换协议命令";
        } else {
            return "未定义";
        }
    }

    public static String getFlagDes(byte flag) {
        if (flag == 0x01) {
            return "单体蓄电池电压数据";
        } else if (flag == 0x02) {
            return "动力蓄电池包温度数据";
        } else if (flag == 0x03) {
            return "整车数据";
        } else if (flag == 0x04) {
            return "卫星定位系统数据";
        } else if (flag == 0x05) {
            return "极值数据";
        } else if (flag == 0x06) {
            return "报警数据";
        } else if (flag >= 0x07 && flag <= 0x2F) {
            return "平台交换协议数据";
        } else if (flag >= 0x30 && flag <= 0x7F) {
            return "预留";
        } else if (flag >= 0x80 || flag <= -1) {
            return String.format("用户自定义:%02X", flag);
        } else {
            return "未定义";
        }
    }

    public static String getG6FlagDesStation(byte flag, Integer type) {
        String end = null != type ? type.toString() : "";
        if (flag == 0x01) {
            return "OBD信息" + end;
        } else if (flag == 0x02) {
            return "数据流信息" + end;
        } else if (flag >= 0x03 && flag <= 0x7F) {
            return "预留" + end;
        } else if (flag >= 0x80 && flag <= 0xFE) {
            return "用户自定义" + end;
        } else {
            return "未定义" + end;
        }
    }

    public static String getFlagDesStation(byte flag) {
        if (flag == 0x01) {
            return "整车数据";
        } else if (flag == 0x02) {
            return "驱动电机数据";
        } else if (flag == 0x03) {
            return "燃料电池数据";
        } else if (flag == 0x04) {
            return "发动机数据";
        } else if (flag == 0x05) {
            return "车辆位置数据";
        } else if (flag == 0x06) {
            return "极值数据";
        } else if (flag == 0x07) {
            return "报警数据";
        } else if (flag == 0x08) {
            return "可充电储能装置电压数据";
        } else if (flag == 0x09) {
            return "可充电储能装置温度数据";
        } else if (flag >= 0x0A && flag <= 0x2F) {
            return "平台交换协议自定义数据";
        } else if (flag >= 0x30 && flag <= 0x7F) {
            return "预留";
        } else if (flag >= 0x80 || flag <= -1) {
            return String.format("用户自定义:%02X", flag);
        } else {
            return "未定义";
        }
    }

    public static byte getBCC(byte buf[], int start, int len) {
        byte bccData = 0;
        for (int i = 0; i < len; i++) {
            bccData = (byte) (bccData ^ buf[i + start]);
        }
        return bccData;
    }


    public static List uploadPacketData(byte[] packetData, int size, boolean onlyData) {
        List<MessageBean> msgs = new ArrayList<MessageBean>();
        try {
            int nSize = size;

            byte[] resvData = packetData;
            byte[] rtuID = new byte[17];

            int dataIndex = 0;
            int findIndex = 0;
            if ((resvData[dataIndex] == 0x23) && (resvData[(dataIndex + 1)] == 0x23)) {
                msgs.add(getDescMM("头部", "起始符", resvData, dataIndex, 2, onlyData));
                dataIndex += 2;
                int cmd = byteToInt(resvData[(dataIndex)]);
                msgs.add(getSrcMM("头部", "命令单元", resvData, dataIndex, 2, onlyData));
                dataIndex += 2;
                msgs.add(getDescMM("头部", "VIN", resvData, dataIndex, 17, onlyData));
                dataIndex += 17;
                msgs.add(getSrcMM("头部", "数据加密方式", resvData, dataIndex, 1, onlyData));
                dataIndex += 1;
                MessageBean lenMM = getDataMM("头部", "长度", resvData, dataIndex, 2, 0, 1, 0, 65534, onlyData, null);
                long packLen = dataIndex + lenMM.getLongValue();
                msgs.add(lenMM);
                dataIndex += 2;

                byte bcc = getBCC(resvData, 0, 24 + (int) lenMM.getLongValue());

                if (bcc != resvData[nSize - 1]) {
                    MessageBean bccBean = new MessageBean();
                    bccBean.setName("校验码");
                    bccBean.setState(2);
                    bccBean.setGroup("尾部");
                    bccBean.setValue(String.format("报文校验码:%02X,正确校验码:%02X", resvData[nSize - 1], bcc));
                    msgs.add(bccBean);

                }
                //解析命令单元
                switch (cmd) {
                    case 1: {
                        msgs.add(getTime("主部", "注册时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getDataMM("主部", "注册流水号", resvData, dataIndex, 2, 0, 1, 1, 65534, onlyData, null));
                        dataIndex += 2;
                        msgs.add(getDescMM("主部", "车牌号", resvData, dataIndex, 8, onlyData));
                        dataIndex += 8;
                        msgs.add(getDescMM("主部", "车载终端编号", resvData, dataIndex, 12, onlyData));
                        dataIndex += 12;
                        int packCount = byteToInt(resvData[(dataIndex)]);
                        msgs.add(getDataMM("主部", "动力蓄电池包总数N", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                        dataIndex += 1;
                        for (int n = 0; n < packCount; n++) {
                            msgs.add(getSrcMM("主部", "动力蓄电池包序号", resvData, dataIndex, 1, onlyData));
                            dataIndex += 1;
                            msgs.add(getDescMM("主部", "动力蓄电池包编码", resvData, dataIndex, 14, onlyData));
                            dataIndex += 14;
                            msgs.add(getSrcMM("主部", "动力蓄电池包预留", resvData, dataIndex, 5, onlyData));
                            dataIndex += 5;
                        }

                        break;
                    }
                    case 2:
                    case 5: {
                        msgs.add(getTime("主部", "数据采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        while (dataIndex < packLen) {

                            MessageBean typeBean = getDataMM("头部", "信息类型", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null);
                            typeBean.setValue(getFlagDes((byte) typeBean.getLongValue()));
                            msgs.add(typeBean);
                            dataIndex += 1;
                            long x;
                            int minvpackNO;
                            int minvNO;
                            byte dataType = (byte) typeBean.getLongValue();
                            if (dataType == 0x01) {
                                MessageBean batteryCountBean = getDataMM(getFlagDes(dataType), "单体蓄电池总数", resvData, dataIndex, 2, 0, 1, 1, 65534, onlyData, null);
                                msgs.add(batteryCountBean);
                                dataIndex += 2;
                                long batteryCount = batteryCountBean.getLongValue();

                                MessageBean batteryPackCountBean = getDataMM(getFlagDes(dataType), "单体蓄电池总包数", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null);
                                msgs.add(batteryPackCountBean);
                                dataIndex += 1;
                                long batteryPackCount = batteryPackCountBean.getLongValue();
                                for (int i = 0; i < batteryPackCount; i++) {
                                    msgs.add(getDataMM(getFlagDes(dataType), "动力蓄电池包序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                    dataIndex += 1;

                                    MessageBean batteryPackVCountBean = getDataMM(getFlagDes(dataType), "包电池总数", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null);
                                    msgs.add(batteryPackVCountBean);
                                    dataIndex += 1;

                                    long batteryPackVCount = batteryPackVCountBean.getLongValue();
                                    for (int j = 1; j <= batteryPackVCount; j++) {
                                        msgs.add(getDataMM(getFlagDes(dataType), String.format("单体电压%d-%d", i + 1, j), resvData, dataIndex, 2, 0, 0.001d, 0, 15, onlyData, null));
                                        dataIndex += 2;
                                    }
                                }
                            } else if (dataType == 0x02) {
                                msgs.add(getDataMM(getFlagDes(dataType), "温度探针总数", resvData, dataIndex, 2, 0, 1, 1, 65534, onlyData, null));
                                dataIndex += 2;
                                MessageBean temPackCountBean = getDataMM(getFlagDes(dataType), "电池包总数N", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null);
                                msgs.add(temPackCountBean);
                                dataIndex += 1;
                                long packCount = temPackCountBean.getLongValue();
                                for (int i = 0; i < packCount; i++) {
                                    msgs.add(getDataMM(getFlagDes(dataType), "动力蓄电池包序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                    dataIndex += 1;
                                    MessageBean tempCountBean = getDataMM(getFlagDes(dataType), "该包探针数", resvData, dataIndex, 1, 0, 1, 0, 254, onlyData, null);
                                    dataIndex += 1;
                                    msgs.add(tempCountBean);
                                    long packDataCount = tempCountBean.getLongValue();
                                    for (int j = 1; j <= packDataCount; j++) {
                                        msgs.add(getDataMM(getFlagDes(dataType), "温度" + (i + 1) + "-" + j, resvData, dataIndex, 1, 40, 1, -40, 125, onlyData, null));
                                        dataIndex += 1;

                                    }
                                }
                            } else if (dataType == 0x03) {
                                msgs.add(getDataMM(getFlagDes(dataType), "车速", resvData, dataIndex, 2, 0, 0.1d, 0, 220, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "里程", resvData, dataIndex, 4, 0, 0.1d, 0, 999999.9d, onlyData, null));
                                dataIndex += 4;
                                msgs.add(getDataMM(getFlagDes(dataType), "档位", resvData, dataIndex, 1, 0, 1, 0, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "加速踏板行程值", resvData, dataIndex, 1, 0, 1, 0, 100, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "制动踏板行程值", resvData, dataIndex, 1, 0, 1, 0, 100, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "充放电状态", resvData, dataIndex, 1, 0, 1, 0, 255, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "电机控制器温度", resvData, dataIndex, 1, 40, 1, -40, 210, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "电机转速", resvData, dataIndex, 2, 0, 1, 0, 65534, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "电机温度", resvData, dataIndex, 1, 40, 1, -40, 210, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "电机电压", resvData, dataIndex, 2, 0, 0.1d, 0, 6000, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "电机电流", resvData, dataIndex, 2, 1000, 0.1d, -1000, 1000, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "空调设定温度", resvData, dataIndex, 1, 0, 1, 0, 200, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getSrcMM(getFlagDes(dataType), "预留", resvData, dataIndex, 7, onlyData));
                                dataIndex += 7;

                            } else if (dataType == 0x04) {
                                msgs.add(getDataMM(getFlagDes(dataType), "定位状态", resvData, dataIndex, 1, 0, 1, 0, 7, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "经度", resvData, dataIndex, 4, 0, 0.000001d, 0, 180, onlyData, null));
                                dataIndex += 4;
                                msgs.add(getDataMM(getFlagDes(dataType), "纬度", resvData, dataIndex, 4, 0, 0.000001d, 0, 90, onlyData, null));
                                dataIndex += 4;
                                msgs.add(getDataMM(getFlagDes(dataType), "速度", resvData, dataIndex, 2, 0, 0.1d, 0, 220, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "方向", resvData, dataIndex, 2, 0, 1, 0, 359, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getSrcMM(getFlagDes(dataType), "预留", resvData, dataIndex, 4, onlyData));
                                dataIndex += 4;

                            } else if (dataType == 0x05) {
                                msgs.add(getDataMM(getFlagDes(dataType), "最高电压电池包序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最高电压电池序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最高电压", resvData, dataIndex, 2, 0, 0.001d, 0, 15, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "最低电压电池包序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最低电压电池序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最低电压", resvData, dataIndex, 2, 0, 0.001d, 0, 15, onlyData, null));
                                dataIndex += 2;

                                msgs.add(getDataMM(getFlagDes(dataType), "最高温度电池包序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最高温度电池序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最高温度", resvData, dataIndex, 1, 40, 1, -40, 125, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最低温度电池包序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最低温度电池序号", resvData, dataIndex, 1, 0, 1, 1, 254, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "最低温度", resvData, dataIndex, 1, 40, 1, -40, 125, onlyData, null));
                                dataIndex += 1;

                                msgs.add(getDataMM(getFlagDes(dataType), "总电压", resvData, dataIndex, 2, 0, 0.1d, 0, 1000, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "总电流", resvData, dataIndex, 2, 1000, 0.1d, -1000, 1000, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "SOC", resvData, dataIndex, 1, 0, 0.4d, 0, 100, onlyData, null));
                                dataIndex += 1;
                                msgs.add(getDataMM(getFlagDes(dataType), "剩余能量", resvData, dataIndex, 2, 0, 0.1d, 0, 999.9d, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getDataMM(getFlagDes(dataType), "绝缘电阻", resvData, dataIndex, 2, 0, 1, 0, 9999, onlyData, null));
                                dataIndex += 2;
                                msgs.add(getSrcMM(getFlagDes(dataType), "预留", resvData, dataIndex, 5, onlyData));
                                dataIndex += 5;
                            } else if (dataType == 0x06) {

                                msgs.add(getSrcMM(getFlagDes(dataType), "动力蓄电池报警标志", resvData, dataIndex, 2, onlyData));
                                dataIndex += 2;

                                MessageBean warnByteCountBean = getDataMM(getFlagDes(dataType), "动力蓄电池其他故障总数", resvData, dataIndex, 1, 0, 1, 0, 254, onlyData, null);
                                dataIndex += 1;
                                msgs.add(warnByteCountBean);
                                long batteryWarnCount = warnByteCountBean.getLongValue();
                                msgs.add(getSrcMM(getFlagDes(dataType), "动力蓄电池其他故障代码列表", resvData, dataIndex, (int) batteryWarnCount, onlyData));
                                dataIndex += batteryWarnCount;

                                MessageBean warnByteCountBean1 = getDataMM(getFlagDes(dataType), "电机故障总数", resvData, dataIndex, 1, 0, 1, 0, 254, onlyData, null);
                                dataIndex += 1;
                                msgs.add(warnByteCountBean1);
                                long batteryWarnCount1 = warnByteCountBean1.getLongValue();
                                msgs.add(getSrcMM(getFlagDes(dataType), "电机故障代码列表", resvData, dataIndex, (int) batteryWarnCount1, onlyData));
                                dataIndex += batteryWarnCount1;

                                MessageBean warnByteCountBean2 = getDataMM(getFlagDes(dataType), "其他故障总数", resvData, dataIndex, 1, 0, 1, 0, 254, onlyData, null);
                                dataIndex += 1;
                                msgs.add(warnByteCountBean2);
                                long batteryWarnCount2 = warnByteCountBean1.getLongValue();
                                msgs.add(getSrcMM(getFlagDes(dataType), "其他故障代码列表", resvData, dataIndex, (int) batteryWarnCount2, onlyData));
                                dataIndex += batteryWarnCount2;
                            } else if (dataType >= 0x80 || dataType <= -1) {
                                MessageBean userdefineLenMM = getDataMM(getFlagDes(dataType), "长度", resvData, dataIndex, 2, 0, 1, 0, 65534, onlyData, null);
                                msgs.add(userdefineLenMM);
                                dataIndex += 2;

                                long userdefineLen = userdefineLenMM.getLongValue();
                                msgs.add(getSrcMM(getFlagDes(dataType), "自定义", resvData, dataIndex, (int) userdefineLen, onlyData));
                                dataIndex += userdefineLen;
                            }
                        }
                        break;
                    }

                    case 3: {
                        msgs.add(getTime("主体", "信息采集时间", resvData, dataIndex, 6, onlyData));
                        dataIndex += 6;
                        msgs.add(getSrcMM("主体", "状态标志", resvData, dataIndex, 1, onlyData));
                        dataIndex += 1;
                        msgs.add(getSrcMM("主体", "预留", resvData, dataIndex, 4, onlyData));
                        dataIndex += 4;
                        break;
                    }
                    //心跳
                    case 4: {

                        break;
                    }
                    //
                }


            } else {
                MessageBean bccBean = new MessageBean();
                bccBean.setName("报文错误");
                bccBean.setValue("未知错误");
                msgs.add(bccBean);
            }


        } catch (Exception localException) {

            MessageBean bccBean = new MessageBean();
            bccBean.setName("报文错误");
            bccBean.setValue("未知错误");
            bccBean.setSrc(localException.getMessage());
            msgs.add(bccBean);
        }

        return msgs;
    }

    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs.toUpperCase();
    }

}
