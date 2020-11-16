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
* 功能： PushBatch实体<br>
* 描述： PushBatch实体<br>
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
* <td>2019-02-27 19:36:18</td>
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
public class PushBatch extends TailBean {

    /** 反馈状态:未反馈 */
    public static final Integer STATUS_DID = 1;

    /** 反馈状态:已反馈 */
    public static final Integer STATUS_YET = 2;

    /** 批次类型:车辆 */
    public static final Integer BATCH_TYPE_VEH = 1;
    /** 批次类型:车型 */
    public static final Integer BATCH_TYPE_VEH_MODEL = 2;


    /** 主键标识 **/
    private String id;
    /** 批次号 **/
    private String batchNum;
    /** 批次类型(1:车辆 2:车型 3:运营单位 4:车辆销售) **/
    private Integer batchType;
    /** 反馈状态(1:未反馈 2:已反馈) **/
    private Integer status;
    /** 备注 **/
    private String remark;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

}
