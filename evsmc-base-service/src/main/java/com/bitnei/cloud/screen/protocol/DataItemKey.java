package com.bitnei.cloud.screen.protocol;

/**
 * 协议数据项
 *
 * @author xuzhijie
 */
public class DataItemKey {

    // region 实时数据

    /***
     * 最后通讯时间
     */
    public static final String v2000 = "2000";

    // region 整车数据

    /**
     * 车速
     */
    public static final String v2201 = "2201";

    /**
     * 累计里程
     */
    public static final String v2202 = "2202";

    /**
     * 档位
     */
    public static final String v2203 = "2203";

    /**
     * SOC
     */
    public static final String v2615 = "2615";
    /**
     * 充放电状态 124充电
     */
    public static final String v2301 = "2301";

    /**
     * 动力方式
     * 1：纯电、2：混动、3：燃油、254：异常、255：无效
     */
    public static final String v2213 = "2213";

    /**
     * 车辆状态
     * 1：车辆启动、2：车辆熄火、3：其他状态、254：异常、255：无效
     */
    public static final String v3201 = "3201";

    // endregion

    // region 车辆位置数据

    /**
     * 经度
     */
    public static final String v2502 = "2502";

    /**
     * 纬度
     */
    public static final String v2503 = "2503";

    /**
     * 经度 - 高德
     */
    public static final String v2502_GD = "2502.gd";

    /**
     * 纬度 - 高德
     */
    public static final String v2503_GD = "2503.gd";

    /**
     * 经度 - 百度
     */
    public static final String v2502_BD = "2502.bd";

    /**
     * 纬度 - 百度
     */
    public static final String v2503_BD = "2503.bd";

    // endregion

    // region 单体电池电压

    /**
     * 单体电压
     */
    public static final String v2003 = "2003";

    // endregion

    // region 单体电池温度

    /**
     * 单体温度
     */
    public static final String v2103 = "2103";

    // endregion

    // region 驱动电机数据

    /**
     * 驱动电机转速
     */
    public static final String v2303 = "2303";

    /**
     * 驱动电机转矩
     */
    public static final String v2311 = "2311";

    /**
     * 电机控制器温度
     */
    public static final String v2302 = "2302";

    /**
     * 电机温度
     */
    public static final String v2304 = "2304";

    /**
     * 驱动电机总成信息列表(国标)
     */
    public static final String v2308 = "2308";

    /**
     * 驱动电机状态（地标）
     * 1：耗电状态、2：发电状态、3：关闭状态、4：准备状态
     */
    public static final String v2310 = "2310";

    // endregion

    // region 发动机数据

    /**
     * 发动机状态
     * 1：启动状态、2：关闭状态、254：异常、255：无效
     */
    public static final String v2401 = "2401";

    /**
     * 曲轴转速
     */
    public static final String v2411 = "2411";

    /**
     * 燃料消耗率
     */
    public static final String v2413 = "2413";

    // endregion

    // region 燃料电池

    /**
     * 燃料电池电压
     */
    public static final String v2110 = "2110";

    /**
     * 燃料电池电流
     */
    public static final String v2111 = "2111";

    /**
     * 氢气最高浓度
     */
    public static final String v2117 = "2117";

    /**
     * 高压DC-DC状态
     */
    public static final String v2121 = "2121";

    /**
     * 燃料电池温度值列表[列表]
     */
    public static final String v2114 = "2114";

    // endregion

    //region 数据流信息

    /**
     * 反应剂余量
     */
    public static final String v60033 = "60033";

    /**
     * 油箱液位
     */
    public static final String v60039 = "60039";

    /**
     * 发动机冷却液温度
     */
    public static final String v60038 = "60038";

    /**
     * 发动机摩擦扭矩
     */
    public static final String v60028 = "60028";

    /**
     * 发动机转速
     */
    public static final String v60029 = "60029";

    /**
     * SCR 入口温度
     */
    public static final String v60035 = "60035";

    /**
     * SCR 出口温度
     */
    public static final String v60036 = "60036";

    // endregion

    // endregion

    // region 自定义项

    /**
     * 故障状态  0正常，1故障
     */
    public static final String v10001 = "10001";

    /**
     * 在线状态
     */
    public static final String v10002 = "10002";

    /**
     * 驱动电机状态
     * 【耗电状态，关闭状态】
     */
    public static final String POWER_LOSS_STATE = "powerLossState";

    /**
     * 驱动电机最大转速
     */
    public static final String MAX_ROTATING_SPEED = "maxRotatingSpeed";

    /**
     * 驱动电机最大转矩
     */
    public static final String MAX_ROTATING_TORQUE = "maxRotatingTorque";

    /**
     * 驱动电机控制器温度-最大温度
     */
    public static final String MAX_2302 = "max2302";

    /**
     * 驱动电机温度-最大温度
     */
    public static final String MAX_2304 = "max2304";

    /**
     * 当前车辆位置
     */
    public static final String ADDRESS = "address";

    // endregion

    /**
     * desc key
     *
     * @param itemKey
     * @return
     */
    public static  String translateDesc(String itemKey) {
        return itemKey + ".desc";
    }

}
