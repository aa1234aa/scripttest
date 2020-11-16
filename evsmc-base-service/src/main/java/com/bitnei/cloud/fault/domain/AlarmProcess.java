package com.bitnei.cloud.fault.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： AlarmProcess实体<br>
 * 描述： AlarmProcess实体<br>
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
 * <td>2019-03-04 17:13:13</td>
 * <td>joinly</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author joinly
 * @version 1.0
 * @since JDK1.8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmProcess extends TailBean {

    /**
     * 主键
     **/
    private String id;
    /**
     * 报警信息id
     **/
    private String faultAlarmId;
    /**
     * 处理状态  1:未处理, 2:处理中, 3:已处理
     **/
    private Integer procesStatus;
    /**
     * 备注
     **/
    private String remark;

    /**
     * 是否再次提醒: 0=不再提醒, 30=30分钟后, 60=一小时后, 120=两小时后, 360=六小时后
     **/
    private Integer againRemindStatus;

    /**
     * 再次提醒时间
     */
    private String againRemindTime;

    /**
     * 处理时间
     **/
    private String createTime;
    /**
     * 处理人
     **/
    private String createBy;

    /**
     * 报警开始时间  用于分表处理
     **/
    private String faultBeginTime;

}
