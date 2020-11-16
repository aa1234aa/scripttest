package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.sys.model.SoftwareVersion;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-base-service <br>
 * 功能：  系统帮助 <br>
 * 描述： 系统帮助 <br>
 * 授权 : (C) Copyright (c) 2018 <br>
 * 公司 : 北京理工新源信息技术有限公司<br>
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
 * <td>2019-04-12</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息技术有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */
public interface ISystermHelpService {

    /**
     * 获取系统版本信息
     * @return
     */
    SoftwareVersion getVersion();

    /**
     * 获取详细的错误信息
     * @param code
     * @return
     */
    void getErrorMessage(final String code);

    /**
     * 检查状态
     */
    void health();
}
