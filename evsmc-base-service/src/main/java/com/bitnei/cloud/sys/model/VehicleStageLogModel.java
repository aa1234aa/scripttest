package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.VehicleStageLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： VehicleStageLog新增模型<br>
* 描述： VehicleStageLog新增模型<br>
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
* <td>2018-12-21 15:28:08</td>
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
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleStageLogModel", description = "车辆阶段变更记录Model")
public class VehicleStageLogModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "车辆ID")
    private String vehicleId;

    @ApiModelProperty(value = "阶段名称")
    private String stageName;

    @ApiModelProperty(value = "阶段名称值")
    @DictName(code = "VEHICLE_STAGE_TYPE", joinField = "stageName")
    private String stageValue;

    @ApiModelProperty(value = "开始时间")
    private String createTime;

    @ApiModelProperty(value = "操作人")
    private String createBy;

    @ApiModelProperty(value = "操作时间")
    private String updateTime;


   /**
     * 将实体转为前台model
     * @param entry 实体
     * @return model
     */
    public static VehicleStageLogModel fromEntry(VehicleStageLog entry){
        VehicleStageLogModel m = new VehicleStageLogModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
