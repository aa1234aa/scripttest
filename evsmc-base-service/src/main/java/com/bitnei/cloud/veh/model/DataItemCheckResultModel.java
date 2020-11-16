package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.veh.domain.DataItemCheckResult;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataItemCheckResult新增模型<br>
* 描述： DataItemCheckResult新增模型<br>
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
* <td>2019-09-17 18:35:18</td>
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
@ApiModel(value = "DataItemCheckResultModel", description = "车辆数据项检测结果Model")
public class DataItemCheckResultModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ColumnHeader(title = "车架号")
    @NotEmpty(message = "车架号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车架号")
    private String vin;

    @ColumnHeader(title = "车辆数据检测记录id")
    @NotEmpty(message = "车辆数据检测记录id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆数据检测记录id")
    private String checkRecordId;

    @ColumnHeader(title = "数据项编码")
    @NotEmpty(message = "数据项编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项编码")
    private String seqNo;

    @ColumnHeader(title = "数据项名称")
    @NotEmpty(message = "数据项名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项名称")
    private String dataItemName;

    @ColumnHeader(title = "数据项检测报文数")
    @NotBlank(message = "数据项检测报文数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项检测报文数")
    private Integer itemPacketNum;

    @ColumnHeader(title = "数据项检测异常报文数")
    @NotBlank(message = "数据项检测异常报文数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项检测异常报文数")
    private Integer itemExceptionNum;

    @ColumnHeader(title = "数据项异常阈值")
    @NotBlank(message = "数据项异常阈值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项异常阈值")
    private Double dataItemException;

    @ColumnHeader(title = "数据项实际异常比例")
    @NotBlank(message = "数据项实际异常比例不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项实际异常比例")
    private Double itemRealException;

    @ColumnHeader(title = "数据项异常检测结果")
    @NotBlank(message = "数据项异常检测结果不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项异常检测结果")
    private Integer itemExceptionResult;

    @DictName(code = "CHECK_RESULT", joinField = "itemExceptionResult")
    @ApiModelProperty(value = "数据项异常检测结果")
    private String itemExceptionResultDisplay;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DataItemCheckResultModel fromEntry(DataItemCheckResult entry){
        DataItemCheckResultModel m = new DataItemCheckResultModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
