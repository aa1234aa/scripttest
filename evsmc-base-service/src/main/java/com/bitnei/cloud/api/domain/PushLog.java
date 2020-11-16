package com.bitnei.cloud.api.domain;

import com.bitnei.cloud.common.annotation.*;
import com.bitnei.cloud.common.bean.TailBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.String;
import java.lang.Integer;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： PushLog实体<br>
* 描述： PushLog实体<br>
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
* <td>2019-01-16 15:24:34</td>
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
public class PushLog extends TailBean {

    /** 主键 **/
    private String id;
    /** 推送明细ID **/
    private String pushId;
    /** 账号id **/
    private String accountId;
    /** 推送时间 **/
    private String pushTime;
    /** 是否推送成功 **/
    private Integer isSuccess;
    /** 推送成功时间 **/
    private String pushSuccessTime;
    /** 错误信息 **/
    private String errorMsg;
    /** 推送内容 **/
    private String pushBodyContent;
    /** 响应体内容 **/
    private String rspBodyContent;
    /** 是否重新推送 **/
    private Integer rePushFlag;

}
