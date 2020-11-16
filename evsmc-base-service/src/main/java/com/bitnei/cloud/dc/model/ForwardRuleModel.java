package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.dc.domain.ForwardRule;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ForwardRule新增模型<br>
* 描述： ForwardRule新增模型<br>
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
* <td>2019-02-20 10:32:15</td>
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
@ApiModel(value = "ForwardRuleModel", description = "转发规则Model")
public class ForwardRuleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "规则名称")
    @NotBlank(message = "规则名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,45}|$", message = "规则名称长度2-45个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "启用状态")
    @NotNull(message = "启用状态不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "启用状态")
    private Integer status;

    @ColumnHeader(title = "备注")
    @Pattern(regexp = "^[\\s\\S]{0,100}|$", message = "备注长度为0~100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ColumnHeader(title = "规则类型")
    @NotNull(message = "规则类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "规则类型")
    private Integer ruleType;

    @ApiModelProperty(value = "规则明细")
    private List<ForwardRuleItemModel> ruleItemList;

    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;

    /** 启用状态名称显示**/
    @DictName(code = "ENABLED_STATUS", joinField = "status")
    @ApiModelProperty(value = "启用状态名称")
    private String statusDisplay;

    /** 规则类型名称显示**/
    @DictName(code = "RULE_TYPE", joinField = "ruleType")
    @ApiModelProperty(value = "规则类型名称")
    private String ruleTypeDisplay;

    @ApiModelProperty(value = "添加车辆Ids")
    private String addVehIds;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;
    @ApiModelProperty(value = "VIN")
    private String vin;

    @ApiModelProperty(value = "车辆厂商")
    private String manuUnitId;

    @ApiModelProperty(value = "运营单位")
    private String operUnitId;
    @ApiModelProperty(value = "车辆型号")
    private String vehModelId;
    @ApiModelProperty(value = "车辆型号名称")
    private String vehModelName;

    @ApiModelProperty(value = "车辆种类")
    private String vehTypeId;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static ForwardRuleModel fromEntry(ForwardRule entry){
        ForwardRuleModel m = new ForwardRuleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
