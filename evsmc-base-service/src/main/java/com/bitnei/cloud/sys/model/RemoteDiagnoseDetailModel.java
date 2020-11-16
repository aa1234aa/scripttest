package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.common.SysDefine;
import com.bitnei.cloud.sys.domain.RemoteDiagnoseDetail;
import com.googlecode.aviator.runtime.function.system.SysDateFunction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： RemoteDiagnoseDetail新增模型<br>
 * 描述： RemoteDiagnoseDetail新增模型<br>
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
 * <td>2019-03-25 16:15:07</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "RemoteDiagnoseDetailModel", description = "远程诊断明细Model")
public class RemoteDiagnoseDetailModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "诊断记录id")
    @NotEmpty(message = "诊断记录id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "诊断记录id")
    private String diagnoseId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ColumnHeader(title = "车牌号")
    @NotEmpty(message = "车牌号不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "诊断时间")
    @NotEmpty(message = "诊断时间不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "诊断时间")
    private String uploadTime;

    @ColumnHeader(title = "诊断结果数据")
    @NotEmpty(message = "诊断结果数据不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "诊断结果数据")
    private String result;

    @ColumnHeader(title = "辅助字段：诊断结果数据列表")
    @ApiModelProperty(value = "辅助字段：诊断结果数据列表")
    private List<DiagnoseResultDto> resultList;

    @ColumnHeader(title = "辅助字段：vin")
    @ApiModelProperty(value = "辅助字段：vin")
    private String vin;

    @ColumnHeader(title = "辅助字段：总体诊断结果编码")
    @ApiModelProperty(value = "辅助字段：总体诊断结果编码")
    private String finalResultCode;

    @ColumnHeader(title = "辅助字段：总体诊断结果")
    @ApiModelProperty(value = "辅助字段：总体诊断结果")
    private String finalResult;

    @ColumnHeader(title = "辅助字段：故障数量")
    @ApiModelProperty(value = "辅助字段：故障数量")
    private Integer failCount;

    @ColumnHeader(title = "辅助字段：正常数量")
    @ApiModelProperty(value = "辅助字段：正常数量")
    private Integer normalCount;

    /**
     * 将实体转为前台model
     *
     * @param entry
     * @return
     */
    public static RemoteDiagnoseDetailModel fromEntry(RemoteDiagnoseDetail entry) {
        RemoteDiagnoseDetailModel m = new RemoteDiagnoseDetailModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

    public void setResult(String result) {
        this.result = result;

        //辅助字段补充逻辑
        if (StringUtils.isNotEmpty(this.result)) {
            JSONArray jsonArray = JSONArray.fromObject(this.result);
            this.resultList = Arrays.asList((DiagnoseResultDto[]) JSONArray
                    .toArray(jsonArray, DiagnoseResultDto.class));
        } else {
            this.resultList = new ArrayList<>();
        }

        this.failCount = 0;
        this.normalCount = 0;
        this.finalResultCode = SysDefine.DIAGNBOSE_MODE.NORMAL;
        this.finalResult = "正常";
        for (DiagnoseResultDto diagnoseResultDto: this.resultList) {

            if (diagnoseResultDto.getResult().equals(SysDefine.DIAGNBOSE_MODE.NORMAL)) {
                this.normalCount++;
            } else if (diagnoseResultDto.getResult().equals(SysDefine.DIAGNBOSE_MODE.FAULT)) {
                this.failCount++;
                this.finalResultCode = SysDefine.DIAGNBOSE_MODE.FAULT;
                this.finalResult = "故障";
            } else {
                this.finalResultCode = SysDefine.DIAGNBOSE_MODE.EXCEPTION;
                this.finalResult = "异常";
            }
        }
    }
}
