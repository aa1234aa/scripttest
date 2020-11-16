package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ParameterRule实体<br>
* 描述： ParameterRule实体<br>
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
* <td>2019-02-27 16:35:01</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterRule extends TailBean {

    /** 主键 **/
    private String id;
    /** 规则名称 **/
    private String name;
    /** 适用车辆型号, all 为全部 **/
    private String vehModelId;
    /** 所属零部件 **/
    private String subordinatePartsId;
    /** 是否产生报警 1：是；0：否；默认：是 **/
    private Integer produceAlarm;
    /** 0：无、1：1级、2：2级、3：3级 **/
    private Integer alarmLevel;
    /** 响应方式  0：无、1：系统弹窗、2：系统弹窗+声音提醒、3：APP弹窗提醒、4：短信通知 **/
    private String responseMode;
    /** 级别描述 **/
    private String levelDescribe;
    /** 分组名称 **/
    private String groupName;
    /** 阈值 **/
    private Integer threshold;
    /**  启用状态 是否有效 1=启用, 0=禁用 **/
    private Integer enabledStatus;
    /** 公式 **/
    private String formula;
    /** 公式中文显示字段 **/
    private String formulaDisplay;
    /** 公式代码,方便前端处理 **/
    private String formulaCode;
    /** 公式中文, 方便前端处理 **/
    private String formulaChinese;
    /** 参数异常描述 **/
    private String dataConstDescribe;
    /** 解决方案 **/
    private String solutionDescribe;
    /** 协议类型 **/
    private String dcRuleTypeId;
    /**
     * 删除状态  1：是；0：否
     */
    private Integer deleteStatus;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 修改时间 **/
    private String updateTime;
    /** 修改人 **/
    private String updateBy;

    /**
     * 是否预设通用报警规则: 1=是, 0=否
     */
    private Integer presetRule;

    /**
     * 规则类型 0 参数规则， 1车型同步过来的规则
     */
    private int type;
    /**
     * 开始时间阈值(秒)
     **/
    private Integer beginThreshold;
    /**
     * 结束时间阈值(秒)
     **/
    private Integer endThreshold;
    /**
     * 开始故障值连续帧数(帧)
     **/
    private Integer beginCountThreshold;
    /**
     * 结束正常值连续帧数(帧)
     **/
    private Integer endCountThreshold;
    /**
     * 是否启用持续时间
     **/
    private Integer enableTimeThreshold;
    /**
     * 是否启用持续帧数
     **/
    private Integer enableCountThreshold;
}
