package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;

import java.lang.String;
import java.lang.Integer;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： VehOwner实体<br>
 * 描述： VehOwner实体<br>
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
 * <td>2018-10-31 15:36:59</td>
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
public class VehOwner extends TailBean {

    /**
     * 主键 *
     */
    private String id;
    /**
     * 姓名 *
     */
    private String ownerName;
    /**
     * 性别 *
     */
    private Integer sex;
    /**
     * 手机号 *
     */
    private String telPhone;
    /**
     * 联系地址 *
     */
    private String address;
    /**
     * 电子邮箱 *
     */
    private String email;
    /**
     * 证件类型 1:居民身份证 2:士官证 3 学生证 4 驾驶证 5 护照 6 港澳通行证 *
     */
    private Integer cardType;
    /**
     * 证件号码 *
     */
    private String cardNo;
    /**
     * 证件地址 *
     */
    private String cardAddress;
    /**
     * 证件正面照id *
     */
    private String frontCardImgId;
    /**
     * 证件反面照id *
     */
    private String backCardImgId;
    /**
     * 手持证件照id *
     */
    private String faceCardImgId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardAddress() {
        return cardAddress;
    }

    public void setCardAddress(String cardAddress) {
        this.cardAddress = cardAddress;
    }

    public String getFrontCardImgId() {
        return frontCardImgId;
    }

    public void setFrontCardImgId(String frontCardImgId) {
        this.frontCardImgId = frontCardImgId;
    }

    public String getBackCardImgId() {
        return backCardImgId;
    }

    public void setBackCardImgId(String backCardImgId) {
        this.backCardImgId = backCardImgId;
    }

    public String getFaceCardImgId() {
        return faceCardImgId;
    }

    public void setFaceCardImgId(String faceCardImgId) {
        this.faceCardImgId = faceCardImgId;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

}
