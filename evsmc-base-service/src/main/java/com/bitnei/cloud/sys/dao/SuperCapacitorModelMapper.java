package com.bitnei.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import com.bitnei.cloud.sys.domain.SuperCapacitorModel;

import java.util.Map;
import java.util.List;

/**
* <p>
* ----------------------------------------------------------------------------- <br>
* 工程名 ： 基础框架 <br>
* 功能： SuperCapacitorModelMapper接口<br>
* 描述： SuperCapacitorModelMapper接口，在xml中引用<br>
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
* <td>2018-12-06 23:38:00</td>
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
public interface SuperCapacitorModelMapper {


    /**
     * 通过ID查询
     *
     * @param params
     * @return
     */
    SuperCapacitorModel findById(Map<String,Object> params);

    /**
     * 根据型号名称查询
     *
     * @param params
     * @return
     */
    SuperCapacitorModel findByName(Map<String,Object> params);

    /**
     * 根据生产厂商查询
     *
     * @param params
     * @return
     */
    SuperCapacitorModel findByUnitId(Map<String,Object> params);

    /**
     * 根据总储容量查询
     *
     * @param params
     * @return
     */
    SuperCapacitorModel findByTotalCapacity(Map<String,Object> params);

    /**
     * 根据容量密度查询
     *
     * @param params
     * @return
     */
    SuperCapacitorModel findByCapacityDensity(Map<String,Object> params);


 	/**
     * 插入
     * @param obj
     * @return
     */
    int insert(SuperCapacitorModel obj);

    /**
     * 更新
     * @param obj
     * @return
     */
    int update(SuperCapacitorModel obj);

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
    List<SuperCapacitorModel> pagerModel(Map<String,Object> params);
}
