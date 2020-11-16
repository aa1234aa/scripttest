package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bitnei.cloud.sys.domain.GroupResourceRule;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： GroupResourceRule新增模型<br>
* 描述： GroupResourceRule新增模型<br>
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
* <td>2019-01-22 16:30:51</td>
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
@ApiModel(value = "GroupResourceRuleModel", description = "数据权限组资源配置Model")
public class GroupResourceRuleModel extends BaseModel {



    @NotEmpty(message = "资源项ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "资源项ID")
    private String resourceItemId;

    @NotNull(message = "操作符(0:等于 1:不等于)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "操作符(0:等于 1:不等于)")
    private Integer op;

    @NotEmpty(message = "值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "值")
    private String val;

    @ApiModelProperty(value = "值描述")
    private String valName;

    @ApiModelProperty(value = "与前一条规则逻辑运算符(0:与 1:或)")
    private Integer preLogicOp;


    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static GroupResourceRuleModel fromEntry(GroupResourceRule entry){
        GroupResourceRuleModel m = new GroupResourceRuleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }




}
