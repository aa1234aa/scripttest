package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.bean.TailBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UpgradeLog新增模型<br>
 * 描述： UpgradeLog新增模型<br>
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
 * <td>2019-03-09 09:56:09</td>
 * <td>hzr</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author hzr
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleUpgradeLogDto", description = "车辆升级日志Dto")
public class VehicleUpgradeLogSumDto extends TailBean {

    @ColumnHeader(title = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "车辆vin")
    @ApiModelProperty(value = "车辆vin")
    private String vin;

    @ColumnHeader(title = "内部编号")
    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ColumnHeader(title = "iccid")
    @ApiModelProperty(value = "iccid")
    private String iccid;

    @ApiModelProperty(value = "聚合任务数据")
    private List<UppackageSendDetailsModel> details;

}
