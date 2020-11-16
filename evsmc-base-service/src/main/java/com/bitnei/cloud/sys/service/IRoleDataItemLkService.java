package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.orm.bean.PagerResult;
import com.bitnei.cloud.sys.domain.RoleDataItemDetail;
import com.bitnei.cloud.sys.domain.RoleDataItemLk;
import com.bitnei.cloud.sys.model.RoleDataItemLkModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： RoleDataItemLkService接口<br>
* 描述： RoleDataItemLkService接口，在xml中引用<br>
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
* <td>2018-11-22 10:28:44</td>
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

public interface IRoleDataItemLkService extends IBaseService {


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
     RoleDataItemLkModel get(String id);




    /**
     * 新增
     * @param model  新增model
     * @return
     */
    void insert(RoleDataItemLkModel model);

    /**
     * 编辑
     * @param model  编辑model
     * @return
     */
    void update(RoleDataItemLkModel model);

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
     * 分配角色数据项权限，是否脱敏，是否隐藏
     *
     * @param
     */
    void updateRoleDateItemPermission(String roleId,String moduleId,List<String> hiddenIds,List<String> sensitiveIds);

    void deleteRoleModuleLk(String roleId);
    void deleteRoleDataItem(String roleId);

    /**
     * 查询角色模块列表
     *
     * @param
     */
    List<String> queryRoleModuleList(String roleId);
    /**
     * 查询角色数据项权限
     *
     * @param
     */
    List<RoleDataItemLk> queryRoleModuleDateItemPermission(String roleId);


    /**
     * 查询角色数据项权限
     *
     * @param
     */
    List<RoleDataItemLk> queryRoleModuleDateItemPermission(String roleId, String moduleId);


    /**
     * 根据角色id和模块编码
     * @param roleId
     * @param moduleCode
     * @return
     */
    List<RoleDataItemDetail> queryByRoleIdAndCode(String roleId, String moduleCode);


    /**
     * 根据角色id
     * @param roleId 角色id
     * @return 菜单按钮数据项权限编码集
     */
    List<Map<String,Object>> queryRoleModuleByRoleId(String roleId);

    /**
     * 查询角色模块
     * @param roleId
     * @return
     */
    List<Map<String, Object>> queryModuleByRoleId(String roleId);

}
