package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.RemoteDiagnose;
import org.springframework.beans.BeanUtils;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RemoteDiagnose新增模型<br>
* 描述： RemoteDiagnose新增模型<br>
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
* <td>2019-03-25 17:01:37</td>
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
@ApiModel(value = "RemoteDiagnoseModel", description = "远程诊断记录Model")
public class RemoteDiagnoseModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "vin")
    @NotEmpty(message = "vin不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "vin")
    private String vin;

    @ColumnHeader(title = "车牌号")
    @NotEmpty(message = "车牌号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ColumnHeader(title = "车辆型号名称")
    @NotEmpty(message = "车辆型号名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "车辆型号名称")
    private String vehModelName;

    @ColumnHeader(title = "诊断方式，0历史诊断，1实时诊断")
    @NotBlank(message = "诊断方式，0历史诊断，1实时诊断不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "诊断方式，0历史诊断，1实时诊断")
    private Integer diagnosticMode;

    @DictName(code = "DIAGNOSIS_WAY", joinField = "diagnosticMode")
    @ApiModelProperty(value = "诊断方式")
    private String diagnosticModeName;

    @ColumnHeader(title = "诊断时间")
    @NotEmpty(message = "诊断时间不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "诊断时间")
    private String diagnositicTime;

    @ColumnHeader(title = "诊断结果，0正常，1失败，2异常")
    @NotBlank(message = "诊断结果，0正常，1失败，2异常不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "诊断结果，0正常，1失败，2异常")
    private Integer resultState;

    @DictName(code = "DIAGNOSIS_RESULT", joinField = "resultState")
    @ApiModelProperty(value = "诊断结果")
    private String resultStateName;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ColumnHeader(title = "创建人id")
    @NotEmpty(message = "创建人id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "创建人id")
    private String createById;

    @ColumnHeader(title = "内部编号")
    @ApiModelProperty(value = "内部编号")
    private String interNo;

    @ColumnHeader(title = "诊断明细id")
    @ApiModelProperty(value = "诊断明细id")
    private String detailId;

   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static RemoteDiagnoseModel fromEntry(RemoteDiagnose entry){
        RemoteDiagnoseModel m = new RemoteDiagnoseModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
