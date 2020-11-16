package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.TermParamDic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： TermParamDic新增模型<br>
* 描述： TermParamDic新增模型<br>
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
* <td>2019-03-07 15:39:13</td>
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
@ApiModel(value = "TermParamDicModel", description = "终端参数字典Model")
public class TermParamDicModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "名称")
    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @ColumnHeader(title = "编码")
    @NotEmpty(message = "编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "编码")
    private String code;

    @ColumnHeader(title = "设置id")
    @NotEmpty(message = "设置id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "设置id")
    private String setupCode;

    @ColumnHeader(title = "接收id")
    @NotEmpty(message = "接收id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接收id")
    private String receiveCode;

    @ColumnHeader(title = "数据长度")
    @NotNull(message = "数据长度不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据长度")
    private Integer dataSize;

    @ColumnHeader(title = "数据类型")
    @NotEmpty(message = "数据类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ColumnHeader(title = "0： 禁用：1，启用")
    @NotNull(message = "0： 禁用：1，启用不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "0： 禁用：1，启用")
    private Integer state;

    @ColumnHeader(title = "描述")
    @NotEmpty(message = "描述不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String describes;

    @ApiModelProperty(value = "添加人")
    private String createBy;

    @ApiModelProperty(value = "添加时间")
    private String createTime;

    @ColumnHeader(title = "排序")
    @NotNull(message = "排序不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "排序")
    private Integer sequence;

    @ColumnHeader(title = "1,可设置 0，警用")
    @NotNull(message = "1,可设置 0，警用不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "1,可设置 0，警用")
    private Integer isSetup;

    @ColumnHeader(title = "1：国标, 2,自定义")
    @NotNull(message = "1：国标, 2,自定义不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "1：国标, 2,自定义")
    private Integer paramType;

    @ApiModelProperty(value = "下限")
    private Integer lowerLimit;

    @ApiModelProperty(value = "上限")
    private Integer upperLimit;

    @ApiModelProperty(value = "单位")
    private String unit;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static TermParamDicModel fromEntry(TermParamDic entry){
        TermParamDicModel m = new TermParamDicModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
