package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.UppackageSend;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UppackageSend新增模型<br>
* 描述： UppackageSend新增模型<br>
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
* <td>2019-03-05 14:50:32</td>
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
@ApiModel(value = "UppackageSendModel", description = "远程升级任务Model")
public class UppackageSendModel extends BaseModel {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ColumnHeader(title = "任务名称")
    @NotEmpty(message = "任务名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ColumnHeader(title = "1,强制升级  2、普通升级")
    @ApiModelProperty(value = "1,强制升级  2、普通升级")
    private Integer schemaType = 2;

    @ApiModelProperty(value = "车辆id，','符号拼接")
    private String vehIds;

    @ColumnHeader(title = "任务包id")
    @NotEmpty(message = "任务包id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "任务包id")
    private String uppackageId;

    @ApiModelProperty(value = "任务包密码，新增任务时用于验证")
    private String uppackagePassword;

    @ColumnHeader(title = "文件名称")
    @NotEmpty(message = "文件名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ColumnHeader(title = "任务描述")
    @ApiModelProperty(value = "任务描述")
    private String describes;

    @ColumnHeader(title = "0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务")
    @ApiModelProperty(value = "0未开始，1进行中，2已结束，3已终止，4超时/失败/升级任务未结束，5车辆离线、车辆正在等待执行升级任务")
    private Integer uppackageSendStatus;

    @DictName(code = "UPPACKAGE_SEND_STATUS", joinField = "uppackageSendStatus")
    private String uppackageSendStatusName;

    @ColumnHeader(title = "创建用户id")
    @ApiModelProperty(value = "创建用户id")
    private String createById;

    @ApiModelProperty(value = "创建用户")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ColumnHeader(title = "升级指令类型：1国标；128自定义；99自定义指令")
    @NotNull(message = "升级指令类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "升级指令类型：1国标；128自定义；99自定义指令")
    private Integer protocolType;

    @DictName(code = "PROTOCOL_TYPE", joinField = "protocolType")
    private String protocolTypeName;

    @ColumnHeader(title = "指令缓存时间")
    @NotNull(message = "指令缓存时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "指令缓存时间")
    private Integer instructCacheTime;

    @ColumnHeader(title = "任务记录状态 0:正常 1:已删除")
    @ApiModelProperty(value = "任务记录状态 0:正常 1:已删除")
    private Integer recordStatus = 0;


    //辅助字段
    @ApiModelProperty(value = "车辆列表查询条件（选择全部车辆数据时使用，使用查询条件时vehIds要保持为空）")
    private PagerInfo pagerInfo;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static UppackageSendModel fromEntry(UppackageSend entry){
        UppackageSendModel m = new UppackageSendModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
