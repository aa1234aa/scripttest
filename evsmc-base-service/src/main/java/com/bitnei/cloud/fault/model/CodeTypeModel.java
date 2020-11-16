package com.bitnei.cloud.fault.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.fault.domain.CodeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CodeType新增模型<br>
* 描述： CodeType新增模型<br>
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
* <td>2019-02-25 10:22:15</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author joinly
* @since JDK1.8
*/
@Data
@ApiModel(value = "CodeTypeModel", description = "故障Model")
public class CodeTypeModel extends BaseModel {

    private String id;

    @ColumnHeader(title = "故障种类名称")
    @NotBlank(message = "故障种类名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Length(min = 2, max = 20, message = "故障种类名称长度为2~20个字符", groups = { GroupInsert.class,GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "故障种类名称")
    private String name;

    @ColumnHeader(title = "协议类型, 引用 dc_rule_type表的id")
    @NotBlank(message = "协议类型, 引用 dc_rule_type表的id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "协议类型, 引用 dc_rule_type表的id")
    private String dcRuleTypeId;

    @ApiModelProperty(value = "协议类型名称")
    @LinkName(table = "dc_rule_type", column = "name", joinField = "dcRuleTypeId",desc = "")
    private String dcRuleTypeName;

    @ColumnHeader(title = "类型编码")
    @NotBlank(message = "类型编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[0-9a-fA-F]{1,10}$",message="类型编码为十六进制字符,长度为:1~10个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "类型编码")
    private String typeCode;

    @ColumnHeader(title = "故障码个数")
    @NotNull(message = "故障码个数不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "故障码个数")
    private Integer faultNumber;

    @ColumnHeader(title = "故障种类唯一标识编码")
    @NotBlank(message = "故障种类唯一标识编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[0-9a-zA-Z]{2,8}$",message="lenCode格式为数字和英文大写字母且长度为2-4个字符", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "故障种类唯一标识编码")
    private String lenCode;

    @ColumnHeader(title = "备注")
    @Length(min = 0, max = 100, message = "备注长度为:0~100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String remark;

    private String createTime;

    private String createBy;

    private String updateTime;

    private String updateBy;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static CodeTypeModel fromEntry(CodeType entry){
        CodeTypeModel m = new CodeTypeModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }
}
