package com.bitnei.cloud.monitor.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.lang.String;
import java.lang.Integer;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： ElectronicFence实体<br>
 * 描述： ElectronicFence实体<br>
 * 授权 : (C) Copyright (c) 2017 <br>
 * 公司 : 北京理工新源信息科技有限公司<br>
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
 * <td>2019-05-17 11:04:12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElectronicFence extends TailBean {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 组编码，时间戳
     */
    private String groupCode;
    /**
     * 围栏名称
     */
    private String name;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 规则状态1、启用 0、禁用
     */
    private Integer ruleStatus;
    /**
     * 是否有效1、启用 0、禁用
     */
    private Integer statusFlag;
    /**
     * 规则类型8、驶离 4、驶入
     */
    private Integer ruleType;
    /**
     * 规则用途1、行驶围栏 2、停车围栏
     */
    private Integer ruleUse;
    /**
     * 周期类型1、单次执行 2、每周循环 3、每天循环
     */
    private Integer periodType;
    /**
     * 平台响应方式：0、无1、系统弹窗2、声音提醒3、APP弹窗提醒4、短信通知
     */
    private String responseMode;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 星期，多个之间用的逗号分隔，周一为1到周日为7
     */
    private String ruleWeek;
    /**
     * 开始启用时间
     */
    private String startTime;
    /**
     * 结束启用时间
     */
    private String endTime;
    /**
     * chart类型1、圆形 2、多边形
     */
    private Integer chartType;
    /**
     * 经纬度范围当地图类型为1圆形时，两个值为半径;圆点; 为2多边形时，每一个;的值为经纬度点
     */
    private String lonlatRange;

}
