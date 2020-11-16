package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Vehicle模型<br>
* 描述： Vehicle模型<br>
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
* <td>zxz</td>
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
@ApiModel(value = "VehicleModel", description = "车辆列表Model")
public class VehicleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "系统内部编号")
    private String uuid;

    @ColumnHeader(title = "VIN码", example = "LA9G3MBD9JSWXB070", desc = "VIN码")
    @NotEmpty(message = "车架号不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[a-zA-Z0-9]{17}$", message = "车架号长度17个英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "车架号")
    private String vin;

    @ColumnHeader(title = "内部编号", example = "临5D6AOE8", desc = "内部编号")
    @NotEmpty(message = "内部编号不能为空", groups = {GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{4,20}$", message = "内部编号长度4-20个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @NotEmpty(message = "车辆型号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车型id")
    private String vehModelId;

    @ApiModelProperty(value = "动力方式")
    private String powerMode;
    @DictName(code = "POWER_MODE", joinField = "powerMode")
    @ApiModelProperty(value = "动力方式名称")
    private String powerModeName;

    @NotEmpty(message = "车辆型号不能为空", groups = GroupExcelImport.class)
    @ColumnHeader(title = "车辆型号", example = "WXB6121GEV2", desc = "车辆型号")
    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ColumnHeader(title = "车辆颜色", notNull = false, example = "车辆颜色", desc = "车辆颜色,可填项:白色|黑色|红色|橙色|黄色|绿色|青色|蓝色|紫色")
    @ApiModelProperty(value = "颜色名称")
    @DictName(code = "VEHICLE_COLOR", joinField = "color")
    private String colorName;

    @ApiModelProperty(value = "颜色个性化名称")
    private String colorNickName;

    @NotEmpty (message = "终端编号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "终端型号id")
    private String termId;

    @ColumnHeader(title = "终端编号", example = "TERM20190330", desc = "终端编号")
    @NotEmpty(message = "终端编号不能为空", groups = GroupExcelImport.class)
    @ApiModelProperty(value = "终端自定义编号")
    @LinkName(table = "sys_term_model_unit", column = "serial_number", joinField = "termId")
    private String serialNumber;

    @ApiModelProperty(value = "车辆阶段")
    private String stage;

    @DictName(code = "VEHICLE_STAGE_TYPE", joinField = "stage")
    @ApiModelProperty(value = "车辆阶段名称")
    private String stageName;

    @ApiModelProperty(value = "车辆阶段变更日期")
    private String stageChangeDate;

    @ApiModelProperty(value = "生产单位统一信用代码", hidden = true)
    @LinkName(table = "sys_unit", column = "organization_code", joinField = "manuUnitId")
    private String organizationCode;

    @ApiModelProperty(value = "可充电储能装置id列表,多个逗号间隔")
    private String engeryDeviceIds;

    @ColumnHeader(title = "可充电储能装置", notNull = false, example = "CODE0001,CODE0002", desc = "可充电储能装置,多个逗号间隔")
    @ApiModelProperty(value = "可充电储能装置name列表,多个逗号间隔")
    private String engeryDeviceNames;


    @ApiModelProperty(value = "驱动装置id列表,多个逗号间隔")
    private String driveDeviceIds;

    @ColumnHeader(title = "驱动装置编码", notNull = false, example = "CODE0001,CODE0002", desc = "驱动装置编码,多个逗号间隔")
    @ApiModelProperty(value = "驱动装置code列表,多个逗号间隔")
    private String driveDeviceCodes;

    @ApiModelProperty(value = "发电装置id列表,多个逗号间隔")
    private String powerDeviceIds;

    @ColumnHeader(title = "发电装置编码", notNull = false, example = "CODE0001,CODE0002", desc = "发电装置编码,多个逗号间隔")
    @ApiModelProperty(value = "发电装置code列表,多个逗号间隔")
    private String powerDeviceCodes;

    @ApiModelProperty(value = "制造工厂id")
    private String manuUnitId;

    @ColumnHeader(title = "制造工厂", notNull = false, example = "北京理工新源", desc = "制造工厂")
    @LinkName(table = "sys_unit", joinField = "manuUnitId")
    private String manuUnitName;

    @ColumnHeader(title = "车辆生产批次", notNull = false, example = "201902", desc = "车辆生产批次")
    @ApiModelProperty(value = "车辆生产批次")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,20}|$", message = "车辆生产批次长度2-20个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String produceBatch;

    @ColumnHeader(title = "整车质保期", notNull = false, example = "BZQ003", desc = "整车质保期")
    @ApiModelProperty(value = "整车质保期")
    @Pattern(regexp = "^.{4,20}|$", message = "整车质保期长度4-20个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String qualityYears;

    @ColumnHeader(title = "车辆合格证编号", notNull = false, example = "NO00020190330", desc = "车辆合格证编号")
    @ApiModelProperty(value = "车辆合格证编号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{4,50}|$", message = "车辆合格证编号长度4-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String vehCertificateNumber;

    @ColumnHeader(title = "车辆生产日期", notNull = false, example = "2019-03-30", desc = "车辆生产日期")
    @ApiModelProperty(value = "车辆生产日期")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "生产日期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String produceDate;

    @ColumnHeader(title = "出厂日期", notNull = false, example = "2019-03-30", desc = "出厂日期")
    @ApiModelProperty(value = "车辆出厂日期")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "出厂日期格式不正确(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    private String factoryDate;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车辆种类名称")
    private String vehTypeName;

    @ApiModelProperty(value = "车辆公告名称")
    private String vehNoticeName;

    @ApiModelProperty(value = "终端生产企业")
    private String unitName;

    @ApiModelProperty(value = "终端型号")
    private String termModelName;

    @ApiModelProperty(value = "iccid")
    private String iccid;

    /** 车辆销售 -> 购车领域  */
    @ApiModelProperty(value = "车主信息|购车单位")
    private String ownerName;
    /** 车辆销售 -> 购车领域  */
    @ApiModelProperty(value = "车主电话|单位电话")
    private String telPhone;

    @ApiModelProperty(value = "最后操作人")
    private String updateBy;

    @ApiModelProperty(value = "录入时间")
    private String createTime;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @ApiModelProperty(value = "更新时间", hidden = true)
    private String updateTime;

    @ApiModelProperty(value = "配置名称", hidden = true)
    private String configName;

    @ApiModelProperty(value = "公告批次")
    private String vehNoticeBatch;

    @ApiModelProperty(value = "推荐目录批次")
    private String recommendBatch;

    @ApiModelProperty(value = "规约号")
    private Integer ruleNo;
    @ApiModelProperty(value = "支持的协议")
    private String supportProtocol;

    @ApiModelProperty(value = "协议类型id")
    private String ruleId;

    @ApiModelProperty(value = "车辆阶段记录", hidden = true)
    private List<VehicleStageLogModel> stageLogs;

    @ApiModelProperty(value = "品牌名称")
    private String vehBrandName;

    @ApiModelProperty(value = "车型系列名称")
    private String vehSeriesName;

    @ApiModelProperty(value = "车辆种类id")
    private String vehTypeId;

    @ApiModelProperty(value = "种类性质", hidden = true)
    private Integer attrCls;

    @ApiModelProperty(value = "运营地区名称", hidden = true)
    private String operAreaName;

    @ApiModelProperty(value = "运营单位名称", hidden = true)
    private String operUnitName;

    @ApiModelProperty(value = "上次更新的在线状态", hidden = true)
    private Integer historyOnlineState;

    @ApiModelProperty(value = "故障通知人数")
    private Integer faultNoticesCount;

    @ApiModelProperty(value = "车辆是否上过线")
    private Integer onlined;

    @ApiModelProperty(value = "上牌城市id")
    private String operLicenseCityId;

    @ApiModelProperty(value = "上牌城市名称")
    private String operLicenseCityName;

    @ApiModelProperty(value = "购车车主ID")
    private String sellPriVehOwnerId;

    @ApiModelProperty(value = "协议类型名称")
    private String ruleTypeName;

    /** 车辆型号 - 车辆厂商id **/
    @ApiModelProperty(value = "汽车厂商id")
    private String vehUnitId;

    /** 车辆型号 - 车辆厂商名称 **/
    @ApiModelProperty(value = "汽车厂商名称")
    @LinkName(table = "sys_unit", joinField = "vehUnitId")
    private String vehUnitName;

    @ApiModelProperty(value = "车辆用途")
    private Integer operUseFor;

    @ApiModelProperty(value = "车辆用途名称")
    @DictName(code = "VEH_USE_FOR", joinField = "operUseFor")
    private String operUseForName;

    private String powerModeDisplay;
    private String termUnitName;
    private String firstRegTime;
    private String operSupportOwnerName;

    private String sellCityName;

    /** 终端编号 */
    private String termSerialNumber;

    /** 终端通讯协议名称 */
    private String termRuleName;

    /**
     * 关联的终端协议名称和编码
     */
    private String termRuleTypeName;
    private String termRuleTypeCode;

    //是否是国六 ,1-是 0-否
    private Integer isG6;

    @ApiModelProperty(value = "增加方式")
    @JsonIgnore
    private Integer addSource;

    @ApiModelProperty(value = "增加方式名称")
    @DictName(code = "RESOURCE_ADD_SOURCE", joinField = "addSource")
    private String addSourceName;


    @ApiModelProperty(value = "支持的通讯协议")
    @LinkName(table = "dc_rule", column = "name", joinField = "supportProtocol")
    private String supportProtocolName;

    /** 终端关联的电话号码 **/
    private String msisd;

    /**
     * 将实体转为前台model
     * @param entry {@link Vehicle}
     * @return {@link VehicleModel}
     */
    public static VehicleModel fromEntry(Vehicle entry){
        VehicleModel m = new VehicleModel();
        BeanUtils.copyProperties(entry, m);
        if(Vehicle.SEEL_FOR_FIELD_ENUM.PUBLIC_SPHERE.getValue().equals(entry.getSellForField())) {
            m.setOwnerName(entry.getSellPubUnitName());
            m.setTelPhone(entry.getSellPubUnitTelephone());
        }
        // 处理公告批次与推荐批次, 车辆档案使用
        if(entry.get("vehNoticeYear") != null) {
            StringBuilder noticeBatch = new StringBuilder(entry.get("vehNoticeYear").toString()).append("年 ");
            if(StringUtils.isNotEmpty(m.getVehNoticeBatch())) {
                noticeBatch.append("第").append(m.getVehNoticeBatch()).append("批次");
            }
            m.setVehNoticeBatch(noticeBatch.toString());
        }
        if(entry.get("recommendYear") != null) {
            StringBuilder recommend = new StringBuilder(entry.get("recommendYear").toString()).append("年 ");
            if(StringUtils.isNotEmpty(m.getRecommendBatch())) {
                recommend.append("第").append(m.getRecommendBatch()).append("批次");
            }
            m.setRecommendBatch(recommend.toString());
        }
        return m;
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
