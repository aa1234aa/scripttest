package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.Role;
import com.bitnei.cloud.sys.model.ModuleDataItemModel;
import com.bitnei.cloud.sys.model.RoleModel;
import com.bitnei.cloud.service.IBaseService;
import com.bitnei.cloud.sys.model.UserModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RoleService接口<br>
* 描述： RoleService接口，在xml中引用<br>
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
* <td>2018-11-22 10:06:09</td>
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

public interface IRoleService extends IBaseService {


    /**
     * 全部查询
     * @return
     *  返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
     RoleModel get(String id);


    /**
     * 根据角色名称获取
     *
     * @return
     */
     RoleModel getByName(String name);


    /**
     * 新增
     * @param model  新增model
     * @return
     */
    RoleModel insert(RoleModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(RoleModel model);

    /**
    * 删除多个
     *
     * @param ids  id列表，用逗号分隔
     * @return
     *  影响行数
    */
    int deleteMulti(String ids);

    /**
     * 导出
     *
     * @param params 查询参数
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
     * 查询关联账号
     *
     * @param
     */
    List<UserModel> queryAssociatedAccount(String roleId, String trueName, String loginName, String mobile);

    /**
     * 查询未关联账号
     *
     * @param
     */
    List<UserModel> queryUnassociatedAccount(String roleId, String trueName, String loginName, String mobile);

    /**
     * 关联账号
     *
     * @param
     */
    void bindAccount(String roleId, List<String> accountIds);

    /**
     * 查询关联角色
     *
     * @param
     */
    List<RoleModel> queryAssociatedRole(String userId, String roleName);

    /**
     * 查询未关联角色
     *
     * @param
     */
    List<RoleModel> queryUnassociatedRole(String userId, String roleName);

    /**
     * 关联角色
     *
     * @param
     */
    void bindRole(String userId, List<String> roleIds);

    /**
     * 解除关联角色
     *
     * @param
     */
    void unbindRole(String userId, List<String> roleIds);

    /**
     * 拷贝角色
     * @param sourceRoleId 源角色id
     * @param targetRoleId 目标角色id
     */
    void copyRole(String sourceRoleId, String targetRoleId);

    /**
     * 查询较色模块数据项配置状态
     *
     * @param
     */
    List<ModuleDataItemModel> queryRoleModuleDataItemPermission(String roleId, String moduleId);

    /**
     * 设置角色数据项权限，是否脱敏，是否隐藏
     *
     * @param conf<module_data_id,"isHidden,isSensitive">
     *
     */
    void updateRoleModuleDateItemPermission(String roleId,String moduleId,Map<String,String> conf);
}
