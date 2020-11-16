package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bitnei.cloud.sys.domain.InstructManagement;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： InstructManagement新增模型<br>
* 描述： InstructManagement新增模型<br>
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
* <td>2019-03-11 15:53:11</td>
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
@Data
@ApiModel(value = "InstructManagementModel", description = "控制命令管理Model")
public class InstructManagementModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "控制命令名称")
    @NotEmpty(message = "控制命令名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "控制命令名称")
    @Length(min = 2, max = 15, message = "命令名称长度为2-15个字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String name;

    @ColumnHeader(title = "控制命令种类ID")
    @NotEmpty(message = "控制命令种类ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "控制命令种类ID")
    private String instructCategoryId;

    @ColumnHeader(title = "参数数据")
    @NotEmpty(message = "参数数据不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "参数数据")
    @Length(min = 1, max = 4, message = "参数数据长度为1-4个字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String paramData;

    @ColumnHeader(title = "操作密码")
    @ApiModelProperty(value = "操作密码")
    private String passwd;

    @ColumnHeader(title = "控制命令状态：0 禁用，1 启用。")
    @NotNull(message = "控制命令状态：0 禁用，1 启用。不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "控制命令状态：0 禁用，1 启用。")
    private Integer status;

    @DictName(code = "INSTRUCT_MANAGEMENT_STATUS", joinField = "status")
    private String statusName;

    @ColumnHeader(title = "控制命令描述")
    @ApiModelProperty(value = "控制命令描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ColumnHeader(title = "创建人id")
    @ApiModelProperty(value = "创建人id")
    private String createById;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    //辅助字段
    @ApiModelProperty(value = "适用车型id列表字符串，','号分割")
    private String vehModelIdStrs;

    @ApiModelProperty(value = "适用车型id列表")
    private String[] vehModelIds;

    @ApiModelProperty(value = "适用车型名称列表，','号分割")
    private String vehModelNameStrs;

    @ApiModelProperty(value = "车辆型号列表查询条件（选择全部车辆型号数据时使用，使用查询条件时vehModelIds要保持为空）")
    private PagerInfo pagerInfo;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static InstructManagementModel fromEntry(InstructManagement entry){
        InstructManagementModel m = new InstructManagementModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
