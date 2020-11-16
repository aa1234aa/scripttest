package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 车辆运营使用Model
 *
 * @author zxz
 * @date 2019年3月20日 17:48:08
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleOperModel", description = "车辆运营使用列表Model")
public class VehicleOperModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ColumnHeader(title = "VIN码", example = "LA9G3MBD8JSWXB075", desc = "VIN码")
    @NotEmpty(message = "VIN码不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "VIN码")
    private String vin;

    @ColumnHeader(title = "车牌号", example = "浙A05449D", desc = "车牌号长度7-8位个中英文、数字")
    @NotEmpty(message = "车牌号不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{7,8}+$", message = "车牌号长度7-8位中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @NotNull(message = "车牌类型不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车牌类型")
    private Integer licenseType;

    @NotEmpty(message = "车牌类型不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "车牌类型", example = "新能源汽车", desc = "车牌类型选项:新能源汽车 小型汽车 大型汽车 挂车 使馆汽车 领馆汽车 港澳入出境车 教练汽车 警用汽车 军用汽车 武警汽车 临时号牌 临时入境汽车 拖拉机")
    @ApiModelProperty(value = "车牌类型名称")
    @DictName(code = "LICENSE_TYPE", joinField = "licenseType")
    private String licenseName;
    /**
     * 车牌颜色
     **/
    @ApiModelProperty(value = "车牌颜色")
    private Integer licensePlateColor;
    @ColumnHeader(title = "车牌颜色", example = "白色", notNull = false, desc = "车牌颜色:蓝色，绿色，黄绿，白色，黑色，黄色")
    @ApiModelProperty(value = "车牌颜色名称")
    @DictName(code = "LICENSE_COLOR", joinField = "licensePlateColor")
    private String licenseColorName;

    /** 上牌时间 **/
    @ColumnHeader(title = "上牌时间", example = "2011-07-22", notNull = true, desc = "上牌时间格式为(yyyy-MM-dd)")
    @NotEmpty(message = "上牌时间(yyyy-MM-dd)不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "上牌时间格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "上牌时间")
    private String operLicenseTime;

    @NotEmpty(message = "上牌城市id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @NotEmpty(message = "上牌城市不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "上牌城市", example = "杭州市", desc = "上牌城市")
    @ApiModelProperty(value = "上牌城市名称")
    @LinkName(table = "sys_area", joinField = "operLicenseCityId")
    private String operLicenseCityName;

    @NotNull(message = "车辆用途不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆用途")
    private Integer operUseFor;

    @NotEmpty(message = "车辆用途不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "车辆用途", example = "公交客车", desc = "车辆用途选项:公交客车 通勤客车 旅游客车 公路客车 公务乘用车 出租乘用车 租赁乘用车 私人乘用车 邮政特种车 物流特种车 环卫特种车")
    @ApiModelProperty(value = "车辆用途名称")
    @DictName(code = "VEH_USE_FOR", joinField = "operUseFor")
    private String operUseForName;

    @NotNull(message = "运营性质不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车辆运营性质")
    private Integer operUseType;

    @NotEmpty(message = "运营性质不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "运营性质", example = "单位运营", desc = "运营性质:单位运营 个人使用")
    @ApiModelProperty(value = "运营性质名称")
    @DictName(code = "VEH_USE_DICT", joinField = "operUseType")
    private String operUserTypeName;

    @ApiModelProperty(value = "运营个人车主id")
    private String operVehOwnerId;

//    @ColumnHeader(title = "个人车主", notNull = false, example = "周星星", desc = "个人车主")
    @ApiModelProperty(value = "运营车主名称")
    private String operOwnerName;

    @ApiModelProperty(value = "运营单位id")
    private String operUnitId;

//    @ColumnHeader(title = "运营单位", notNull = false, example = "杭州市公共交通集团有限公司", desc = "运营单位")
    @ApiModelProperty(value = "运营单位名称")
    private String operUnitName;

    /** 运营单位名称或运营车主名称 */
    @ColumnHeader(title = "运营单位/个人车主", notNull = true, example = "杭州市公共交通集团有限公司", desc = "运营性质为运营单位时填写运营单位名称，为个人使用时填写个人车主名称")
    @NotEmpty(message = "运营单位/个人车主不能为空", groups = { GroupExcelImport.class })
    @ApiModelProperty(value = "运营单位名称或运营车主名称")
    private String operUnitOrOwnerName;

    @ColumnHeader(title = "运营内部编号", notNull = false, example = "ZXZY546M61", desc = "运营内部编号长度2-20个字符")
    @Pattern(regexp = "^.{2,20}|$", message = "运营内部编号长度2-20个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "运营内部编号")
    private String operInterNo;

    @NotEmpty(message = "运营区域不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "运营区域")
    private String operAreaId;

    @NotEmpty(message = "运营区域不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "运营区域", example = "杭州市", desc = "运营区域")
    @ApiModelProperty(value = "运营地区名称")
    private String operAreaName;

    @ColumnHeader(title = "投运时间", example = "2019-04-11", notNull = false, desc = "投运时间格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "投运时间格式为(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "投运时间(yyyy-MM-dd)")
    private String operTime;

    @ApiModelProperty(value = "售后负责人 ")
    private String operSupportOwnerId;

    @ColumnHeader(title = "售后负责人", example = "汪东城", notNull = false, desc = "售后负责人")
    @ApiModelProperty(value = "售后负责人名称")
    private String operSupportOwnerName;

    @ApiModelProperty(value = "存放城市")
    private String operSaveCityId;

    @ColumnHeader(title = "存放城市", notNull = false, example = "杭州市", desc = "存放城市")
    @ApiModelProperty(value = "存放城市名称")
    private String operSaveCityName;

    @ColumnHeader(title = "存放地址", notNull = false, example = "杭州市主城区", desc = "存放地址长度最大50个字符")
    @ApiModelProperty(value = "存放地址")
    @Pattern(regexp = ".{0,50}", message = "存放地址长度最大50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String operSaveAddress;

    @ApiModelProperty(value = "充电桩城市id")
    private String operChgCityId;

    @ColumnHeader(title = "对应充电桩城市", notNull = false, example = "杭州市", desc = "对应充电桩城市")
    @ApiModelProperty(value = "充电桩城市名称")
    private String operChgCityName;

    @ColumnHeader(title = "对应充电桩地址", notNull = false, example = "杭州市主城区", desc = "充电桩地址长度最大50个字符")
    @ApiModelProperty(value = "充电桩地址")
    @Pattern(regexp = ".{0,50}", message = "充电桩地址长度最大50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String operChgAddress;

    @ColumnHeader(title = "机动车行驶证编号", notNull = false, example = "ZXZV1AP8WB", desc = "机动车行驶证编号长度2-20个字符")
    @ApiModelProperty(value = "机动车行驶证编号")
    @Pattern(regexp = ".{2,20}|", message = "机动车行驶证编号长度2-20个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String sellLicenseNo;

    @ColumnHeader(title = "行驶证注册日期", notNull = false, example = "2019-04-11", desc = "行驶证注册日期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "行驶证注册日期格式为(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "行驶证注册日期")
    private String sellLicenseRegDate;

    @ColumnHeader(title = "行驶证发放日期", notNull = false, example = "2019-04-11", desc = "行驶证发放日期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "行驶证发放日期格式为(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "行驶证发放日期")
    private String sellLicenseGrantDate;

    @ApiModelProperty(value = "保险投保日期")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "保险投保日期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class})
    private String sellSecureDate;

    @ApiModelProperty(value = "首次年检时间")
    private String sellFirstCheckDate;


    @ApiModelProperty(value = "行驶证照片")
    private String sellLicenseImgId;

    @ApiModelProperty(value = "车辆种类名称")
    private String vehTypeName;

    @ApiModelProperty(value = "车辆型号名称")
    private String vehModelName;

    @ApiModelProperty(value = "运营车主手机号")
    private String operTelPhone;

    @ApiModelProperty(value = "运营法人名称")
    private String operContactorName;

    @ApiModelProperty(value = "运营法人手机号")
    private String operContactorPhone;

    @ApiModelProperty(value = "售后负责人手机号")
    private String operSupportOwnerPhone;

    @ApiModelProperty(value = "品牌名称")
    private String vehBrandName;

    @ApiModelProperty(value = "车型系列名称")
    private String vehSeriesName;


    /**
     * 初次登记日期
     **/
    @ColumnHeader(title = "初次登记日期", example = "2015-07-22", notNull = false, desc = "初次登记日期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "初次登记日期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "初次登记日期")
    private String initialRegistrationDate;
    /**
     * 年检状态:1-未年检；2-已年检；3-年检过期 （年检过期状态按照年检时间数据判断。年检时间+1年有效期）
     **/
    @ApiModelProperty(value = "年检状态")
    private Integer annualInspectionStatus;
    @ColumnHeader(title = "年检状态", example = "已年检", notNull = false, desc = "年检状态:1-未年检；2-已年检；3-年检过期 ")
    @ApiModelProperty(value = "年检状态名称")
    @DictName(code = "ANNUAL_INSPECTION_STATUS", joinField = "annualInspectionStatus")
    private String inspectionStatusName;
    /**
     * 年检日期
     **/
    @ColumnHeader(title = "年检日期", example = "2019-07-22", notNull = false, desc = "年检日期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "年检日期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "年检日期")
    private String annualInspectionDate;
    /**
     * 保养有效期
     **/
    @ColumnHeader(title = "保养有效期", example = "2022-07-22", notNull = false, desc = "保养有效期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "保养有效期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "保养有效期")
    private String maintenancePeriod;

    /**
     * 二级维护状态:1-未维护；2-已维护
     **/
    @ApiModelProperty(value = "二级维护状态")
    private Integer secondaryMtcStatus;
    @ColumnHeader(title = "二级维护状态", example = "已维护", notNull = false, desc = "二级维护状态:1-未维护，2-已维护")
    @ApiModelProperty(value = "二级维护状态名称")
    @DictName(code = "SECONDARY_MTC_STATUS", joinField = "secondaryMtcStatus")
    private String mtcStatusName;
    /**
     * 二级维护时间
     **/
    @ColumnHeader(title = "二级维护时间", example = "2019-07-22", notNull = false, desc = "二级维护时间格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "二级维护时间格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "二级维护时间")
    private String secondaryMtcTime;
    /**
     * 强制报废日期
     **/
    @ColumnHeader(title = "强制报废日期", example = "2021-07-22", notNull = false, desc = "强制报废日期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "强制报废日期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "强制报废日期")
    private String forcedScrapDate;
    /**
     * 年检有效期 依据年检日期+1年显示
     **/
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "年检有效期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "年检有效期")
    private String annualInspectionPeriod;

    /** 动力方式 **/
    @ApiModelProperty(value = "动力方式")
    private String powerMode;

    @DictName(code = "POWER_MODE", joinField = "powerMode")
    @ApiModelProperty(value = "动力方式名称")
    private String powerModeDisplay;

    public String getOperUnitOrOwnerName() {
        String str = "";
        if(StringUtils.isNotBlank(this.operUnitName)){
            str = this.operUnitName;
        }else if(StringUtils.isNotBlank(this.operOwnerName)){
            str = this.operOwnerName;
        }else{
            str = this.operUnitOrOwnerName;
        }
//        if(Vehicle.VEH_USE_DICT_ENUM.UNIT_OPER.getValue().equals(operUseType)) {
//            return this.operUnitName;
//        } else if(Vehicle.VEH_USE_DICT_ENUM.PERSONAGE_OPER.getValue().equals(operUseType)) {
//            return this.operOwnerName;
//        }
        return str;
    }

    /**
     * 运营单位法人手机号或运营车主手机号
     */
    @ApiModelProperty(value = "运营单位法人手机号或运营车主手机号")
    private String operUnitOrOwnerPhone;

    public String getOperUnitOrOwnerPhone() {
        if (Vehicle.VEH_USE_DICT_ENUM.UNIT_OPER.getValue().equals(operUseType)) {
            return this.operContactorPhone;
        } else if (Vehicle.VEH_USE_DICT_ENUM.PERSONAGE_OPER.getValue().equals(operUseType)) {
            return this.operTelPhone;
        }
        return "";
    }

    @ApiModelProperty(value = "保险有效期(辅助字段，投保日期加一年)")
    private String dateOfInsuranceEffective;

    public void setSellSecureDate(String sellSecureDate) {
        this.sellSecureDate = sellSecureDate;

        if (StringUtils.isNotEmpty(sellSecureDate)) {

            //保险有效期为投保日期加一年
            try {
                this.dateOfInsuranceEffective = DateUtil.formatTime(
                        DateUtil.addYear(DateUtil.strToDate(sellSecureDate), 1),
                        DateUtil.DAY_FORMAT);
            } catch (Exception e) {
            }

        }
    }

    /**
     * 处理年检有效期
     * @param annualInspectionDate
     */
    public void setAnnualInspectionDate(String annualInspectionDate) {
        this.annualInspectionDate = annualInspectionDate;

        if (StringUtils.isNotEmpty(annualInspectionDate)) {

            //保险有效期为投保日期加一年
            try {
                this.annualInspectionPeriod = DateUtil.formatTime(
                        DateUtil.addYear(DateUtil.strToDate(annualInspectionDate), 1),
                        DateUtil.DAY_FORMAT);
            } catch (Exception e) {
            }

        }
    }

    /**
     * 将实体转为前台model
     *
     * @param entry 实体
     * @return 车辆运营使用Model
     */
    public static VehicleOperModel fromEntry(Vehicle entry) {
        VehicleOperModel m = new VehicleOperModel();
        BeanUtils.copyProperties(entry, m);
        m.setOperUnitOrOwnerName(m.getOperUnitOrOwnerName());
        m.setOperUnitOrOwnerPhone(m.getOperUnitOrOwnerPhone());
        return m;
    }


}
