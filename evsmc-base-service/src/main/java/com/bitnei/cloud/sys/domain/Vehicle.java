package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Vehicle实体<br>
* 描述： Vehicle实体<br>
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
* <td>2018-12-12 17:40:52</td>
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
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle extends TailBean {

    /** 主键 **/
    private String id;
    /** 车架号 **/
    private String vin;
    /** 系统内部编码 **/
    private String uuid;
    /** 车牌类型 **/
    private Integer licenseType;
    /** 车牌号 **/
    private String licensePlate;
    /** 内部编号 **/
    private String interNo;
    /** 车型id **/
    private String vehModelId;
    /** 颜色 **/
    private String color;
    /** 颜色个性化名称 **/
    private String colorNickName;
    /** 终端编号型号id **/
    private String termId;
    /** 车辆阶段(1:生产 2:入库 3:销售 4:运营 5:报废) **/
    private String stage;
    /** 车辆阶段变更日期 **/
    private String stageChangeDate;
    /** 生产单位id **/
    private String manuUnitId;
    /** 车辆生产批次 **/
    private String produceBatch;
    /** 整车质保期(3年/10万公里) **/
    private String qualityYears;
    /** 车辆合格证编号 **/
    private String vehCertificateNumber;
    /** 车辆生产日期 **/
    private String produceDate;
    /** 车辆出厂日期 **/
    private String factoryDate;
    /** 销售日期 **/
    private String sellDate;
    /** 销售价格(万元) **/
    private Double sellPrice;
    /** 购车领域 **/
    private Integer sellForField;
    /** 私人车主ID **/
    private String sellPriVehOwnerId;
    /** 购车单位ID **/
    private String sellPubUnitId;
    /** 购车城市id **/
    private String sellCityId;
    /** 经销商 ID **/
    private String sell4sUnitId;
    /** 销售人员id **/
    private String sellSellerId;
    /** 保险投保日期 **/
    private String sellSecureDate;
    /** 首次年检时间 **/
    private String sellFirstCheckDate;
    /** 发票号 **/
    private String sellInvoiceNo;
    /** 发票时间 **/
    private String sellInvoiceDate;
    /** 发票图片id **/
    private String sellInvoiceImgId;
    /** 车辆行驶证号 **/
    private String sellLicenseNo;
    /** 行驶证注册日期 **/
    private String sellLicenseRegDate;
    /** 行驶证发放日期 **/
    private String sellLicenseGrantDate;
    /** 行驶证照片 **/
    private String sellLicenseImgId;
    /** 是否已新增销售信息 **/
    private Integer isSelled;
    /** 运营内部编号 **/
    private String operInterNo;
    /** 车辆用途 **/
    private Integer operUseFor;
    /** 车辆运营性质 **/
    private Integer operUseType;
    /** 用车个人id **/
    private String operVehOwnerId;
    /** 用车单位id **/
    private String operUnitId;
    /** 上牌城市id **/
    private String operLicenseCityId;
    /** 运营区域 **/
    private String operAreaId;
    /** 投运时间(yyyy-MM-dd) **/
    private String operTime;
    /** 售后负责人 **/
    private String operSupportOwnerId;
    /** 存放城市 **/
    private String operSaveCityId;
    /** 存放地址 **/
    private String operSaveAddress;
    /** 充电桩城市id **/
    private String operChgCityId;
    /** 充电桩地址 **/
    private String operChgAddress;
    /** 是否已新增运营信息 **/
    private Integer isOpered;
    /** 补贴申报状态 **/
    private Integer subsidyApplyStatus;
    /** 申报次数 **/
    private Integer susidyApplyCount;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;
    /** 是否删除(0:否 1:是) **/
    private Integer isDelete;

    /** 车辆运营使用信息 新增字段 **/
    /** 车牌颜色 **/
    private Integer licensePlateColor;
    /** 二级维护状态:1-未维护；2-已维护 **/
    private Integer secondaryMtcStatus;
    /** 二级维护时间 **/
    private String secondaryMtcTime;
    /** 强制报废日期 **/
    private String forcedScrapDate;
    /** 初次登记日期 **/
    private String initialRegistrationDate;
    /** 年检状态:1-未年检；2-已年检；3-年检过期 **/
    private Integer annualInspectionStatus;
    /** 年检日期 **/
    private String annualInspectionDate;
    /** 年检有效期 **/
    private String annualInspectionPeriod;
    /** 保养有效期 **/
    private String maintenancePeriod;
    /** 上牌时间 **/
    private String operLicenseTime;

    /**  关联字段  ***/

    /** 终端生产企业 */
    private String unitName;
    /** 终端型号名称 */
    private String termModelName;
    /** iccid */
    private String iccid;

    /** 车辆公告编号 **/
    private String vehNoticeName;
    /** 公告批次 **/
    private String vehNoticeBatch;
    /** 推荐目录批次 **/
    private String recommendBatch;
    /** 规约号 **/
    private Integer ruleNo;
    private String ruleId;
    private String supportProtocol;
    /** 协议类型名称 **/
    private String ruleTypeName;
    /** 配置名称 **/
    private String configName;
    /** 电池包数量 **/
    private Integer batteryPackageCount;
    /** 车辆种类名称 **/
    private String vehTypeName;
    /** 种类性质 **/
    private Integer attrCls;
    /** 车辆型号名称 **/
    private String vehModelName;
    /** 购车车主名称 **/
    private String ownerName;
    /** 购车车主地址 **/
    private String ownerAddress;
    /** 购车车主身份证 **/
    private String cardNo;
    /** 购车车主联系人电话 */
    private String telPhone;
    /** 购车单位名称 **/
    private String sellPubUnitName;
    /** 购车单位地址 **/
    private String sellPubUnitAddress;
    /** 购车单位社会统一信用代码 **/
    private String organizationCode;
    /** 购车单位座机号 **/
    private String sellPubUnitTelephone;

    /** 购车城市名称 **/
    private String sellCityName;

    /** 运营车主名称 **/
    private String operOwnerName;
    /** 运营车主手机号 **/
    private String operTelPhone;
    /** 运营单位名称 **/
    private String operUnitName;
    /** 运营法人名称 **/
    private String operContactorName;
    /** 运营法人手机号 **/
    private String operContactorPhone;
    /** 运营地区名称 **/
    private String operAreaName;
    /** 售后负责人名称 **/
    private String operSupportOwnerName;
    /** 售后负责人手机号 **/
    private String operSupportOwnerPhone;
    /** 存放城市名称 **/
    private String operSaveCityName;
    /** 充电桩城市名称 **/
    private String operChgCityName;

    /** 品牌名称 **/
    private String vehBrandName;

    /** 车型系列名称 **/
    private String vehSeriesName;

    /** 车辆种类 **/
    private String vehTypeId;

    /** 上次更新的在线状态 **/
    private Integer historyOnlineState;

    /** 车辆是否上过线 **/
    private Integer onlined;

    /** 上牌城市名称 **/
    private String operLicenseCityName;

    /** 车辆型号-车辆厂商id **/
    private String vehUnitId;

    /** 动力方式 **/
    private String powerMode;

    /** 车辆首次上线时间 **/
    private String firstRegTime;

    /** 终端关联的电话号码 **/
    private String msisd;

    /** 首次上线时间 */
    private String firstTimeOnLine;

    /** 累计行驶里程 */
    private Double lastEndMileage;

    /**
     * 关联的终端协议名称和编码
     */
    private String termRuleTypeName;
    private String termRuleTypeCode;

    /** 终端编号 */
    private String termSerialNumber;

    /** 终端通讯协议名称 */
    private String termRuleName;

    /** 车辆阶段(1:生产 2:入库 3:销售 4:运营 5:报废) **/
    public enum STAGE_TYPE_ENUM {
        /** 生产 **/
        PRODUCTION("1"),
        /** 入库 **/
        STORAGE("2"),
        /** 销售 **/
        SELL("3"),
        /** 运营 **/
        OPER("4"),
        /** 报废 **/
        SCRAP("5");
        private String value;
        STAGE_TYPE_ENUM(String value){
            this.value = value;
        }
        public String getValue() { return value; }
    }

    /** 购车领域 1:私人领域 2:公共领域*/
    public enum SEEL_FOR_FIELD_ENUM {
        /** 私人领域 */
        PRIVATE_SPHERE(1),
        /** 公共领域 */
        PUBLIC_SPHERE(2);
        private Integer value;
        SEEL_FOR_FIELD_ENUM(Integer value) {
            this.value = value;
        }
        public Integer getValue() { return value; }
    }

    /** 运营性质:单位运营 个人使用*/
    public enum VEH_USE_DICT_ENUM {
        /** 单位运营 */
        UNIT_OPER(0),
        /** 个人使用 */
        PERSONAGE_OPER(1);
        private Integer value;
        VEH_USE_DICT_ENUM(Integer value) {
            this.value = value;
        }
        public Integer getValue() { return value; }
    }

    public boolean isG6(){
        if (StringUtils.isBlank(termRuleTypeName)){
            return false;
        }
        return termRuleTypeName.contains("国六")||termRuleTypeCode.contains("17691");
    }

    public boolean isGb(){
        return !isG6();
    }
}
