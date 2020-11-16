package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.bitnei.cloud.sys.domain.UppackageInfo;
import org.springframework.beans.BeanUtils;

import java.lang.String;

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
public class UppackageInfoModel extends BaseModel {

    @ApiModelProperty(value = "主键标识")
    private String id;

    @ColumnHeader(title = "文件名")
    @NotEmpty(message = "文件名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件名")
    @Length(min = 2, max = 128, message = "文件名长度为2-128个字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String fileName;

    @ColumnHeader(title = "文件别名")
    @NotEmpty(message = "文件别名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件别名")
    @Length(min = 2, max = 128, message = "文件别名长度为2-128个字符", groups = { GroupInsert.class,GroupUpdate.class})
    private String nickname;

    @ColumnHeader(title = "文件类型(0x01:DBC、0x02:BMS主机、0x03:文件存储URL、0x04:BMS-CAN盒、)")
    @NotEmpty(message = "文件类型(0x01:DBC、0x02:BMS主机、0x03:文件存储URL、0x04:BMS-CAN盒、)不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件类型(0x01:DBC、0x02:BMS主机、0x03:文件存储URL、0x04:BMS-CAN盒、)")
    private String type;

    @DictName(code = "UP_PACKAGE_TYPE", joinField = "type")
    @ApiModelProperty(value = "文件类型描述")
    private String typeName;

    @ColumnHeader(title = "密码")
    @NotEmpty(message = "密码不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "密码")
    private String password;

    @ColumnHeader(title = "固件版本号")
    @ApiModelProperty(value = "固件版本号")
    private String firmwareVersion;

    @ColumnHeader(title = "扩展版本号")
    @ApiModelProperty(value = "扩展版本号")
    private String extVersion;

    @ColumnHeader(title = "硬件版本号")
    @ApiModelProperty(value = "硬件版本号")
    private String hardwareVersion;

    @ColumnHeader(title = "描述")
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
    @NotEmpty(message = "导入文件失败，文件过大或文件名称不合法", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "存储路径")
    private String path;


   /**
     * 将实体转为前台model
     * @param entry
     * @return
     */
    public static UppackageInfoModel fromEntry(UppackageInfo entry){
        UppackageInfoModel m = new UppackageInfoModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

    public static UppackageInfoDto dtoFromEntry(UppackageInfo entry){
        UppackageInfoDto m = new UppackageInfoDto();
        BeanUtils.copyProperties(entry, m);
        return m;
    }

}
