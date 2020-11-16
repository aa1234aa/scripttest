package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.bean.TokenInfo;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.domain.User;
import com.bitnei.cloud.sys.model.UserModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： UserService接口<br>
 * 描述： UserService接口，在xml中引用<br>
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
 * <td>2018-11-12 10:33:47</td>
 * <td>chenpeng</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * <br>
 * <font color="#FF0000">注意: 本内容仅限于[北京理工新源信息科技有限公司]内部使用，禁止转发</font> <br>
 *
 * @author chenpeng
 * @version 1.0
 * @since JDK1.8
 */

public interface IUserService extends IBaseService {

    /**
     * 全部查询
     * @param pagerInfo PagerInfo
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 查询全部
     * @param params 查询条件
     * @return List集合
     */
    List<User> find(Map<String, Object> params);

    /**
     * 根据id获取
     * @param id id
     * @return UserModel
     */
    UserModel get(String id);

    /**
     * 新增
     *
     * @param model 新增model
     */
    void insert(UserModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     */
    void update(UserModel model);

    /**
     * 删除多个
     *
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    int deleteMulti(String ids);

    /**
     * 导出
     *
     * @param pagerInfo 查询参数
     */
    void export(PagerInfo pagerInfo);

    /**
     * 批量导入
     *
     * @param file 文件
     */
    void batchImport(MultipartFile file);

    /**
     * 批量更新
     *
     * @param file 文件
     */
    void batchUpdate(MultipartFile file);

    /**
     * 自定义单条搜索
     *
     * @param params 自定义参数
     * @return
     */
    UserModel queryUserByParam(Map<String, Object> params);

    /**
     * 重置密码
     *
     * @param username 用户名
     * @param password 新密码
     */
    void resetPwd(String username, String password);

    /**
     * 通过用户名查询用户信息
     *
     * @param username
     * @return 用户信息
     */
    UserModel findByUsername(String username);

    void copyGroupAndRole(String sourceUserId, String targetUserId);


    /**
     * 通过用户Id生效token信息(方便开放接口的数据权限处理)
     * @param userId
     * @return
     */
    TokenInfo createTokenByUserId(String userId);

    /**
     * 管理员冻结账号
     * @param userId 用户id
     * @param reason 冻结原因
     */
    void adminFrozenUser(String userId,String reason);

    /**
     * 管理员解冻账号
     * @param userId 用户id
     */
    void adminUnfrozenUser(String userId);

    /**
     *  修改头像
     * @param userId 用户id
     * @param photoId 头像文件id
     */
    void resetPhoto(String userId, String photoId);

    /**
     * 开通app权限
     * @param id
     */
    void openAppPermissions(String id);

    /**
     * 关闭app权限
     * @param id
     */
    void turnOffAppPermissions(String id);
}
