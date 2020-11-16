package com.bitnei.cloud.sms.model;

import lombok.Data;

/**
 * @Desc：
 * @Author: joinly
 * @Date: 2019/8/21
 */

@Data
public class UserModel {

    private String id;

    public String name;

    private String telPhone;

    /**单位**/
    private String unitName;

    /**岗位**/
    private String jobPost;

    /**工号**/
    private String jobNumber;

    /**区域**/
    private String areaName;

    private Integer userType;

    public UserModel() {

    }

    public UserModel(String id, String name, String telPhone) {
        this.id = id;
        this.name = name;
        this.telPhone = telPhone;
    }
}
