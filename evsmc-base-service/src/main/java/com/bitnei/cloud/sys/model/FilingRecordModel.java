package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.FilingRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： FilingRecord新增模型<br>
* 描述： FilingRecord新增模型<br>
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
* <td>2019-07-24 16:20:42</td>
* <td>joinly</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "FilingRecordModel", description = "国六防篡改备案上报记录Model")
public class FilingRecordModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;


    @ApiModelProperty(value = "记录类型(1:发动机 2:芯片 3:终端 4:车型 5:车辆)")
    private Integer fromType;

    @ColumnHeader(title = "类型对应id")
    @ApiModelProperty(value = "类型对应id")
    private String fromId;

    @ColumnHeader(title = "状态(0:失败 1:成功)")
    @ApiModelProperty(value = "状态(0:失败 1:成功)")
    private Integer fromStatus;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


   /**
     * 将实体转为前台model
     * @param entry {@link FilingRecord}
     * @return FilingRecordModel
     */
    public static FilingRecordModel fromEntry(FilingRecord entry){
        if(entry == null) {
            return null;
        }
        FilingRecordModel m = new FilingRecordModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
