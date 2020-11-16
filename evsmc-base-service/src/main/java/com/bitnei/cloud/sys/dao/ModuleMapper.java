package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.Module;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： ModuleMapper接口<br>
* 描述： ModuleMapper接口，在xml中引用<br>
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
* <td>2018-12-10 17:33:28</td>
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
@Mapper
public interface ModuleMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    Module findById(Map<String,Object> params);

    /**
     * 根据编码查询
     *
     * @param params
     * @return
     */
    Module findByCode(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(Module obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(Module obj);

	/**
     * 删除
     * @param params
     * @return
     */
    int delete(Map<String,Object> params);

    /**
     * 查询
     * @param params
     * @return
    */
    List<Module> pagerModel(Map<String,Object> params);


    /**
     *
     * @param params
     * @return
     */
    int replacePath(Map<String,Object> params);

    /**
     * 查询角色的首页url
     * @param roleId
     * @return
     */
    String findIndexUrl(@Param("roleId") String roleId);

    /**
     * 获取根节点和首页
     * @return
     */
    List<Module> findRootAndIndex();


    /**
     * 通过父节点和名称查询
     * @param parentId
     * @param name
     * @return
     */
    Module getByNamaAndParentId(@Param("parentId") String parentId, @Param("name") String name);

}
