package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import com.bitnei.cloud.common.constant.ReferenceStaticClass;
import com.bitnei.cloud.orm.bean.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： OfflineExport实体<br>
* 描述： OfflineExport实体<br>
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
* <td>2019-04-03 13:33:38</td>
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class OfflineExport extends TailBean {

    /** 主键 **/
    private String id;
    /** 导出文件名称 **/
    private String exportFileName;
    /** 状态机:1-已创建,2-导出中,3-已完成, 4-已取消, 5-有异常. **/
    @ReferenceStaticClass(OfflineExportStateMachine.class)
    private Integer stateMachine;
    /** 导出服务名称 **/
    private String exportServiceName;
    /** 导出方法名称 **/
    private String exportMethodName;
    /** 导出方法参数 **/
    private String exportMethodParams;
    /** 导出备注 **/
    private String exportNote;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

    /** 执行任务系统名称 **/
    private String systemName;

    /**
     * 当前用户权限
     */
    private String authSQL;

    /**
     * 排序条件
     */
    private List<Sort> sorts;

    public static final String TABLE ="sys_offline_export";

    public static final String TABLE_ALIAS="soe";
}
