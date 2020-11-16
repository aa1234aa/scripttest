package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.CoreResource;
import com.bitnei.cloud.sys.util.RegexUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Pattern;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreResource新增模型<br>
* 描述： CoreResource新增模型<br>
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
* <td>2018-11-05 09:32:22</td>
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
@Data
@ApiModel(value = "CoreResourceModel", description = "核心资源类型Model")
public class CoreResourceModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "资源名")
    @NotEmpty(message = "资源名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "资源名")

    @Length(min=1,max=32,message="资源名长度必须为1-32",groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[\\w\\u4e00-\\u9fa5]+$",message="资源名只接受汉字字母下划线，不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String name;

    @ColumnHeader(title = "表名")
    @NotEmpty(message = "表名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "表名")
    @Length(min=1,max=32,message="表名长度必须为1-32", groups = { GroupInsert.class,GroupUpdate.class})
    @Pattern(regexp="^[\\w\\u4e00-\\u9fa5]+$",message="表名只接受汉字字母下划线，不能使用特殊字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String tableName;


    @ColumnHeader(title = "备注")
    @Pattern(regexp = RegexUtil.C_0_100, message = "备注最大长度为100", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "备注")

    private String note;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


    /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static CoreResourceModel fromEntry(CoreResource entry){
        CoreResourceModel m = new CoreResourceModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
