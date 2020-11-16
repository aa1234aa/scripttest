package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.PagerInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： InstructSendCan新增模型<br>
 * 描述： InstructSendCan新增模型<br>
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
 * <td>2019-03-15 17:08:14</td>
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
@Data
@ApiModel(value = "SendInstructDto", description = "下发远程指令参数")
public class SendInstructDto {

    @ApiModelProperty(value = "车辆id拼接，','号分割")
    private String vehIds;

    @ApiModelProperty(value = "指令id")
    @NotNull(message = "指令id不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    private String instructId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "车辆列表查询条件（选择全部车辆数据时使用，使用查询条件时vehIds要保持为空）")
    private PagerInfo pagerInfo;

}
