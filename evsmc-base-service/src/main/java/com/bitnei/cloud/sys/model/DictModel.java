package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.*;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Dict新增模型<br>
* 描述： Dict新增模型<br>
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
* <td>2018-10-31 16:54:54</td>
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
@ApiModel(value = "DictModel", description = "字典Model")
public class DictModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @NotEmpty(message = "名称不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9()/-]{1,10}$", message = "名称长度为1-10个中英文或数字、()/-", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "名称")
    private String name;

    @NotEmpty(message = "值不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[a-zA-Z0-9()/-]{1,32}$", message = "值长度为1-32个英文或数字", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "值")
    private String value;

    @NotEmpty(message = "编码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp = "^[a-zA-Z0-9_]{1,32}$", message = "编码长度为1-32个英文数字及_下划线", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "编码")
    private String type;

    @Pattern(regexp = "^[\\s\\S]{0,100}$", message = "备注长度为0-100个字符", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")
    private String note;

    @Pattern(regexp = "^[a-zA-Z0-9_]{1,20}|$", message = "组名长度为1-20个英文数字及_下划线", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "组名")
    private String groupName;

    @Digits(integer = 3, fraction = 0, message = "序号整数长度1~3位", groups = {GroupInsert.class, GroupUpdate.class})
    @DecimalMin(value = "0", message = "序号范围0-999之间", groups = { GroupInsert.class,GroupUpdate.class})
    @DecimalMax(value = "999", message = "序号范围0-999之间", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "序号")
    private Integer orderNum;


    /**
     * 将实体转为前台model
     * @param entry Dict
     * @return DictModel
     */
    public static DictModel fromEntry(Dict entry){
        DictModel m = new DictModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
