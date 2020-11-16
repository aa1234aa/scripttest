package com.bitnei.cloud.sys.common;


import com.bitnei.cloud.dc.domain.DataItem;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口数据项ID
 * Created by lenovo-1 on 2015/8/28.
 */
@Slf4j
 public  class DataItemKey {

    public static final String GPS_REGION="GPS_REGION";
    public static final String RegTime = "1001"; //注册
    public static final String VehicleState = "3202"; //车辆状态
    public static final String VehicleSpeed = "2201";//车速
    public static final String GPSSpeed = "2504";//GPS速度
    public static final String GPSSpeedNew = "4410004";//GPS速度新
    public static final String GPSMIL = "4410003";//GPS里程

    public static final String VehicleDistance = "2202";//累计里程
    public static final String VehicleGear = "2203";//档位
    public static final String AirConditioningTemp = "2210";//空调设定温度

    public static final String LocateState = "2501";//GPS定位状态
    public static final String Lng = "2502";//经度
    public static final String Lat = "2503";//纬度
    public static final String SOC = "2615";
    public static final String RtDateTime = "2000";//实时数据更新时间
    public static final String ServerTime="9999"; //服务器接收时间
    public static final String RemainEnergy = "2616";//剩余能量
    public static final String BatMaxV = "2603";//单体最高电压
    public static final String BatMinV = "2606";//单体最低电压
    public static final String BatTotalV = "2613";//总电压(可充电储能装置电压)
    public static final String BatTotalI = "2614";//总电流
    public static final String BatMaxT = "2609";//最高温度
    public static final String BatMinT = "2612";//最低温度

    public static final String BatVolCount = "2001";//单体电压个数
    public static final String item2002 = "2002";//动力蓄电池包总数  电压(可充电储能装置子系统号)
    public static final String BatVols = "2003";//单体电压

    public static final String BatTempCount = "2101";//单体温度个数(可充电储能温度探针个数)
    public static final String item2102 = "2102";//动力蓄电池包总数  温度
    public static final String BatTemps = "2103";//单体温度

    public static final String item7001 = "7001";//单体电压个数
    public static final String item7002 = "7002";//动力蓄电池包总数  电压
    public static final String item7003 = "7003";//单体电压

    public static final String item7101 = "7101";//单体温度个数
    public static final String item7102 = "7102";//动力蓄电池包总数  温度
    public static final String item7103 = "7103";//单体温度

    public static final String Resistance = "2617";//绝缘电阻
    public static final String MaxVPackIndex = "2601";//最高电压包序号
    public static final String MaxVPos = "2602";//最高电压包内位置
    public static final String MinVPackIndex = "2604";//最低电压包序号
    public static final String MinVPos = "2605";//最低电压包内位置
    public static final String MaxTPackIndex = "2607";//最高温度包序号
    public static final String MaxTPos = "2608";//最高温度包内位置
    public static final String MinTPackIndex = "2610";//最低温度包序号
    public static final String MinTPos = "2611";//最低温度包内位置
    public static final String MachineRotation = "2303";//电机转速
    public static final String MachineTemprature = "2304";//电机温度
    public static final String MachineVol = "2305";//电机电压
    public static final String MachineCur = "2306";//电机电流
    public static final String SpeedBoard = "2208";//加速踏板值
    public static final String BrakingBoard = "2209";//制动踏板值
    public static final String GPSDirection = "2505";//GPS方向
    //public static final String RcvDataTime = "3201";//信息采集时间
    public static final String EngineState = "2401";//发动机状态
    public static final String ECUTemperature = "2402";//ECU温度
    public static final String VehicleBatVolt = "2403";//车辆电池电压
    public static final String EngineTemperature = "2404";//发动机温度
    public static final String InVolt = "2405";//进气歧管气压
    public static final String InTemperature = "2406";//进气温度
    public static final String GasOutTemperature = "2407";//废气排出温度
    public static final String FuelPress = "2408";//燃料喷射压力
    public static final String FuelCapacity = "2409";//燃料喷射量
    public static final String Fire = "2410"; //点火提前角
    public static final String QZSpeed = "2411"; //曲轴转速
    public static final String OilDegree = "2412"; //油门开度
    public static final String AlarmState = "10001";//故障状态
    public static final String OnlineState = "10002";//在线状态
    public static final String ChargeState2301 = "2301";//充放电状态0x01：停车充电；0x02：行驶充电；0x03：未充电状态；0x04：充电完成；“0xFE”表示异常，“0xFF”表示无效。
    public static final String ChargeState = "10003"; //充放电状态    //是取 10003的 那个是判断后的状态  ，2301是判断前的状态
    public static final String CurDistance = "10004"; //当日里程
    public static final String LastUpdateTime = "10005";//最后更新时间
    public static final String ChgPileState = "4103";//充电枪使用状态
    public static final String ChgPileVol = "4105";//充电枪电压
    public static final String ChgPileCur = "4106";//充电枪电流



    //国标数据项

    public static final String item2205 = "2205";// 整车数据的驱动状态
    public static final String item2204 = "2204";//整车数据的制动状态

    public static final String item1020 = "1020";//终端登出流水号
    public static final String item1033 = "1033";//终端登出流水号

    public static final String item1021 = "1021";//ICCID
    public static final String item1022 = "1022";//可充电储能子系统数
    public static final String item1023 = "1023";//可充电储能系统编码长度
    public static final String item1024 = "1024";//可充电储能系统编码
    public static final String item1025 = "1025";//终端登入/时间
    public static final String item1031 = "1031";//终端登入/时间


    public static final String item1026 = "1026";//平台登入/登出时间
    public static final String item1027 = "1027";//平台登入/登出流水号
    public static final String item1028 = "1028";//平台用户名
    public static final String item1029 = "1029";//平台密码
    public static final String item1030 = "1030";//加密规则
    public static final String item2110 = "2110";//燃料电池电压
    public static final String item2111 = "2111";//燃料电池电流
    public static final String item2112 = "2112";//燃料消耗率
    public static final String item2113 = "2113";//燃料电池温度探针总数
    public static final String item2114 = "2114";//探针温度值
    public static final String item2115 = "2115";//氢系统中最高温度
    public static final String item2116 = "2116";//氢系统中最高温度探针代号
    public static final String item2117 = "2117";//氢气最高浓度
    public static final String item2118 = "2118";//氢气最高浓度传感器代号
    public static final String item2119 = "2119";//氢气最高压力
    public static final String item2120 = "2120";//氢气最高压力传感器代号
    public static final String item2121 = "2121";//高压DC/DC状态
    public static final String item3201 = "3201";//整车数据的车辆状态，11.18确认
    public static final String item2213 = "2213";//运行模式
    public static final String item2214 = "2214";//DC-DC状态
    //    public static final String item2215 = "2215";//整车数据的车辆状态
    public static final String item2307 = "2307";//驱动电机个数
    /**
     * "2308": "2309:1,
     *          2310:3,
     *          2311:20000,
     *          2302:88,
     *          2303:20000,
     *          2304:97,
     *          2305:5790,
     *          2306:10000"
     * ***/
    public static final String item2308 = "2308";//驱动电机总成信息列表
    public static final String item2309 = "2309";//驱动电机序号
    public static final String item2310 = "2310";//驱动电机状态
    public static final String item2311 = "2311";//驱动电机转矩
    public static final String item2413 = "2413";//燃料消耗率
    public static final String item2920 = "2920";//最高报警等级
    public static final String item2921 = "2921";//可充电储能装置故障总数N1
    // public static final String item2922 = "2922";//可充电储能装置故障代码列表
    public static final String item2923 = "2923";//发动机故障总数N3
    //public static final String item2924 = "2924";//发动机故障列表
    public static final String item3202 = "3202";//状态标志

    //新加
    public static final String item2302 = "2302";//驱动电机控制器温度



    //新加国标故障数据项
    public static final String item2901 = "2901";//温度差异报警
    public static final String item2902 = "2902";//电池极柱高温报警
    public static final String item2903 = "2903";//动力蓄电池包过压报警
    public static final String item2904 = "2904";//	动力蓄电池包欠压报警
    public static final String item2905 = "2905";//SOC低报警
    public static final String item2906 = "2906";//单体蓄电池过压报警
    public static final String item2907 = "2907";//单体蓄电池欠压报警
    public static final String item2908 = "2908";//SOC太低报警
    public static final String item2909 = "2909";//SOC过高报警
    public static final String item2910 = "2910";//动力蓄电池包不匹配报警
    public static final String item2911 = "2911";//	动力蓄电池一致性差报警
    public static final String item2912 = "2912";//绝缘故障


    public static final String item2913 = "2913";//2913: DC-DC温度报警
    public static final String item2914 = "2914";//  2914: 制动系统报警
    public static final String item2915 = "2915";//2915: DC-DC状态报警
    public static final String item2916 = "2916";// 2916: 驱动电机控制器温度报警
    public static final String item2917 = "2917";// 2917: 高压互锁状态报警
    public static final String item2918 = "2918";// 2918: 驱动电机温度报警
    public static final String item2919 = "2919";// 2919: 车载储能装置类型过充
    public static final String item2930 = "2930";//2930: SOC跳变报警


    //......
    public static final String item2802 = "2802";//动力蓄电池其他故障总数n
    public static final String item2809 = "2809";//其他故障代码列表
    public static final String item2803 = "2803";//动力蓄电池其他故障代码列表
    public static final String item2804 = "2804";//电机故障总数N
    public static final String item2805 = "2805";//电机故障代码列表
    public static final String item2806 = "2806";//发动机故障总数N1
    public static final String item2807 = "2807";//发动机故障列表
    public static final String item2808 = "2808";//其他故障总数M
    public static final String item2922 = "2922";//可充电储能装置故障代码列表
    public static final String item2924 = "2924";//发动机故障列表


    //组合数据项
    //运行状态，停止、运行、无
    public static final String CUSTOM_RUN_STATUS = "C_RUN_STATUE";
    //GPS是否有值
    public static final String CUSTOM_GPS_HAS_VAL = "C_GPS_HAS_VAL";
    //附近充电车
    public static final String NEAR_CHARGER_TRUCK = "nearChgCar";


    //有效
    public static final String USE_FUL = "useful_";


    /**
     *国六数据项-数据流信息
     */
    public static final String G6_ITEM_60039 = "60039";//油箱液位
    public static final String G6_ITEM_60030 = "60030";//发动机燃料流量
    public static final String G6_ITEM_60037 = "60037";//DPF压差
    public static final String G6_ITEM_60041 = "2502";//经度
    public static final String G6_ITEM_60028 = "60028";//摩擦扭矩
    public static final String G6_ITEM_60029 = "60029";//发动机转速
    public static final String G6_ITEM_60036 = "60036";//SCR出口温度
    public static final String G6_ITEM_60040 = "2501";//定位状态
    public static final String G6_ITEM_60032 = "60032";//SCR下游NOx传感器输出值
    public static final String G6_ITEM_60025 = "2201";//车速
    public static final String G6_ITEM_60034 = "60034";//进气量
    public static final String G6_ITEM_60042 = "2503";//纬度
    public static final String G6_ITEM_60026 = "60026";//大气压力
    public static final String G6_ITEM_60027 = "60027";//发动机净输出扭矩
    public static final String G6_ITEM_60038 = "60038";//发动机冷却液温度
    public static final String G6_ITEM_60035 = "60035";//入口温度
    public static final String G6_ITEM_60033 = "60033";//反应剂余量
    public static final String G6_ITEM_60043 = "2202";//累计里程
    public static final String G6_ITEM_60031 = "60031";//SCR上游NOx传感器输出值
    public static final String G6_ITEM_60011 = "2000";//报文时间
    public static final String G6_ITEM_ServerTime="RECVTIME"; //服务器接收时间
    /**
     *国六数据项-防拆除警报信息
     */
    public static final String G6_ITEM_60050 = "60050";//上报时间
    public static final String G6_ITEM_60051 = "60051";//防拆除警报流水号
    /**
     *国六数据项-obd信息
     */
    public static final String G6_ITEM_60019 = "60019";//IUPR值
    public static final String G6_ITEM_60020 = "60020";//故障码总数
    public static final String G6_ITEM_60015 = "60015";//诊断就绪状态
    public static final String G6_ITEM_60017 = "60017";//软件标定识别号
    public static final String G6_ITEM_60010 = "60010";//流水号
    public static final String G6_ITEM_60013 = "60013";//MIL状态
    public static final String G6_ITEM_60016 = "60016";//车辆识别码
    public static final String G6_ITEM_60021 = "60021";//故障码列表
    public static final String G6_ITEM_60014 = "60014";//诊断支持状态
    public static final String G6_ITEM_60018 = "60018";//标定验证码
    public static final String G6_ITEM_60012 = "60012";//OBD诊断协议
    /**
     *国六数据项-备案信息
     */
    public static final String G6_ITEM_60064 = "60064";//备案结果
    public static final String G6_ITEM_60063 = "60063";//vin
    public static final String G6_ITEM_60061 = "60061";//芯片ID
    public static final String G6_ITEM_60060 = "60060";//备案时间
    public static final String G6_ITEM_60062 = "60062";//公钥


    /**
     * 车辆在线查询数据项列表
     * @return
     */
    public static List<String> getOnlineStatus(){

        List<String> dataItems = new ArrayList<>();

        dataItems.add(DataItemKey.SOC);//soc,soc>0说明有can数据
        dataItems.add(DataItemKey.VehicleSpeed);//运行状态--车速
        dataItems.add(DataItemKey.ChargeState);//充电状态--充放电状态
        dataItems.add(DataItemKey.LocateState);//GPS有效性--定位状态
        dataItems.add(DataItemKey.AlarmState);//故障状态--是否故障
        dataItems.add(DataItemKey.OnlineState);//在线状态--是否在线
        dataItems.add(DataItemKey.LastUpdateTime);//最后更新时间
        dataItems.add(DataItemKey.Lng);//GPS是否有值--经度
        dataItems.add(DataItemKey.Lat);//GPS是否有值--纬度
        dataItems.add(DataItemKey.VehicleDistance);//累计里程
        dataItems.add(DataItemKey.USE_FUL+DataItemKey.VehicleDistance);//累计有效里程

        return dataItems;
    }

    public static final String[] getAllDataItemArray() {
        Field[] fields = DataItemKey.class.getDeclaredFields();
        List<String> queryItemsList = new ArrayList(fields.length);
        for(int i = 0, j = fields.length; i < j; i++){
            Field field = fields[i];
            field.setAccessible(true);
            String value = null;
            try {
                value = (String) field.get(DataItemKey.class);
            } catch (IllegalAccessException e) {
               log.error("error", e);
            }
            queryItemsList.add(value);
        }
        return queryItemsList.toArray(new String[queryItemsList.size()]);
    }

    public static final String getAllDataItemStr(){
        StringBuffer stringBuffer = new StringBuffer();
        Field[] fields = DataItemKey.class.getDeclaredFields();
        for(int i = 0, j = fields.length; i < j; i++){
            Field field = fields[i];
            field.setAccessible(true);
            String value = null;
            try {
                value = (String) field.get(DataItemKey.class);
            } catch (IllegalAccessException e) {
               log.error("error", e);
            }
            if(i==0){
                stringBuffer.append(value);
            }else{
                stringBuffer.append("," + value);
            }
        }
        return stringBuffer.toString();
    }

    public static final String[] getPowerBaseMakerItemArray() {

        List<String> dataItems = new ArrayList<>();

        dataItems.add(DataItemKey.ServerTime);//最后更新时间
        dataItems.add(DataItemKey.RtDateTime);//最后更新时间
        dataItems.add(DataItemKey.LastUpdateTime);
        dataItems.add(DataItemKey.VehicleSpeed);//当前车速
        dataItems.add(DataItemKey.SOC);//当前soc
        dataItems.add(DataItemKey.Lng);// GPS是否有值--经度-7-2502
        dataItems.add(DataItemKey.Lat);// GPS是否有值--纬度-8-2503
        dataItems.add(DataItemKey.OnlineState); //车辆在线状态
        dataItems.add(DataItemKey.VehicleDistance); //累计里程

        //国六
        dataItems.add(DataItemKey.G6_ITEM_60039); //油箱液位

        return dataItems.toArray(new String[0]);
    }

    public static final String[] getG6PowerBaseMakerItemArray() {

        List<String> dataItems = new ArrayList<>();

        dataItems.add(DataItemKey.G6_ITEM_60011);//最后更新时间
        dataItems.add(DataItemKey.LastUpdateTime);
        dataItems.add(DataItemKey.G6_ITEM_60025);//当前车速

        dataItems.add(DataItemKey.G6_ITEM_60041);// GPS是否有值--经度-7-2502
        dataItems.add(DataItemKey.G6_ITEM_60042);// GPS是否有值--纬度-8-2503
        dataItems.add(DataItemKey.OnlineState); //车辆在线状态
        dataItems.add(DataItemKey.G6_ITEM_60043); //累计里程
        dataItems.add(DataItemKey.G6_ITEM_ServerTime);//最后更新时间

        //国六
        dataItems.add(DataItemKey.G6_ITEM_60039); //油箱液位

        return dataItems.toArray(new String[0]);
    }

    public static final String desc(String item){
        return item+".desc";
    }
}
