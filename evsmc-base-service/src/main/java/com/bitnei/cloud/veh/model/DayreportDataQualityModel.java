package com.bitnei.cloud.veh.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DateUtil;
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

import com.bitnei.cloud.veh.domain.DayreportDataQuality;
import org.springframework.beans.BeanUtils;

import java.lang.Integer;
import java.lang.String;
import java.sql.Timestamp;
import java.lang.Double;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DayreportDataQuality新增模型<br>
* 描述： DayreportDataQuality新增模型<br>
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
* <td>2019-09-19 18:48:27</td>
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
@ApiModel(value = "DayreportDataQualityModel", description = "数据质量日报Model")
public class DayreportDataQualityModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ColumnHeader(title = "vid")
    @NotEmpty(message = "vid不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vid")
    private String vid;

    @ColumnHeader(title = "vin")
    @NotEmpty(message = "vin不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vin")
    private String vin;

    @ColumnHeader(title = "统计日期")
    @NotEmpty(message = "统计日期不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "统计日期")
    private Timestamp reportDate;

    @ColumnHeader(title = "车辆当日首次上线时间")
    @NotEmpty(message = "车辆当日首次上线时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆当日首次上线时间")
    private String startTime;

    @ColumnHeader(title = "车辆当日最后通讯时间")
    @NotEmpty(message = "车辆当日最后通讯时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆当日最后通讯时间")
    private String endTime;

    @ColumnHeader(title = "应该上传的报文")
    @NotBlank(message = "应该上传的报文不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "应该上传的报文")
    private Integer shouldUploadNum;

    @ColumnHeader(title = "实际上传的报文总条数")
    @NotBlank(message = "实际上传的报文总条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "实际上传的报文总条数")
    private Integer actualUploadNum;

    @ColumnHeader(title = "异常报文条数")
    @NotBlank(message = "异常报文条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常报文条数")
    private Integer abnormalNum;

    @ColumnHeader(title = "异常报文比例")
    @NotBlank(message = "异常报文比例不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常报文比例")
    private Double anbormalRate;

    @ColumnHeader(title = "实时数据丢包率")
    @NotBlank(message = "实时数据丢包率不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "实时数据丢包率")
    private Double missRate;

    @ColumnHeader(title = "是否存在转发报文,0不存在，1存在")
    @NotBlank(message = "是否存在转发报文,0不存在，1存在不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "是否存在转发报文,0不存在，1存在")
    private Double existForward;

    @ColumnHeader(title = "转发报文总条数")
    @NotBlank(message = "转发报文总条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "转发报文总条数")
    private Integer forwardNum;

    @ColumnHeader(title = "数据转发丢包率")
    @NotBlank(message = "数据转发丢包率不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据转发丢包率")
    private Double missForwardRate;

    @ColumnHeader(title = "数据上传的频率")
    @NotBlank(message = "数据上传的频率不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据上传的频率")
    private Integer frequency;


    @ApiModelProperty(value = "是否存在转发报文(String)")
    private String existForwardToString;
    @ApiModelProperty(value = "是否存在转发报文名称")
    @DictName(code = "EXIST_FORWARD", joinField = "existForwardToString")
    private String existForwardName;
    @ApiModelProperty(value = "统计日期")
    private String reportDateEx;
    @ApiModelProperty(value = "异常报文比例")
    private String anbormalRateEx;
    @ApiModelProperty(value = "实时数据丢包率")
    private String missRateEx;
    @ApiModelProperty(value = "数据转发丢包率")
    private String missForwardRateEx;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DayreportDataQualityModel fromEntry(DayreportDataQuality entry){
        DayreportDataQualityModel m = new DayreportDataQualityModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}