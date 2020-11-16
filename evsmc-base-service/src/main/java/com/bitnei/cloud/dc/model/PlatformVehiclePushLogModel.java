package com.bitnei.cloud.dc.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.dc.domain.PlatformVehiclePushLog;
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
* 功能： PlatformVehiclePushLog新增模型<br>
* 描述： PlatformVehiclePushLog新增模型<br>
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
* <td>2019-02-19 14:19:06</td>
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
@ApiModel(value = "PlatformVehiclePushLogModel", description = "静态数据推送日志Model")
public class PlatformVehiclePushLogModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "车辆ID")
    @NotEmpty(message = "车辆ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆ID")
    private String vehicleId;

    @ColumnHeader(title = "推送平台ID")
    @NotEmpty(message = "推送平台ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "推送平台ID")
    private String forwardPlatformId;

    @ColumnHeader(title = "推送时间")
    @NotEmpty(message = "推送时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "推送时间")
    private String reqTime;

    @ColumnHeader(title = "响应时间")
    @NotEmpty(message = "响应时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应时间")
    private String rspTime;

    @ColumnHeader(title = "推送状态")
    @NotNull(message = "推送状态不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "推送状态")
    private Integer pushStatus;

    @ColumnHeader(title = "错误原因")
    @NotEmpty(message = "错误原因不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "错误原因")
    private String errorMessage;

    @ColumnHeader(title = "人工确认账号")
    @NotEmpty(message = "人工确认账号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "人工确认账号")
    private String confirmUser;

    @ColumnHeader(title = "转发请求内容")
    @NotEmpty(message = "转发请求内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "转发请求内容")
    private String reqBody;

    @ColumnHeader(title = "解密请求内容")
    @NotEmpty(message = "解密请求内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "解密请求内容")
    private String decryptReqBody;

    @ColumnHeader(title = "响应原始内容")
    @NotEmpty(message = "响应原始内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "响应原始内容")
    private String rspBody;

    @ColumnHeader(title = "解密响应内容")
    @NotEmpty(message = "解密响应内容不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "解密响应内容")
    private String decryptRspBody;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /** 静态数据推送状态**/
    @DictName(code = "PUSH_STATUS",joinField = "pushStatus")
    private String pushStatusName;


    /** 车牌号**/
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    /** VIN**/
    @ApiModelProperty(value = "vin")
    private String vin;

    /** 转发平台名称**/
    @ApiModelProperty(value = "转发平台名称")
    private String forwardPlatformName;

    /** 静态数据推送平台**/
    @ApiModelProperty(value = "静态数据推送平台")
    private Integer staticForwardPlatform;

    /** 静态数据推送名称显示**/
    @DictName(code = "PUSH_PLATFORM_TYPE", joinField = "staticForwardPlatform")
    @ApiModelProperty(value = "静态数据推送名称")
    private String staticForwardPlatformName;

    @ApiModelProperty(value = "备注")
    private String remark;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static PlatformVehiclePushLogModel fromEntry(PlatformVehiclePushLog entry){
        PlatformVehiclePushLogModel m = new PlatformVehiclePushLogModel();
        BeanUtils.copyProperties(entry, m);
        m.setLicensePlate(entry.get("licensePlate") == null ? null : entry.get("licensePlate").toString());
        m.setVin(entry.get("vin") == null ? null : entry.get("vin").toString());
        m.setForwardPlatformName(entry.get("forwardPlatformName") == null ? null : entry.get("forwardPlatformName").toString());
        m.setStaticForwardPlatform(entry.get("staticForwardPlatform") == null ? null : Integer.parseInt(entry.get("staticForwardPlatform").toString()));
        return m;
    }

}
