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

import com.bitnei.cloud.sys.domain.AttentionVeh;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： AttentionVeh新增模型<br>
* 描述： AttentionVeh新增模型<br>
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
* <td>2019-03-19 18:45:16</td>
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AttentionVehModel", description = "用户关注车辆LkModel")
public class AttentionVehModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "车辆ID")
    @NotEmpty(message = "车辆ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆ID")
    private String vehId;

    @ColumnHeader(title = "用户ID")
    @NotEmpty(message = "用户ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ColumnHeader(title = "0:车辆，1: 故障, 2: 消息")
    @NotNull(message = "0:车辆，1: 故障, 2: 消息不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "0:车辆，1: 故障, 2: 消息")
    private Integer type;

    @ColumnHeader(title = "关注内容，以json格式存储")
    @NotEmpty(message = "关注内容，以json格式存储不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "关注内容，以json格式存储")
    private String content;

    @ColumnHeader(title = "关注时间")
    @NotEmpty(message = "关注时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "关注时间")
    private String attentionTime;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static AttentionVehModel fromEntry(AttentionVeh entry){
        AttentionVehModel m = new AttentionVehModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
