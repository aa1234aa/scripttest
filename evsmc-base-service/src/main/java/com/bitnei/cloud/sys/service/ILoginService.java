package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.api.TreeNode;

import java.util.Map;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： evsmc-module <br>
 * 功能： 用户登入登出业务 <br>
 * 描述： 用户登入登出业务 <br>
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
 * <td>2018-${MOTH}-19</td>
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
public interface ILoginService {

    /**
     * 生成CSRF码，写到cookie，并存储到redis
     */
    void createCsrf();

    /**
     * 用户登入
     * 登入成功将，需要将csrf和token进行绑定，存储到redis
     *
     * @param username      用户名
     * @param password      密码
     * @param randCode      随机码
     * @return
     *  登入成功时，返回token令牌，否则为空
     */
    Map<String,Object> login(String username, String password, String randCode, String isApp);


    /**
     * 用户登出
     *
     * 用户登出时需要清理token和csrf记录
     */
    void logout();

    /**
     * 我的资料
     * @return
     */
    Map<String,Object> me();


    /**
     * 获取用户菜单
     * @return
     */
    TreeNode menus();

    /**
     * 生成随机验证码
     * @return
     */
    String randCode();

    /**
     * 生成随机验证码
     * @param mobile
     * @return
     */
    String randCodeToMobile(String mobile);

    /**
     * 用户手机登入
     * @param mobile 电话号码
     * @param randCode 验证码
     * @return
     */
    Map<String, Object> loginByMobile(String mobile, String randCode);

}
