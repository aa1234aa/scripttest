package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.UppackageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UppackageInfo新增模型<br>
* 描述： UppackageInfo新增模型<br>
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
* <td>2019-03-04 14:05:24</td>
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
@ApiModel(value = "UppackageInfoModel", description = "升级包信息Model")
public class UppackageInfoDto extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "文件名")
    @NotEmpty(message = "文件名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ColumnHeader(title = "文件别名")
    @NotEmpty(message = "文件别名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件别名")
    private String nickname;

    @ColumnHeader(title = "文件类型(0x01:DBC、0x02:BMS主机、0x03:文件存储URL、0x04:BMS-CAN盒、)")
    @NotEmpty(message = "文件类型(0x01:DBC、0x02:BMS主机、0x03:文件存储URL、0x04:BMS-CAN盒、)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件类型(0x01:DBC、0x02:BMS主机、0x03:文件存储URL、0x04:BMS-CAN盒、)")
    private String type;

    @DictName(code = "UP_PACKAGE_TYPE", joinField = "type")
    @ApiModelProperty(value = "文件类型描述")
    private String typeName;

    @ColumnHeader(title = "固件版本号")
    @NotEmpty(message = "固件版本号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "固件版本号")
    private String firmwareVersion;

    @ColumnHeader(title = "扩展版本号")
    @NotEmpty(message = "扩展版本号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "扩展版本号")
    private String extVersion;

    @ColumnHeader(title = "硬件版本号")
    @NotEmpty(message = "硬件版本号不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "硬件版本号")
    private String hardwareVersion;

    @ColumnHeader(title = "描述")
    @NotEmpty(message = "描述不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "描述")
    private String describes;

    @ApiModelProperty(value = "添加人姓名")
    private String createBy;

    @ColumnHeader(title = "添加人ID")
    @ApiModelProperty(value = "添加人ID")
    private String createById;

    @ApiModelProperty(value = "添加时间")
    private String createTime;

    @ColumnHeader(title = "存储路径")
    @NotEmpty(message = "存储路径不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "存储路径")
    private String path;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static UppackageInfoDto fromEntry(UppackageInfo entry){
        UppackageInfoDto m = new UppackageInfoDto();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
