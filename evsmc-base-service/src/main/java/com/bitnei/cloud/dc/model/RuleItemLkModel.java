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

import com.bitnei.cloud.dc.domain.RuleItemLk;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RuleItemLk新增模型<br>
* 描述： RuleItemLk新增模型<br>
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
* <td>2019-01-31 15:29:20</td>
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
@ApiModel(value = "RuleItemLkModel", description = "协议数据项中间表Model")
public class RuleItemLkModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "协议ID")
    @NotEmpty(message = "协议ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "协议ID")
    private String ruleId;

    @ColumnHeader(title = "数据项ID")
    @NotEmpty(message = "数据项ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项ID")
    private String itemId;


    @ApiModelProperty(value = "新增数据项ids")
    private String addItemIds;

    @ApiModelProperty(value = "是否通同步全部协议数据项")
    private Integer syncDataItem;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static RuleItemLkModel fromEntry(RuleItemLk entry){
        RuleItemLkModel m = new RuleItemLkModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
