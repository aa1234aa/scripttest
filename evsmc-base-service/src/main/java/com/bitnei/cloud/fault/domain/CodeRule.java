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
* 功能： CodeRule实体<br>
* 描述： CodeRule实体<br>
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
* <td>2019-02-25 16:55:47</td>
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
public class CodeRule extends TailBean {

    /** 主键 **/
    private String id;
    /** fault_code_type表的id **/
    private String faultCodeTypeId;
    /** 故障码规则名称 **/
    private String faultName;
    /** 解析方式, 1=字节， 2＝bit **/
    private Integer analyzeType;
    /** 车辆公告型号id, 以, 的形式组成的串 **/
    private String vehModelId;
    /** 正常码（无故障状态故障码） **/
    private String normalCode;
    /**  启用状态 是否有效 1=启用, 2=禁用 **/
    private Integer enabledStatus;
    /** 起始位/起始字节 **/
    private Integer startPoint;
    /** 故障码长度 **/
    private Integer exceptionCodeLength;
    /** 故障描述 **/
    private String faultDescribe;
    /** 解决方案描述 **/
    private String solutionDescribe;
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
    /** 开始时间阈值(秒) **/
    private Integer beginThreshold;
    /** 结束时间阈值(秒) **/
    private Integer endThreshold;
    /** 开始故障值连续帧数(帧) **/
    private Integer beginCountThreshold;
    /** 结束正常值连续帧数(帧) **/
    private Integer endCountThreshold;
    /** 所属零部件 **/
    private String subordinatePartsId;
    /** 是否启用持续时间 **/
    private Integer enableTimeThreshold;
    /** 是否启用持续帧数 **/
    private Integer enableCountThreshold;
}
