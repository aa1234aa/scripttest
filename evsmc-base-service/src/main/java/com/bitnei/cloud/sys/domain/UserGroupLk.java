package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

/**
 * @author renshuo
 * @description 数据权限——用户关联表
 * @date 2019/01/08
 */
@Setter
@Getter
public class UserGroupLk extends TailBean {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 权限组id
     */
    private String groupId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String createBy;

}
