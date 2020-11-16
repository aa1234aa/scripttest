package com.bitnei.cloud.sys.dao;

import com.bitnei.cloud.sys.domain.HashValue;
import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.CoreResourceItem;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： CoreResourceItemMapper接口<br>
* 描述： CoreResourceItemMapper接口，在xml中引用<br>
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
* <td>2018-12-26 15:10:50</td>
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
@Mapper
public interface CoreResourceItemMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    CoreResourceItem findById(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(CoreResourceItem obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(CoreResourceItem obj);

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
    List<CoreResourceItem> pagerModel(Map<String,Object> params);

    /**
     * 根据id删除资源属性
     * @param id
     * @return
     */
    void deleteByResourceId(String id);


    /**
     * 获取车辆的项目
     *
     * @return
     */
    List<CoreResourceItem> findVehicleItems();


    /**
     * 获取车辆自身属性
     * @return
     */
    CoreResourceItem getVehicleSelfItem();



    /**
     * 获取资源自身属性
     * @return
     */
    CoreResourceItem getResourceSelfItem(String resourceId);


    /**
     * 获取哈希值列表
     * @param params
     * @return
     */
    List<HashValue> findHashValuesByResItemIdAndIds(Map<String,Object> params);


    /**
     * 查找资源
     * @param params
     * @return
     */
    List<Map<String,Object>> queryResources(Map<String,Object> params);


    /**
     * 更新路径
     * @param newPath
     * @param oldPath
     * @return
     */
    int updatePath(@Param("newPath") String newPath, @Param("oldPath") String oldPath);





}
