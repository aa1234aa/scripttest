package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UserBlockResource实体<br>
* 描述： UserBlockResource实体<br>
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
* <td>2019-07-04 14:22:54</td>
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
@Data
public class UserBlockResourceVehicle extends UserBlockResource {

    /** VIN **/
    private String vin;
    /** 车牌号 **/
    private String licensePlate;
    /** 内部编号 **/
    private String interNo;
    /** 制造工厂ID **/
    private String manuUnitId;
    /** 品牌ID **/
    private String brandId;
    /** 系列ID **/
    private String seriesId;
    /** 车型 **/
    private String vehModelName;
    /** 车辆种类 **/
    private String vehTypeName;
    /** 终端编号 **/
    private String termNo;
    /** ICCID **/
    private String iccid;
    /** 车主姓名 **/
    private String ownerName;
    /** 车主姓名 **/
    private String ownerTelPhone;
    /** 公告型号 **/
    private String vehNoticeName;
    /** 车辆阶段 **/
    private String stage;
    /**  运营单位ID **/
    private String operUnitId;
    /** 上牌城市  **/
    private String onPlateCity;
    /** 领域 **/
    private Integer sellForField;

    private String sellPubUnitName;

    private String sellPubUnitTelephone;


}
