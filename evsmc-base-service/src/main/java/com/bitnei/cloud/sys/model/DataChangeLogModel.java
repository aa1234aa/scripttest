package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.DataChangeLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataChangeLog新增模型<br>
* 描述： DataChangeLog新增模型<br>
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
* <td>2018-12-04 15:43:28</td>
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
@Setter
@Getter
@ApiModel(value = "DataChangeLogModel", description = "数据变更日志Model")
public class DataChangeLogModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "模块名")
    @NotEmpty(message = "模块名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "模块名")
    private String moduleName;

    @ColumnHeader(title = "对象")
    @NotEmpty(message = "对象不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "对象")
    private String instName;

    @ColumnHeader(title = "类型 0：新增 1:变更 2：删除")
    @NotNull(message = "类型 0：新增 1:变更 2：删除不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "类型 0：新增 1:变更 2：删除")
    private Integer type;

    @ColumnHeader(title = "操作人")
    @NotEmpty(message = "操作人不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "操作人")
    private String operator;

    @ColumnHeader(title = "操作时间")
    @NotEmpty(message = "操作时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "操作时间")
    private String operTime;

    @ColumnHeader(title = "变更原因")
    @NotEmpty(message = "变更原因不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "变更原因")
    private String changeReason;

    @ColumnHeader(title = "变更内容")
    @NotEmpty(message = "变更内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "变更内容")
    private String changeContent;

    @ColumnHeader(title = "原始日志")
    @NotEmpty(message = "原始日志不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "原始日志")
    private String orginContent;

    /** 类型名称显示**/
    @DictName(code = "DATA_CHANGE_TYPE",joinField = "type")
    private String typeDisplay;


   /**
     * 将实体转为前台model
     * @param entry DataChangeLog
     * @return DataChangeLogModel
     */
    public static DataChangeLogModel fromEntry(DataChangeLog entry){
        DataChangeLogModel m = new DataChangeLogModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
