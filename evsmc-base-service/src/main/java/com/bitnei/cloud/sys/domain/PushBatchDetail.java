package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PushBatchDetail实体<br>
* 描述： PushBatchDetail实体<br>
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
* <td>2019-02-27 19:37:27</td>
* <td>hzr</td>
* <td>创建</td>
* </tr>
* </table>
* <br>
* <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
*
* @version 1.0
*
* @author hzr
* @since JDK1.8
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PushBatchDetail extends TailBean {

    /** 推送中 */
    public static final Integer PUSH_STATUS_ING = 0;
    /** 推送成功 */
    public static final Integer PUSH_STATUS_SUCCESS = 1;
    /** 推送失败 */
    public static final Integer PUSH_STATUS_FAIL = 2;

    /** 审核状态:未审核 */
    public static final Integer AUDIT_STATUS_DID = 0;


    /*
      -- 审核状态 ===
      -- 0-基础数据未审核
      -- 1-基础数据程序审核通过
      -- 2-基础数据程序审核未通过
      -- 3-基础数据人工审核通过
      -- 4-基础数据人工审核未通过
      -- 5-实时数据程序审核通过
      -- 6-实时数据程序审核未通过
      -- 7-实时数据人工审核通过
     */

    /** 主键标识 **/
    private String id;
    /** 批次id **/
    private String batchId;
    /** 关联id **/
    private String formId;
    /** 关联名称(车型名称|车辆vin|运营单位名称) */
    private String formName;
    /** 转发车辆id **/
    private String forwardVehicleId;
    /** 推送状态(0: 推送中 1:推送成功 2:推送失败) **/
    private Integer pushStatus;
    /** 错误信息 **/
    private String errorInfo;
    /** 审核状态 **/
    private Integer auditStatus;
    /** 审核信息 **/
    private String auditMessage;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

    /** 批次号 **/
    private String batchNum;
    /** 批次类型 **/
    private String batchType;
    /** vin **/
    private String vin;




}
