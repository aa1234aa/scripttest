package com.bitnei.cloud.sys.model;

import com.bitnei.cloud.common.annotation.ColumnHeader;
import com.bitnei.cloud.common.annotation.DictName;
import com.bitnei.cloud.common.annotation.LinkName;
import com.bitnei.cloud.common.bean.BaseModel;
import com.bitnei.cloud.common.validator.group.GroupExcelImport;
import com.bitnei.cloud.common.validator.group.GroupInsert;
import com.bitnei.cloud.common.validator.group.GroupUpdate;
import com.bitnei.cloud.sys.domain.OwnerPeople;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： OwnerPeople新增模型<br>
 * 描述： OwnerPeople新增模型<br>
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
 * <td>2018-11-02 15:19:04</td>
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
@Setter
@Getter
@ApiModel(value = "OwnerPeopleModel", description = "联系人管理Model")
public class OwnerPeopleModel extends BaseModel {

    @ApiModelProperty(value = "主键")
    private String id;

    @ColumnHeader(title = "联系人姓名", example = "汪涵", desc = "联系人姓名长度1-10个字符")
    @NotEmpty(message = "姓名不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Length(min = 1, max = 10, message = "姓名长度1-10个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "姓名")
    private String ownerName;

    @NotEmpty(message = "单位不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "单位id")
    private String unitId;

    @ColumnHeader(title = "所属单位", example = "上海汽车车载终端电器有限公司", desc = "所属单位")
    @ApiModelProperty(value = "单位名称")
    @LinkName(table = "sys_unit", joinField = "unitId")
    private String unitName;

    @ColumnHeader(title = "岗位", example = "嵌入式开发", desc = "岗位为2-30个中英文或数字")
    @NotEmpty(message = "岗位不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,30}$", message = "岗位为2-30个中英文或数字", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "岗位")
    private String jobPost;

    @ColumnHeader(title = "工号", example = "0758", notNull = false, desc = "工号长度2-30个字符")
    @Pattern(regexp = "^$|^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,30}$", message = "工号长度2-30个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ColumnHeader(title = "性别", notNull = false, example = "男", desc = "性别选项:男或女")
    @DictName(code = "SEX", joinField = "sex")
    private String sexName;

    @ColumnHeader(title = "手机号", example = "13078908907", desc = "手机号")
    @NotEmpty(message = "手机号不能为空", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^[1][3,4,5,7,8,9][0-9]{9}$", message = "手机号格式错误", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "手机号")
    private String telPhone;

    @ColumnHeader(title = "联系地址", example = "上海市浦东开发区大润发小区5楼", desc = "联系地址长度4-50个字符")
    @NotEmpty(message = "联系地址不能为空", groups = {GroupInsert.class, GroupUpdate.class})
    @Length(min = 4, max = 50, message = "联系地址长度4-50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "联系地址")
    private String address;

    @ColumnHeader(title = "邮箱", notNull = false, example = "pepc@126.com", desc = "电子邮箱长度为3-50个字符")
    @Length(max = 50, message = "电子邮箱长度为3-50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @Pattern(regexp = "^$|^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$", message = "电子邮箱格式错误", groups = {GroupInsert.class, GroupUpdate.class})
    @ApiModelProperty(value = "邮箱")
    protected String email;


    @ApiModelProperty(value = "证件类型 1:居民身份证 2:士官证 3 学生证 4 驾驶证 5 护照 6 港澳通行证")
    private Integer cardType;

    @ColumnHeader(title = "证件类型", notNull = false, example = "居民身份证", desc = "证件类型选项: 居民身份证|士官证|学生证|驾驶证|护照|港澳通行证")
    @ApiModelProperty(value = "证件类型名称")
    @DictName(code = "CERT_TYPE", joinField = "cardType")
    private String cardTypeName;

    @ColumnHeader(title = "证件号码", notNull = false, example = "430524199103151111", desc = "证件号码")
    @ApiModelProperty(value = "证件号码")
    private String cardNo;

    @ColumnHeader(title = "联系人证件地址", notNull = false, example = "上海市浦东开发区大润发小区5楼", desc = "联系人证件地址长度4-50个字符")
    @Length(min = 4, max = 50, message = "证件地址长度4-50个字符", groups = {GroupInsert.class, GroupUpdate.class, GroupExcelImport.class})
    @ApiModelProperty(value = "证件地址")
    private String cardAddress;

    @ApiModelProperty(value = "证件正面照id")
    private String frontCardImgId;

    @ApiModelProperty(value = "证件反面照id")
    private String backCardImgId;

    @ApiModelProperty(value = "手持证件照id")
    private String faceCardImgId;

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
     * @param entry OwnerPeople
     * @return OwnerPeopleModel
     */
    public static OwnerPeopleModel fromEntry(OwnerPeople entry) {
        OwnerPeopleModel m = new OwnerPeopleModel();
        BeanUtils.copyProperties(entry, m);
        return m;
    }


}
