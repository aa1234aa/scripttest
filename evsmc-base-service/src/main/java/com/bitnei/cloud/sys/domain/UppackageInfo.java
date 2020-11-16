package com.bitnei.cloud.sys.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UppackageInfo实体<br>
* 描述： UppackageInfo实体<br>
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
* <td>2019-03-04 14:05:24</td>
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
public class UppackageInfo extends TailBean {

    /** 主键标识 **/
    private String id;
    /** 文件名 **/
    private String fileName;
    /** 文件别名 **/
    private String nickname;
    /** 文件类型(0x01:DBC、0x02:BMS主机、0x03:文件存储URL、0x04:BMS-CAN盒、) **/
    private String type;
    /** 密码 **/
    private String password;
    /** 固件版本号 **/
    private String firmwareVersion;
    /** 扩展版本号 **/
    private String extVersion;
    /** 硬件版本号 **/
    private String hardwareVersion;
    /** 描述 **/
    private String describes;
    /** 添加人姓名 **/
    private String createBy;
    /** 添加人ID **/
    private String createById;
    /** 添加时间 **/
    private String createTime;
    /** 存储路径 **/
    private String path;

}
