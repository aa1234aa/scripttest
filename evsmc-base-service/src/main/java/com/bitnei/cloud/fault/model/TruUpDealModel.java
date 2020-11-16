package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;


/**
 * *  ┏┓　　　┏┓
 *     ┏┛┻━━━┛┻┓
 *     ┃　　　　　　　┃ 　
 *     ┃　　　━　　　┃
 *     ┃　┳┛　┗┳　┃
 *     ┃　　　　　　　┃
 *     ┃　　　┻　　　┃
 *     ┃　　　　　　　┃
 *     ┗━┓　　　┏━┛
 *     　　┃　　　┃神兽保佑
 *     　　┃　　　┃代码无BUG！
 *     　　┃　　　┗━━━┓
 *     　　┃　　　　　　　┣┓
 *     　　┃　　　　　　　┏┛
 *     　　┗┓┓┏━┳┓┏┛
 *     　　　┃┫┫　┃┫┫
 *     　　　┗┻┛　┗┻┛
 *
 *
 * 车型安全事故model
 * @author zhouxianzhou
 */
@Setter
@Getter
public class TruUpDealModel {

    /** 车辆型号 */
    private String vehModel;

    /** 电池型号 */
    private String batteryModel;

    /** 电池生产企业 */
    private String batteryUnit;

    /** 电池单体型号 */
    private String batteryMonModel;

    /** 电池单体生产企业 */
    private String batteryMonUnit;

    /** 运行城市 */
    private String useCity;

    /** 运营单位 */
    private String useUnitName;

    /** 车辆里程 */
    private String mileage;

    /** 事故发生时车辆状态 1 运行2 熄火3 充电*/
    private String vehState;

    /** 生产日期 */
    private String factoryDate;

    /** 销售日期 */
    private String saleDate;

    /** 故障情况 */
    private String errorMessage;

    /** 事故表征 */
    private String troToken;

    /** 事故影响 */
    private String troEffect;

    /** 事故原因初判 */
    private String troReason;

    /** 现场照片（一） */
    private String photo1;

    /** 现场照片（二） */
    private String photo2;

    /** 现场照片（三） */
    private String photo3;

    /** 现场照片（四） */
    private String photo4;

}
