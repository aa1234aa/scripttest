package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Vehicle;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * 车辆销售model
 * @author zxz
 * @date 2019年3月20日 17:48:08
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VehicleSellModel", description = "车辆销售列表Model")
public class VehicleSellModel extends BaseModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ColumnHeader(title = "VIN码", example = "LA9G3MBD8JSWXB075", desc = "VIN码")
    @NotEmpty(message = "VIN码不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "车架号")
    private String vin;

    @ColumnHeader(title = "销售时间", example = "2019-04-11", desc = "销售日期格式为(yyyy-MM-dd)")
    @NotEmpty(message = "销售日期不能为空", groups = { GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}$", message = "销售日期格式为(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "销售日期")
    private String sellDate;

    @ColumnHeader(title = "销售价格(万元)", notNull = false, example = "11.5", desc = "销售价格(万元)")
    @ApiModelProperty(value = "销售价格(万元)")
    private Double sellPrice;

    @ApiModelProperty(value = "购车城市id")
    private String sellCityId;

    @ColumnHeader(title = "购车城市", notNull = false, example = "珠海市", desc = "购车城市")
    @ApiModelProperty(value = "购车城市名称")
    private String sellCityName;

    @NotNull(message = "购车领域不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "购车领域")
    private Integer sellForField;

    @NotEmpty(message = "购车领域不能为空", groups = {GroupExcelImport.class})
    @ColumnHeader(title = "购车领域", example = "私人领域", desc = "购车领域选项:私人领域|公共领域")
    @ApiModelProperty(value = "购车领域名称")
    @DictName(code = "SEEL_FOR_FIELD", joinField = "sellForField")
    private String sellForFieldName;

    @ApiModelProperty(value = "购车车主id")
    private String sellPriVehOwnerId;

    @ColumnHeader(title = "个人车主", example = "周星星", desc = "个人车主")
    @ApiModelProperty(value = "购车车主名称")
    private String ownerName;

    @ApiModelProperty(value = "购车单位ID")
    private String sellPubUnitId;

    @ColumnHeader(title = "购车单位", example = "北京理工新源信息科技有限公司", desc = "购车单位")
    @ApiModelProperty(value = "购车单位名称")
    private String sellPubUnitName;

    @ApiModelProperty(value = "经销商ID")
    private String sell4sUnitId;

    @ColumnHeader(title = "经销商", notNull = false, example = "海马新能源汽车有限公司", desc = "经销商")
    @ApiModelProperty(value = "经销商名称")
    @LinkName(table = "sys_unit", joinField = "sell4sUnitId")
    private String  sell4sUnitName;

    @NotEmpty(message = "销售人员不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "销售人员id")
    private String sellSellerId;

    @NotEmpty(message = "销售人员不能为空", groups = { GroupExcelImport.class})
    @ColumnHeader(title = "销售人员", example = "周星星", desc = "销售人员")
    @ApiModelProperty(value = "销售人员名称")
    @LinkName(table = "sys_owner_people", column = "owner_name", joinField = "sellSellerId")
    private String sellSellerName;

    @ApiModelProperty(value = "销售人员联系方式")
    @LinkName(table = "sys_owner_people", column = "tel_phone", joinField = "sellSellerId")
    private String sellSellerPhone;

    @ColumnHeader(title = "保险投保日期", notNull = false, example = "2019-04-11", desc = "保险投保日期格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "保险投保日期格式为(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "保险投保日期")
    private String sellSecureDate;

    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "首次年检时间格式为(yyyy-MM-dd)", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "首次年检时间")
    private String sellFirstCheckDate;

    @ColumnHeader(title = "销售发票编号", notNull = false, example = "FP20190411", desc = "销售发票编号长度0-32个字符")
    @Pattern(regexp = RegexUtil.A_T_0_32, message = "发票号长度0-32个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "发票号")
    private String sellInvoiceNo;

    @ColumnHeader(title = "发票时间", notNull = false, example = "2019-04-11", desc = "发票时间格式为(yyyy-MM-dd)")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "发票时间格式为(yyyy/MM/dd)", groups = {GroupInsert.class, GroupUpdate.class,GroupExcelImport.class})
    @ApiModelProperty(value = "发票时间")
    private String sellInvoiceDate;

    @ApiModelProperty(value = "发票图片id")
    private String sellInvoiceImgId;

    @ApiModelProperty(value = "车辆行驶证号")
    private String sellLicenseNo;

    @ApiModelProperty(value = "行驶证注册日期")
    private String sellLicenseRegDate;

    @ApiModelProperty(value = "行驶证发放日期")
    private String sellLicenseGrantDate;

    @ApiModelProperty(value = "行驶证照片")
    private String sellLicenseImgId;

    @ApiModelProperty(value = "车辆种类名称")
    private String vehTypeName;

    @ApiModelProperty(value = "车辆型号名称")
    private String vehModelName;

    @ApiModelProperty(value = "购车车主地址")
    private String ownerAddress;

    @ApiModelProperty(value = "购车单位地址")
    private String sellPubUnitAddress;


    @ApiModelProperty(value = "品牌名称")
    private String vehBrandName;

    @ApiModelProperty(value = "车型系列名称")
    private String vehSeriesName;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "颜色名称")
    @DictName(code = "VEHICLE_COLOR", joinField = "color")
    private String colorName;

    @ApiModelProperty(value = "车辆用途")
    private Integer operUseFor;

    @ApiModelProperty(value = "车辆用途名称")
    @DictName(code = "VEH_USE_FOR", joinField = "operUseFor")
    private String operUseForName;

    @ApiModelProperty(value = "购车车主身份证")
    private String cardNo;

    @ApiModelProperty(value = "购车单位社会统一信用代码")
    private String organizationCode;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "运营单位")
    private String operOwnerName;

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static VehicleSellModel fromEntry(Vehicle entry){
        VehicleSellModel m = new VehicleSellModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
