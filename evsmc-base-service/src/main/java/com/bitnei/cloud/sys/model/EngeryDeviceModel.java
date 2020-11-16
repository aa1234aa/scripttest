package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.util.DataLoader;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.EngeryDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： EngeryDevice新增模型<br>
* 描述： EngeryDevice新增模型<br>
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
* <td>2018-12-06 23:59:45</td>
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
@ApiModel(value = "EngeryDeviceModel", description = "可充电储能装置信息Model")
public class EngeryDeviceModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "可充电储能装置编码", example = "E1234", notNull = true, desc = "可充电储能装置编码")
    @NotBlank(message = "可充电储能装置编码不能为空", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,50}|$", message = "可充电储能装置编码长度2-50个中英文、数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "可充电储能装置编码")
    private String name;

    @NotNull(message = "可充电储能装置类型不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "可充电储能装置类型 1:动力蓄电池 2:超级电容")
    private Integer modelType;
    /** 可充电装置类型类字典名称显示**/
    @ColumnHeader(title = "储能装置类型", example = "动力蓄电池", notNull = true, desc = "储能装置类型 1:动力蓄电池 2:超级电容")
    @NotBlank(message = "储能装置类型不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,10}|$", message = "驱动装置类型长度2-10个中英文、数字", groups = {GroupExcelImport.class})
    @DictName(code = "ENERGY_DEVICE_TYPE", joinField = "modelType")
    @ApiModelProperty(value = "可充电储能装置类型名称")
    private String  modelTypeDisplay;

    /** @ColumnHeader(title = "超级电容型号id")**/
    @ApiModelProperty(value = "超级电容型号id")
    private String capacityModelId;

    /** 超级电容型号id关联表名称显示**/
    @ApiModelProperty(value = "超级电容型号名称")
    @LinkName(table = "sys_super_capacitor_model", column = "name", joinField = "capacityModelId",desc = "")
    private String capacityModelIdDisplay;


    /** @ColumnHeader(title = "动力蓄电池型号id")**/
    @ApiModelProperty(value = "动力蓄电池型号id")
    private String batteryModelId;

    /** 动力蓄电池型号关联表名称显示**/
    @ApiModelProperty(value = "动力蓄电池型号名称")
    @LinkName(table = "sys_battery_device_model", column = "name", joinField = "batteryModelId",desc = "")
    private String batteryModelIdDisplay;

    @ApiModelProperty(value = "可充电储能装置成箱型号ID")
    private String modelId;

    @ColumnHeader(title = "储能装置型号", example = "XY400-1", notNull = true, desc = "储能装置型号，选自动力蓄电池型号或者超级电容型号，所选型号需与储能装置类型一致")
    @NotBlank(message = "储能装置型号不能为空", groups = { GroupExcelImport.class})
    @Pattern(regexp = "^.{2,100}|$", message = "储能装置型号长度2-100个字符", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "可充电储能装置成箱型号名称")
    private String modelName;

    @ColumnHeader(title = "安装位置", example = "前置位置", notNull = false, desc = "安装位置")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{4,10}|$", message = "安装位置长度4-10个中文", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "安装位置")
    private String installPostion;

    @ColumnHeader(title = "储能装置生产日期", example = "2019-04-09", notNull = false, desc = "储能装置生产日期,格式为：yyyy-MM-dd")
    @Pattern(regexp = "^\\d{4}(-|/|.)\\d{1,2}\\1\\d{1,2}|$", message = "储能装置生产日期格式不正确,格式为：yyyy-MM-dd", groups = {GroupExcelImport.class})
    @ApiModelProperty(value = "储能装置生产日期")
    private String produceDate;

    @ColumnHeader(title = "储能装置发票号", example = "I0001", notNull = false, desc = "储能装置发票号")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9()/-]{0,32}|$", message = "储能装置发票号长度为0-32个中英文、数字及特殊字符()/-", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "储能装置发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "可充电储能装置生产企业ID")
    private String prodUnitId;

    @ApiModelProperty(value = "可充电储能装置生产企业名称")
    private String prodUnitName;

    /** 超级电容型号生产企业id关联表名称显示**/
    @ApiModelProperty(value = "超级电容型号生产企业id")
    @LinkName(table = "sys_super_capacitor_model", column = "unit_id", joinField = "capacityModelId",desc = "",level = 1)
    private String capacitorUnitId;
    /** 超级电容型号生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "capacitorUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "超级电容型号生产企业名称")
    private String capacitorUnitDisplay;

    /** 动力蓄电池型号生产企业id关联表名称显示**/
    @ApiModelProperty(value = "动力蓄电池型号生产企业id")
    @LinkName(table = "sys_battery_device_model", column = "unit_id", joinField = "batteryModelId",desc = "",level = 1)
    private String batteryUnitId;
    /** 动力蓄电池型号生产企业名称显示**/
    @LinkName(table = "sys_unit", column = "name", joinField = "batteryUnitId",desc = "", level = 2)
    @ApiModelProperty(value = "动力蓄电池型号生产企业名称")
    private String batteryUnitDisplay;

    /** 动力蓄电池总储电量 **/
    @LinkName(table = "sys_battery_device_model", column = "total_elec_capacity", joinField = "batteryModelId",desc = "")
    private String bmTotalCapacity;

    /** 超级电容总储电量 **/
    @LinkName(table = "sys_super_capacitor_model", column = "total_capacity", joinField = "capacityModelId",desc = "")
    private String scTotalCapacity;

    @ApiModelProperty(value = "总储电量")
    private String totalCapacity;

    public String getCapacityModelIdDisplay() {
        return capacityModelIdDisplay;
    }

    public void setCapacityModelIdDisplay(String capacityModelIdDisplay) {
        this.capacityModelIdDisplay = capacityModelIdDisplay;
    }

    public String getBatteryModelIdDisplay() {
        return batteryModelIdDisplay;
    }

    public void setBatteryModelIdDisplay(String batteryModelIdDisplay) {
        this.batteryModelIdDisplay = batteryModelIdDisplay;
    }

    public String getModelTypeDisplay() {
        return modelTypeDisplay;
    }

    public void setModelTypeDisplay(String modelTypeDisplay) {
        this.modelTypeDisplay = modelTypeDisplay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }
    public String getCapacityModelId() {
        return capacityModelId;
    }

    public void setCapacityModelId(String capacityModelId) {
        this.capacityModelId = capacityModelId;
    }
    public String getBatteryModelId() {
        return batteryModelId;
    }

    public void setBatteryModelId(String batteryModelId) {
        this.batteryModelId = batteryModelId;
    }
    public String getInstallPostion() {
        return installPostion;
    }

    public void setInstallPostion(String installPostion) {
        this.installPostion = installPostion;
    }
    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCapacitorUnitId() {
        return capacitorUnitId;
    }

    public void setCapacitorUnitId(String capacitorUnitId) {
        this.capacitorUnitId = capacitorUnitId;
    }

    public String getCapacitorUnitDisplay() {
        return capacitorUnitDisplay;
    }

    public void setCapacitorUnitDisplay(String capacitorUnitDisplay) {
        this.capacitorUnitDisplay = capacitorUnitDisplay;
    }

    public String getBatteryUnitId() {
        return batteryUnitId;
    }

    public void setBatteryUnitId(String batteryUnitId) {
        this.batteryUnitId = batteryUnitId;
    }

    public String getBatteryUnitDisplay() {
        return batteryUnitDisplay;
    }

    public void setBatteryUnitDisplay(String batteryUnitDisplay) {
        this.batteryUnitDisplay = batteryUnitDisplay;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProdUnitId() {
        return prodUnitId;
    }

    public void setProdUnitId(String prodUnitId) {
        this.prodUnitId = prodUnitId;
    }

    public String getProdUnitName() {
        return prodUnitName;
    }

    public void setProdUnitName(String prodUnitName) {
        this.prodUnitName = prodUnitName;
    }

    public String getBmTotalCapacity() {
        return bmTotalCapacity;
    }

    public void setBmTotalCapacity(String bmTotalCapacity) {
        this.bmTotalCapacity = bmTotalCapacity;
    }

    public String getScTotalCapacity() {
        return scTotalCapacity;
    }

    public void setScTotalCapacity(String scTotalCapacity) {
        this.scTotalCapacity = scTotalCapacity;
    }

    public String getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(String totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static EngeryDeviceModel fromEntry(EngeryDevice entry){
        EngeryDeviceModel m = new EngeryDeviceModel();
        BeanUtils.copyProperties(entry, m);
        DataLoader.loadNames(m);
        if(entry.getModelType()!= null && entry.getModelType() == 1){ //动力蓄电池
            m.setModelId(entry.getBatteryModelId());
            m.setModelName(m.getBatteryModelIdDisplay());
            m.setProdUnitId(m.getBatteryUnitId());
            m.setProdUnitName(m.getBatteryUnitDisplay());
            m.setTotalCapacity(m.getBmTotalCapacity());
        }else if(entry.getModelType()!= null && entry.getModelType() == 2){ //超级电容
            m.setModelId(entry.getCapacityModelId());
            m.setModelName(m.getCapacityModelIdDisplay());
            m.setProdUnitId(m.getCapacitorUnitId());
            m.setProdUnitName(m.getCapacitorUnitDisplay());
            m.setTotalCapacity(m.getScTotalCapacity());
        }
        return m;
    }

}
