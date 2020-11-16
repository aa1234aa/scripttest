package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.VehicleFiling;
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
* 功能： 车辆防篡改备案Model<br>
* 描述： 车辆防篡改备案Model<br>
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
* <td>2019-07-02 11:25:31</td>
* <td>zxz</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author zxz
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleFilingModel", description = "车辆防篡改备案Model")
public class VehicleFilingModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ApiModelProperty(value = "车辆id")
    private String vehicleId;

    @ApiModelProperty(value = "备案状态(0:失败,1:成功,2:未备案)")
    private Integer status;

    @ApiModelProperty(value = "备案状态名称")
    @DictName(joinField = "status", code = "FILING_STATUS")
    private String statusName;

    @ApiModelProperty(value = "状态信息")
    private String statusInfo;

    @ApiModelProperty(value = "国家平台公钥")
    private String publicKey;

    @ApiModelProperty(value = "签名R值")
    private String signr;

    @ApiModelProperty(value = "签名S值")
    private String signs;

    @ApiModelProperty(value = "创建时间|静态数据推送成功时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "VIN")
    private String vin;

    @ApiModelProperty(value = "终端编号")
    private String serialNumber;

    @ApiModelProperty(value = "ICCID")
    private String iccid;

    @ApiModelProperty(value = "加密芯片ID")
    private String identificationId;

   /**
     * 将实体转为前台model
     * @param entry {@link VehicleFiling}
     * @return {@link VehicleFilingModel}
     */
    public static VehicleFilingModel fromEntry(VehicleFiling entry){
        VehicleFilingModel m = new VehicleFilingModel();
        BeanUtils.copyProperties(entry, m);
        m.setVin(ts(entry.get("vin")));
        m.setSerialNumber(ts(entry.get("serialNumber")));
        m.setIccid(ts(entry.get("iccid")));
        m.setIdentificationId(ts(entry.get("identificationId")));
        return m;
    }

    private static String ts(Object obj){
        return obj == null ? null : obj.toString();
    }


}
