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
 * 功能： ParameterManage实体<br>
 * 描述： ParameterManage实体<br>
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
 * <td>2019-03-01 09:17:04</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterManage extends TailBean {

    /**
     * 主键
     **/
    private String id;

    /**
     * 协议类型id
     **/
    private String dcRuleTypeId;

    /**
     * 协议类型名称
     **/
    private String dcRuleTypeName;

    /**
     * 数据项单位
     **/
    private String unit;

    /**
     * 数据项编号
     **/
    private String seqNo;

    /**
     * 协议数据项id 取dc_data_item表id
     **/
    private String dcDataItemId;

    /**
     * 协议数据项名称
     **/
    private String dcDataItemName;

    /**
     * code, 产品预留字段, 正整数, 长度1~4
     **/
    private Integer code;
    /**
     * 备注
     **/
    private String remark;
    /**
     * 创建人
     **/
    private String createBy;
    /**
     * 创建时间
     **/
    private String createTime;
    /**
     * 修改人
     **/
    private String updateBy;
    /**
     * 修改时间
     **/
    private String updateTime;

}
