package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.UnitType;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： UnitTypeMapper接口<br>
* 描述： UnitTypeMapper接口，在xml中引用<br>
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
* <td>2018-12-20 11:48:35</td>
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
public interface UnitTypeMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    UnitType findById(Map<String,Object> params);

    /**
     * 根据名称查询
     *
     * @param params
     * @return
     */
    UnitType findByName(Map<String,Object> params);

    /**
     * 根据编码查询
     *
     * @param params
     * @return
     */
    UnitType findByCode(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(UnitType obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(UnitType obj);

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
    List<UnitType> pagerModel(Map<String,Object> params);

    /**
     * 查询下级类型
     * @param parentID
     * @return
     */
    List<UnitType> findNext(String parentId);

    /**
     * 更新下级路径
     * @param params
     */
    void updateChildPath(@Param("oldpath") String oldPath, @Param("newpath") String newPath);
}
