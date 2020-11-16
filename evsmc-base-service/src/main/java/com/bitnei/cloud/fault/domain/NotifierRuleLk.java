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
* 功能： NotifierRuleLk实体<br>
* 描述： NotifierRuleLk实体<br>
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
* <td>2019-07-04 11:22:26</td>
* <td>chenpeng</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author chenpeng
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifierRuleLk extends TailBean {

    /** 主键ID **/
    private String id;
    /** 推送负责人ID **/
    private String notifierId;
    /** 规则ID, all表示全部规则 **/
    private String ruleId;
    /** 规则类型: 1=参数, 2=故障码, 3=围栏 **/
    private Integer ruleType;
    /** 分配人 **/
    private String createBy;
    /** 分配时间 **/
    private String createTime;

    /**
     * 规则名称
     */
    private String ruleName;
}
