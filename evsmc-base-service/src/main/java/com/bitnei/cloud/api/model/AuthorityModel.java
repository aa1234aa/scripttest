package com.bitnei.cloud.api.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.api.domain.Authority;
import org.springframework.beans.BeanUtils;

import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： Authority新增模型<br>
* 描述： Authority新增模型<br>
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
* <td>2019-01-22 10:39:46</td>
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
@ApiModel(value = "AuthorityModel", description = "接口授权Model")
public class AuthorityModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "账号id")
    @NotEmpty(message = "账号id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "账号id")
    private String accountId;

    @ColumnHeader(title = "接口明细id")
    @NotEmpty(message = "接口明细id不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "接口明细id")
    private String apiDetailId;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static AuthorityModel fromEntry(Authority entry){
        AuthorityModel m = new AuthorityModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
