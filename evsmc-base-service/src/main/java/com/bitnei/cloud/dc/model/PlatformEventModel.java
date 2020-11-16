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

import com.bitnei.cloud.dc.domain.PlatformEvent;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PlatformEvent新增模型<br>
* 描述： PlatformEvent新增模型<br>
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
* <td>2019-01-29 20:37:50</td>
* <td>lijiezhou</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author lijiezhou
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PlatformEventModel", description = "平台事件Model")
public class PlatformEventModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ColumnHeader(title = "类型")
    @NotNull(message = "类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "类型")
    private Integer type;

    @ColumnHeader(title = "时间")
    @NotEmpty(message = "时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "时间")
    private String time;

    @ColumnHeader(title = "平台ID")
    @NotEmpty(message = "平台ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "平台ID")
    private String pid;

    @ColumnHeader(title = "流水号")
    @NotEmpty(message = "流水号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "流水号")
    private String seqid;

    @ColumnHeader(title = "状态")
    @NotNull(message = "状态不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ColumnHeader(title = "描述")
    @NotEmpty(message = "描述不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String description;


    /** 平台事件**/
    @DictName(code = "PLATFORM_EVENT_TYPE",joinField = "type")
    private String typeName;
    /** 事件状态**/
    @DictName(code = "PLATFORM_EVENT_STATUS",joinField = "status")
    private String statusName;

    @ApiModelProperty(value = "平台名称")
    private String name;

    @ApiModelProperty(value = "平台类型")
    private Integer platformType;

    @ApiModelProperty(value = "用户名")
    private String username;

    /** 平台类型**/
    @DictName(code = "PLATFORM_TYPE",joinField = "platformType")
    private String platformTypeName;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static PlatformEventModel fromEntry(PlatformEvent entry){
        PlatformEventModel m = new PlatformEventModel();
        BeanUtils.copyProperties(entry, m);
        m.setName(entry.get("name") == null ? null : entry.get("name").toString());
        m.setPlatformType(entry.get("platformType") == null ? null : Integer.parseInt(entry.get("platformType").toString()));
        m.setUsername(entry.get("username") == null ? null : entry.get("username").toString());
        return m;
    }

}