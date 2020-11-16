package com.bitnei.cloud.sys.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 给大数据据那边提供的车辆列表格式
 *
 * @author xuzhijie
 */
@Getter
@Setter
public class VehicleInfoModel extends BaseModel {

    /**
     * 主键
     */
    private String id;

    /**
     * 车架号
     */
    private String vin;

    /**
     * uuid
     */
    private String vid;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 内部编号
     */
    private String interNo;

    /**
     * 车型名称
     */
    private String vehModelName;

    /**
     * 车型ID
     */
    private String vehModelId;

    /**
     * 上牌城市
     */
    private List<String> operLicenseCityName;
    private List<String> operLicenseCityCode;
    private String operLicenseCityId;
    private List<String> operLicenseCityPath;

    /**
     * 运营区域
     */
    private List<String> operAreaName;
    private List<String> operAreaCode;
    private String operAreaId;
    private List<String> operAreaPath;

    /**
     * 车辆用途名称
     */
    @DictName(code = "VEH_USE_FOR", joinField = "operUseFor")
    private String operUseForName;
    private String operUseFor;

    /**
     * 车辆公告ID
     */
    private String vehNoticeId;

    /**
     * 车辆公告
     */
    private String vehNoticeName;

    /**
     * 车辆厂商ID
     */
    private String vehUnitId;

    /**
     * 车辆厂商名称
     */
    @LinkName(table = "sys_unit", joinField = "vehUnitId")
    private String vehUnitName;

    /**
     * 运营单位ID
     */
    private String operUnitId;

    /**
     * 运营单位名称
     */
    @LinkName(table = "sys_unit", joinField = "operUnitId")
    private String operUnitName;

    /**
     * 车辆录入时间
     */
    private String createTime;

    /**
     * 车辆最后修改时间
     */
    private String updateTime;

    /**
     * 权限编码
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private String whoCanSeeMe;

    /**
     * 权限编码
     */
    @JsonProperty(value = "whoCanSeeMe")
    @JSONField(name = "whoCanSeeMe")
    private String[] whoCanSeeMes;

}
