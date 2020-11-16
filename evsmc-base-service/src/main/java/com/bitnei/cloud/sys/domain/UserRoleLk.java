package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

/**
 * @author renshuo
 * @description 角色——用户关联表
 * @date 2019/01/09
 */
@Setter
@Getter
public class UserRoleLk extends TailBean {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;

}
