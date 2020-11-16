package com.bitnei.cloud.sys.model;

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

import com.bitnei.cloud.sys.domain.InstructManagementModel;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： InstructManagementModel新增模型<br>
* 描述： InstructManagementModel新增模型<br>
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
* <td>2019-03-11 15:48:19</td>
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
@ApiModel(value = "InstructManagementModelModel", description = "控制命令车型关联Model")
public class InstructManagementModelModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "控制命令id")
    @NotEmpty(message = "控制命令id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "控制命令id")
    private String instructManagementId;

    @ColumnHeader(title = "适用车型id")
    @NotEmpty(message = "适用车型id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "适用车型id")
    private String vehModelId;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static InstructManagementModelModel fromEntry(InstructManagementModel entry){
        InstructManagementModelModel m = new InstructManagementModelModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

    public InstructManagementModelModel(String instructManagementId, String vehModelId) {
        this.instructManagementId = instructManagementId;
        this.vehModelId = vehModelId;
    }
}
