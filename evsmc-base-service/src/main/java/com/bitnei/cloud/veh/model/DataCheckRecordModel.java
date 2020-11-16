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

import com.bitnei.cloud.veh.domain.DataCheckRecord;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;
import java.lang.Double;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： DataCheckRecord新增模型<br>
* 描述： DataCheckRecord新增模型<br>
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
* <td>2019-09-17 14:11:42</td>
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
@ApiModel(value = "DataCheckRecordModel", description = "车辆数据检测记录Model")
public class DataCheckRecordModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ColumnHeader(title = "车架号")
    @NotEmpty(message = "车架号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ColumnHeader(title = "检测结果 0:未通过,  1:已通过")
    @NotBlank(message = "检测结果 0:未通过,  1:已通过不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "检测结果 0:未通过,  1:已通过")
    private Integer checkResult;

    @DictName(code = "CHECK_RESULT", joinField = "checkResult")
    @ApiModelProperty(value = "总检测结果")
    private String checkResultDisplay;

    @ColumnHeader(title = "异常原因")
    @NotEmpty(message = "异常原因不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "异常原因")
    private String reason;

    @ColumnHeader(title = "检测时间范围起")
    @NotEmpty(message = "检测时间范围起不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "检测时间范围起")
    private String checkTimeBg;

    @ColumnHeader(title = "检测时间范围止")
    @NotEmpty(message = "检测时间范围止不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "检测时间范围止")
    private String checkTimeEd;

    @ColumnHeader(title = "车辆登入报文数")
    @NotBlank(message = "车辆登入报文数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆登入报文数")
    private Integer loginPacketNum;

    @ColumnHeader(title = "车辆登出报文数")
    @NotBlank(message = "车辆登出报文数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆登出报文数")
    private Integer logoutPacketNum;

    @ColumnHeader(title = "实时上报信息报文数")
    @NotBlank(message = "实时上报信息报文数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "实时上报信息报文数")
    private Integer realStatusPacketNum;

    @ColumnHeader(title = "需上传报文条数")
    @NotBlank(message = "需上传报文条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "需上传报文条数")
    private Integer needUploadNum;

    @ColumnHeader(title = "实际上传报文条数")
    @NotBlank(message = "实际上传报文条数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "实际上传报文条数")
    private Integer realUploadNum;

    @ColumnHeader(title = "丢包率阈值")
    @NotBlank(message = "丢包率阈值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "丢包率阈值")
    private Double packetLossRate;

    @ColumnHeader(title = "实际丢包率")
    @NotBlank(message = "实际丢包率不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "实际丢包率")
    private Double packetRealLossRate;

    @ColumnHeader(title = "丢包率检测结果 0:未通过，1:已通过")
    @NotBlank(message = "丢包率检测结果不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "丢包率检测结果 0:未通过，1:已通过")
    private Integer lossCheckResult;

    @DictName(code = "CHECK_RESULT", joinField = "lossCheckResult")
    @ApiModelProperty(value = "丢包率检测结果")
    private String lossCheckResultDisplay;

    @ColumnHeader(title = "数据项检测报文数")
    @NotBlank(message = "数据项检测报文数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项检测报文数")
    private Integer dataPacketNum;

    @ColumnHeader(title = "数据项检测异常报文数")
    @NotBlank(message = "数据项检测异常报文数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项检测异常报文数")
    private Integer dataExceptionNum;

    @ColumnHeader(title = "数据项异常阈值")
    @NotBlank(message = "数据项异常阈值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项异常阈值")
    private Double dataItemException;

    @ColumnHeader(title = "数据项实际异常比例")
    @NotBlank(message = "数据项实际异常比例不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项实际异常比例")
    private Double dataRealException;

    @ColumnHeader(title = "数据项异常检测结果 0:未通过，1:已通过")
    @NotBlank(message = "数据项异常检测结果不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "数据项异常检测结果 0:未通过，1:已通过")
    private Integer dataExceptionResult;

    @DictName(code = "CHECK_RESULT", joinField = "dataExceptionResult")
    @ApiModelProperty(value = "数据项异常检测结果")
    private String dataExceptionResultDisplay;

    @ColumnHeader(title = "报文类型检测结果 0:未通过，1:已通过")
    @NotBlank(message = "报文类型检测结果不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "报文类型检测结果 0:未通过，1:已通过")
    private Integer packetTypeCheckResult;

    @DictName(code = "CHECK_RESULT", joinField = "packetTypeCheckResult")
    @ApiModelProperty(value = "报文类型检测结果")
    private String packetTypeCheckResultDisplay;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static DataCheckRecordModel fromEntry(DataCheckRecord entry){
        DataCheckRecordModel m = new DataCheckRecordModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
