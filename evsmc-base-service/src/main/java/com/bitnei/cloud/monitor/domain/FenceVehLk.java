package com.bitnei.cloud.monitor.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DFY
 * @description
 * @date 2019/5/23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FenceVehLk extends TailBean {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 电子围栏id
     */
    private String electronicFenceId;
    /**
     * 车辆id
     */
    private String vid;
    /**
     * 是否有效1有效0无效
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

}
