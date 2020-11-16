package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.orm.bean.Condition;
import com.bitnei.cloud.screen.protocol.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能： 请完善功能说明 <br>
 * 描述： 这个人很懒，什么都没有留下 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2019-03-12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
@Data
@ApiModel(value = "GroupListAddModel", description = "列表模式新增Model")
public class GroupListAddModel {

    @NotBlank(message = "组ID不能为空", groups = { GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "组ID")
    private String groupId;


    @NotBlank(message = "资源类型ID不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "资源类型ID")
    private String resourceTypeId;

    @ApiModelProperty(value = "值，多个用逗号分隔")
    private String val;

    @ApiModelProperty(value = "是否全部")
    private boolean all;

    @ApiModelProperty(value = "查询条件")
    private Condition[] conditions;

    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 获取val的size
     * @return
     */
    public int valSize(){

        if (StringUtil.isEmpty(val)){
            return 0;
        }
        return val.trim().split(",").length;
    }


}
