package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.veh.domain.DataCheckRule;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataCheckRule新增模型<br>
* 描述： DataCheckRule新增模型<br>
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
* <td>2019-09-16 15:39:53</td>
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
@ApiModel(value = "DataCheckRuleModel", description = "数据质量检测规则Model")
public class DataCheckRuleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "规则名称")
    @NotBlank(message = "规则名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^.{2,10}|$", message = "规则名称长度2-10个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "规则名称")
    private String name;

    @ColumnHeader(title = "车辆型号")
    @NotBlank(message = "车辆型号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆型号")
    private String vehModelId;

    @ColumnHeader(title = "通讯协议")
    @NotBlank(message = "通讯协议不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "通讯协议")
    private String protocolId;

    @ColumnHeader(title = "丢包率阈值")
    @NotNull(message = "丢包率阈值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Digits(integer = 3, fraction = 2, message = "丢包率阈值长度1~3位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "丢包率阈值必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "100", message = "丢包率阈值必须是一个数字，其值必须小于等于100", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "丢包率阈值")
    private Double packetLossRate;

    @ColumnHeader(title = "数据项异常阈值")
    @NotNull(message = "数据项异常阈值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Digits(integer = 3, fraction = 2, message = "数据项异常阈值长度1~3位,保留2位小数", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "数据项异常阈值必须是一个数字，其值必须大于等于0", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "100", message = "数据项异常阈值必须是一个数字，其值必须小于等于100", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项异常阈值")
    private Double dataItemException;

    @ApiModelProperty(value = "新增数据项ids")
    private String addItemIds;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /** 车辆型号名称显示**/
    @ColumnHeader(title = "车辆型号名称")
    @LinkName(table = "sys_veh_model", column = "veh_model_name", joinField = "vehModelId",desc = "")
    @ApiModelProperty(value = "车辆型号名称")
    private String vehModelName;

    /** 通讯协议名称显示**/
    @ColumnHeader(title = "通讯协议名称")
    @LinkName(table = "dc_rule", column = "name", joinField = "protocolId",desc = "")
    @ApiModelProperty(value = "通讯协议名称")
    private String protocolName;

    @ApiModelProperty(value = "数据项数量")
    private Integer itemIdsCount;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DataCheckRuleModel fromEntry(DataCheckRule entry){
        DataCheckRuleModel m = new DataCheckRuleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
