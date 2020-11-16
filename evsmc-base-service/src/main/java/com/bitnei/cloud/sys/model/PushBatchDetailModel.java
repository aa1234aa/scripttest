package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.sys.domain.PushBatchDetail;
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
* 功能： PushBatchDetail新增模型<br>
* 描述： PushBatchDetail新增模型<br>
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
* <td>2019-02-27 19:37:27</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PushBatchDetailModel", description = "国家平台推送批次详情Model")
public class PushBatchDetailModel extends BaseModel {

    /** 推送中 */
    public static final Integer PUSH_STATUS_ING = 0;
    /** 推送成功 */
    public static final Integer PUSH_STATUS_SUCCESS = 1;
    /** 推送失败 */
    public static final Integer PUSH_STATUS_FAIL = 2;

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @ApiModelProperty(value = "关联id(车辆id|车型id|运营单位id)")
    private String formId;

    @ApiModelProperty(value = "关联名称(车型名称|车辆vin|运营单位)")
    private String formName;

    @ApiModelProperty(value = "转发车辆id")
    private String forwardVehicleId;

    @ApiModelProperty(value = "推送状态(0: 推送中 1:推送成功 2:推送失败)")
    private Integer pushStatus;

    @ApiModelProperty(value = "错误信息")
    private String errorInfo;

    @ApiModelProperty(value = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核信息")
    private String auditMessage;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "批次类型(1:车辆 2:车型 3:运营单位 4:车辆销售)")
    private String batchType;

    @ApiModelProperty(value = "vin")
    private String vin;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static PushBatchDetailModel fromEntry(PushBatchDetail entry){
        PushBatchDetailModel m = new PushBatchDetailModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
