package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.dc.domain.DataItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItem新增模型<br>
* 描述： DataItem新增模型<br>
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
* <td>2019-01-30 17:28:53</td>
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
@ApiModel(value = "DataItemTempModel", description = "数据项Model")
public class DataItemTempModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "名称")
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数据项组ID")
    private String groupId;

    @ApiModelProperty(value = "数据项IDs")
    private String itemIds;

    @ApiModelProperty(value = "协议类型ID")
    private String ruleTypeId;

    @ApiModelProperty(value = "重复数据项名称")
    private String repeatItemName;

    @ApiModelProperty(value = "路径名称")
    private String pathName;

    @ApiModelProperty(value = "编号")
    private String seqNo;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DataItemTempModel fromEntry(DataItem entry){
        DataItemTempModel m = new DataItemTempModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
