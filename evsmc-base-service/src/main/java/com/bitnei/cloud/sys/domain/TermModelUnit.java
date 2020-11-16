package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.bean.TailBean;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： TermModelUnit实体<br>
* 描述： TermModelUnit实体<br>
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
* <td>2018-11-05 10:01:48</td>
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
@Setter
@Getter
public class TermModelUnit extends TailBean {

    /** 主键 **/
    private String id;
    /** 终端厂商自定义编号 **/
    private String serialNumber;
    /** 终端型号ID **/
    private String sysTermModelId;
    /** 支持通讯协议 **/
    private String supportProtocol;
    /** 协议版本号 **/
    private String protocolVersion;
    /** ICCID **/
    private String iccid;
    /** imei **/
    private String imei;
    /** 终端零件号 **/
    private String termPartFirmwareNumber;
    /** 固件版本号 **/
    private String firewareVersion;
    /** 生产批次 **/
    private String produceBatch;
    /** 出厂日期 **/
    private String factoryDate;
    /** 创建时间 **/
    private String createTime;
    /** 创建人 **/
    private String createBy;
    /** 更新时间 **/
    private String updateTime;
    /** 更新人 **/
    private String updateBy;

    //add by renshuo
    /** 移动用户号 **/
    private String msisd;

    /** 车辆vin **/
    private String vin;

    /** 终端单位 */
    private String unitId;

    /** 条形码 */
    private String barCode;

    /** 协议类型 */
    private String ruleTypeId;

    /** 加密芯片ID */
    private String encryptionChipId;
    /** 终端入网证号 */
    private String networkAccessNumber;
    /** 车载终端说明 */
    private String termDescription;
    /** 车载终端其他文件 */
    private String otherFile;

}
