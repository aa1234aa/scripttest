package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

import java.lang.String;
import java.lang.Integer;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： Unit实体<br>
 * 描述： Unit实体<br>
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
 * <td>2018-11-05 17:33:20</td>
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
public class Unit extends TailBean {

    /**
     * 主键 *
     */
    private String id;
    /**
     * 单位名称 *
     */
    private String name;
    /**
     * 单位简称 *
     */
    private String nickName;
    /**
     * 是否根节点 *
     */
    private Integer isRoot;
    /**
     * 父节点id *
     */
    private String parentId;
    /**
     * 路径 *
     */
    private String path;
    /**
     * 单位地址 *
     */
    private String address;
    /**
     * 座机号 *
     */
    private String telephone;
    /**
     * 业务种类 *
     */
    private String bussinessLines;
    /**
     * 产品品牌，多个用逗号分隔 *
     */
    private String brands;
    /**
     * 统一社会信用代码 *
     */
    private String organizationCode;
    /**
     * 营业执照扫描图片id *
     */
    private String licenceImgId;
    /**
     * 授权书扫描件ID *
     */
    private String certImgId;
    /**
     * 联系人姓名 *
     */
    private String contactorName;
    /**
     * 联系人手机号 *
     */
    private String contactorPhone;
    /**
     * 联系人地址 *
     */
    private String contactorAddress;
    /**
     * 创建时间 *
     */
    private String createTime;
    /**
     * 创建人 *
     */
    private String createBy;
    /**
     * 更新时间 *
     */
    private String updateTime;
    /**
     * 更新人 *
     */
    private String updateBy;

    /**
     * 单位类型id，多个用逗号分隔
     */
    private String unitTypeIds;
    /**
     * 单位类型名称，多个用逗号分隔
     */
    private String unitTypeNames;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;


}
