package com.bitnei.cloud.sys.service;

import com.bitnei.cloud.common.api.TreeNode;
import com.bitnei.cloud.orm.bean.PagerInfo;
import com.bitnei.cloud.sys.domain.Dept;
import com.bitnei.cloud.sys.model.DeptModel;
import com.bitnei.cloud.service.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * ----------------------------------------------------------------------------- <br>
 * 工程名 ： 基础框架 <br>
 * 功能： DeptService接口<br>
 * 描述： DeptService接口，在xml中引用<br>
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
 * <td>2018-11-07 14:11:13</td>
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

public interface IDeptService extends IBaseService {

    /**
     * 全部查询
     *
     * @return 返回所有
     */
    Object list(PagerInfo pagerInfo);

    /**
     * 查询树形列表
     *
     * @return 返回所有
     */
    Object tree(PagerInfo pagerInfo);

    /**
     * 根据id获取
     *
     * @return
     */
    DeptModel get(String id);

    /**
     * 新增
     *
     * @param model 新增model
     * @return
     */
    void insert(DeptModel model);

    /**
     * 编辑
     *
     * @param model 编辑model
     * @return
     */
    void update(DeptModel model);

    /**
     * 删除多个
     *
     * @param ids id列表，用逗号分隔
     * @return 影响行数
     */
    //int deleteMulti(String ids);

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
     * 自定义参数获取组织架构信息-无权限
     *
     * @param params
     * @return
     */
    DeptModel queryDeptByParam(Map<String, Object> params);

    /**
     * 查询联系人单位下组织架构
     *
     * @param opId 联系人id
     * @return
     */
    List<DeptModel> queryDeptsByOpid(String opId);

    /**
     * 根据单位名称或简称查询组织架构
     *
     * @param unitName 单位名称或简称
     * @return
     */
    TreeNode queryDeptsByUnitName(String unitName);

    /**
     * 统计部门用户数量
     *
     * @param deptId  部门ID
     * @return
     */
    Integer statDeptUserCount(String deptId);

    /**
     * 删除单个节点
     *
     * @param id 节点id
     * @return 影响行数
     */
    void deleteDept(String id);
}
