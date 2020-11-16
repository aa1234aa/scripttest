package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.PlatformRuleLk;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformRuleLk新增模型<br>
* 描述： PlatformRuleLk新增模型<br>
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
* <td>2019-02-21 14:30:57</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PlatformRuleLkModel", description = "转发平台规则中间表Model")
public class PlatformRuleLkModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "平台ID")
    @NotEmpty(message = "平台ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "平台ID")
    private String platformId;

    @ColumnHeader(title = "转发规则ID")
    @NotEmpty(message = "转发规则ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "转发规则ID")
    private String forwardRuleId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "关联规则IDs")
    private String addRuleIds;

    @ApiModelProperty(value = "规则车辆总数")
    private int ruleVehTotalNum;

    @ApiModelProperty(value = "成功车辆数量")
    private int sucVehNum;

    @ApiModelProperty(value = "成功车辆IDs")
    private String sucVehIds;

    @ApiModelProperty(value = "与待转发车辆重复数量")
    private int unFWRepeatVehNum;

    @ApiModelProperty(value = "与待转发车辆重复Ids")
    private String unFWRepeatVehIds;

    @ApiModelProperty(value = "与已转发车辆重复数量")
    private int repeatVehNum;

    @ApiModelProperty(value = "与已转发车辆重复Ids")
    private String repeatVehIds;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "启用状态")
    private Integer ruleStatus;
    /** 启用状态名称显示**/
    @DictName(code = "ENABLED_STATUS", joinField = "ruleStatus")
    @ApiModelProperty(value = "启用状态名称")
    private String statusDisplay;

    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
    @ApiModelProperty(value = "规则备注")
    private String ruleNote;


    @ApiModelProperty(value = "与已转发车辆重复数量")
    private int blankVehNum;

    @ApiModelProperty(value = "与已转发车辆重复Ids")
    private String blankVehIds;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static PlatformRuleLkModel fromEntry(PlatformRuleLk entry){
        PlatformRuleLkModel m = new PlatformRuleLkModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
