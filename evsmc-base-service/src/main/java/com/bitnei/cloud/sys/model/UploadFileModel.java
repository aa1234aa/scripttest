package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UploadFile新增模型<br>
* 描述： UploadFile新增模型<br>
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
* <td>2018-10-31 12:42:12</td>
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
@ApiModel(value = "UploadFileModel", description = "上传文件管理Model")
public class UploadFileModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "文件名")
    @NotEmpty(message = "文件名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件名")
    private String name;

    @ColumnHeader(title = "文件扩展名")
    @NotEmpty(message = "文件扩展名不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件扩展名")
    private String extension;

    @ColumnHeader(title = "文件大小，单位Kb")
    @NotNull(message = "文件大小，单位Kb不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件大小，单位Kb")
    private Integer fileSize;

    @ColumnHeader(title = "存储类型 1:数据库 2：本地 3：远程url")
    @NotNull(message = "存储类型 1:数据库 2：本地 3：远程url不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "存储类型 1:数据库 2：本地 3：远程url")
    private Integer type;

    @ColumnHeader(title = "文件内容，当type!=1时为空")
    @NotEmpty(message = "文件内容，当type!=1时为空不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "文件内容，当type!=1时为空")
    private String content;

    @ColumnHeader(title = "url，当type=1时为空")
    @NotEmpty(message = "url，当type=1时为空不能为空", groups = { GroupInsert.class,GroupUpdate.class})
    @ApiModelProperty(value = "url，当type=1时为空")
    private String url;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

}
