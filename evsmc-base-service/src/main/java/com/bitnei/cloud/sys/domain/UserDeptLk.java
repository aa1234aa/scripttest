package com.bitnei.cloud.sys.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author DFY
 * @description 组织架构——用户关联表
 * @date 2018/11/14
 */
@Setter
@Getter
public class UserDeptLk {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 组织架构id
     */
    private String deptId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createBy;


}
